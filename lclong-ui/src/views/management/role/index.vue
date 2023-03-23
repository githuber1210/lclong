<template>
  <div>
    <el-input style="width: 100px" placeholder="角色名称" v-model="keyword"></el-input>
    <el-button icon="el-icon-search"  @click="load"></el-button>
    <el-button icon="el-icon-refresh" @click="refresh"></el-button>
    <el-button icon="el-icon-plus" @click="add" circle ></el-button>
    <div style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);margin-top: 20px">
      <el-table :data="tableData">
        <el-table-column prop="name" label="角色名称"></el-table-column>
        <el-table-column label="权限字符">
          <template slot-scope="scope">
            <el-tag type="success">
              {{ scope.row.roleKey }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column prop="createBy" label="创建人" width="100"></el-table-column>
        <el-table-column prop="updateTime" label="最近修改时间"></el-table-column>
        <el-table-column prop="updateBy" label="最近修改人" width="100"></el-table-column>
        <el-table-column>
          <template slot-scope="scope">
            <el-button icon="el-icon-menu" @click="selectMenu(scope.row)" circle></el-button>
            <el-button icon="el-icon-menu" @click="selectPermission(scope.row)" type="warning" circle></el-button>
            <el-button icon="el-icon-edit" @click="edit(scope.row)" type="primary" circle></el-button>
            <el-popconfirm
                style="margin-left: 10px"
                confirm-button-text='确定'
                cancel-button-text='取消'
                icon="el-icon-info"
                icon-color="red"
                title="確定刪除？"
                @confirm="del(scope.row.id)"
            >
              <el-button type="danger" slot="reference" icon="el-icon-delete" circle></el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div style="padding: 30px 0;text-align: center">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10,20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="角色信息表" :visible.sync="dialogFormVisible">
      <el-form>
        <el-form-item label="名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="权限字符">
          <el-input v-model="form.roleKey"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="权限分配" :visible.sync="menuDialogVis">
      <el-tree
          :check-strictly="true"
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          default-expand-all
          :default-checked-keys="menuChecks"
          ref="tree"
      >
         <span slot-scope="{ data }"> <i :class="data.icon"></i> {{ data.name }} </span>
      </el-tree>
      <div slot="footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenus">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="权限分配" :visible.sync="permissionDialogVis">
      <el-tree
          :check-strictly="true"
          :props="props"
          :data="permissionData"
          show-checkbox
          node-key="id"
          default-expand-all
          :default-checked-keys="permissionChecks"
          ref="tree"
      >
        <span slot-scope="{ data }"> <i :class="data.icon"></i> {{ data.name }} </span>
      </el-tree>
      <div slot="footer">
        <el-button @click="permissionDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRolePermissions">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
export default {
  name: "Role",
  data() {
    return {
      tableData: [],
      form: {},
      keyword: "",
      total: 0,
      pageNum: 1,
      pageSize: 10,
      dialogFormVisible: false,
      menuDialogVis: false,
      permissionDialogVis: false,
      menuData: [],
      permissionData: [],
      props: {},
      menuChecks: [],
      permissionChecks: [],
      roleId: 0,
      ids: [],
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/role/list",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          keyword: this.keyword,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
      this.request.get("/menu/listIds").then(res => {
        this.ids = res.data
      })
    },
    add() {
      this.dialogFormVisible = true
      this.form = {}
    },
    selectMenu(role) {
      this.request.get("/menu/list").then(res => {
        this.menuData = res.data
      })
      this.roleId = role.id
      this.request.get("/roleMenu/" + this.roleId).then(res => {
        this.menuChecks = res.data
        this.menuDialogVis = true
      })
    },
    selectPermission(role) {
      this.request.get("/permission/list").then(res => {
        this.permissionData = res.data
      })
      this.roleId = role.id
      this.request.get("/roleMenu/permission/" + this.roleId).then(res => {
        this.permissionChecks = res.data
        this.permissionDialogVis = true
      })
    },
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/role/" + id).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    save() {
      if (this.form.id != null) {
        this.request.put("/role/update", this.form).then(res => {
          if (res.code === 200) {
            this.$message.success("修改成功!")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      } else {
        this.request.post("/role/create", this.form).then(res => {
          if (res.code === 200) {
            this.$message.success("添加成功!")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      }
    },
    refresh(){
      this.keyword = '';
      this.load();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    saveRoleMenus() {
      this.request.post("/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        if (res.code === 200) {
          this.$message.success("绑定成功!")
          this.menuDialogVis = false
        }
      })
    },
    saveRolePermissions(){
      this.request.post("/roleMenu/permission/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        if (res.code === 200) {
          this.$message.success("绑定成功!")
          this.permissionDialogVis = false
        }
      })
    }
  }
}
</script>


<style>

</style>

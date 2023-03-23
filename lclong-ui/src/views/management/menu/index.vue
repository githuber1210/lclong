<template>
  <div>

    <div style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);margin-top: 20px">

      <el-table :data="tableData"
                row-key="id"
                default-expand-all
          >
        <el-table-column prop="name" label="菜单名称"></el-table-column>
        <el-table-column label="图标" width="50">
          <template slot-scope="scope">
            <span :class="scope.row.icon" />
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由"></el-table-column>
        <el-table-column prop="component" label="组件名称"></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column prop="createBy" label="创建人" width="80px"></el-table-column>
        <el-table-column prop="updateTime" label="最近修改时间"></el-table-column>
        <el-table-column prop="updateBy" label="最近修改人" width="80px"></el-table-column>

        <el-table-column>
          <template slot-scope="scope">
            <el-button icon="el-icon-plus" @click="add(scope.row.id)" circle></el-button>
            <el-button  icon="el-icon-edit" @click="edit(scope.row)" type="primary" circle ></el-button>
            <el-popconfirm
                style="margin-left:10px"
                confirm-button-text='确定'
                cancel-button-text='取消'
                icon="el-icon-info"
                icon-color="red"
                title="确定删除？"
                @confirm="del(scope.row.id)"
            >
              <el-button type="danger" slot="reference" icon="el-icon-delete" circle ></el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog title="" :visible.sync="dialogFormVisible">
      <el-form>
        <el-form-item label="菜单名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path"></el-input>
        </el-form-item>
        <el-form-item label="组件">
          <el-input v-model="form.component"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-select v-model="form.icon">
            <el-option v-for="item in icons" :key="item.id" :label="item.name" :value="item.value">
              <i :class="item.value" />{{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Menu",
  data() {
    return {
      tableData: [],
      form: {},
      dialogFormVisible: false,
      icons: [],
      pid:'',
      key:''
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/menu/list").then(res => {
          if (res.code === 200){
            this.tableData = res.data
          }else {
            this.$message.error(res.msg)
          }
      })
      this.request.get("/dict/icons").then(res => {
        this.icons = res.data
      })
    },
    save() {
      if (this.form.id != undefined) {
        this.request.put("/menu/update", this.form).then(res => {
          if (res.code === 200) {
            this.$message.success("修改成功!")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      } else {
        this.request.post("/menu/create", this.form).then(res => {
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

    add(pid) {
      this.dialogFormVisible = true
      this.form = {}
      if (pid) {
        this.form.pid = pid
      }
    },
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/menu/delete/" + id).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    refresh(){
      this.name = '',
      this.load()
    }

  }
}
</script>


<style>

</style>

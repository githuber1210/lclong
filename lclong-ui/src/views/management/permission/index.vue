<template>
  <div>
    <el-input style="width: 100px" placeholder="角色名称" v-model="keyword"></el-input>
    <el-button icon="el-icon-search"  @click="load"></el-button>
    <el-button icon="el-icon-refresh" @click="refresh"></el-button>
    <el-button icon="el-icon-plus" @click="add" circle ></el-button>
    <div style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);margin-top: 20px">
      <el-table :data="tableData"
                default-expand-all
                row-key="id">
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="url" label="路由"></el-table-column>
        <el-table-column prop="description" label="组件名称"></el-table-column>
        <el-table-column>
          <template slot-scope="scope">
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
        <el-form-item label="名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.url"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description"></el-input>
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
      keyword:''
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/permission/list").then(res => {
        if (res.code === 200){
          this.tableData = res.data
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    save() {
      if (this.form.id != undefined) {
        this.request.put("/permission/update", this.form).then(res => {
          if (res.code === 200) {
            this.$message.success("修改成功!")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      } else {
        this.request.post("/permission/create", this.form).then(res => {
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
      this.request.delete("/permission/delete/" + id).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    refresh(){
      this.keyword = '';
      this.load();
    }


  }
}
</script>


<style>

</style>

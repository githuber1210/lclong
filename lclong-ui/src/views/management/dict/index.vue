<template>
  <div>
    <el-input style="width: 100px" placeholder="字典名称" v-model="keyword"></el-input>
    <el-button icon="el-icon-search"  @click="load"></el-button>
    <el-button icon="el-icon-refresh" @click="refresh"></el-button>
    <el-button icon="el-icon-plus" @click="add(null)" circle ></el-button>

    <div style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);margin-top: 20px;">
      <el-table :data="tableData"
                default-expand-all
                row-key="id">
        <el-table-column prop="id" label="字典编号"></el-table-column>
        <el-table-column prop="name" label="字典名称"></el-table-column>
        <el-table-column prop="value" label="字典值"></el-table-column>
        <el-table-column prop="type" label="字典类型"></el-table-column>
        <el-table-column prop="time" label="最后修改时间"></el-table-column>
        <el-table-column>
          <template slot-scope="scope">
            <el-button icon="el-icon-plus" @click="add(scope.row.id)" circle></el-button>
            <el-button  icon="el-icon-edit" @click="edit(scope.row)" type="primary" circle></el-button>
            <el-popconfirm
                style="margin-left:10px"
                confirm-button-text='确定'
                cancel-button-text='取消'
                icon="el-icon-info"
                icon-color="red"
                title="确定删除？"
                @confirm="del(scope.row.id)"
            >
              <el-button type="danger" slot="reference" icon="el-icon-delete"circle></el-button>
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

    <el-dialog title="" :visible.sync="dialogFormVisible">
      <el-form>
        <el-form-item label="字典编号">
          <el-input v-model="form.id"></el-input>
        </el-form-item>
        <el-form-item label="字典名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="字典值">
          <el-input v-model="form.value"></el-input>
        </el-form-item>
        <el-form-item label="字典类型">
          <el-input v-model="form.type"></el-input>
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
  name: "Dict",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      keyword: '',
      form: {},
      dialogFormVisible: false,
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/dict/list", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          keyword: this.keyword
        }
      }).then(res => {
        if (res.code === 200){
          this.tableData = res.data.records
          this.total = res.data.total
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    save() {
      if (this.form.id != undefined) {
        this.request.put("/dict/update", this.form).then(res => {
          if (res.code === 200) {
            this.$message.success("修改成功!")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      } else {
        this.request.post("/dict/create", this.form).then(res => {
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
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
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
      this.request.delete("/dict/delete/" + id).then(res => {
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

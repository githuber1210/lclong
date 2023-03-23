<template>
  <div>

      <el-table :data="tableData" @selection-change="handleSelectionChange">
        <el-table-column prop="cover" label="封面">
          <template slot-scope="{row}">
            <el-popover v-if="row.cover" placement="right" trigger="hover">
              <el-image style="width: 360px; height: 240px" :src="row.cover" fit="cover"></el-image>
              <el-image slot="reference" style="width: 100px; height: 100px" :src="row.cover" fit="cover"></el-image>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="名称"></el-table-column>
        <el-table-column prop="abs" label="介绍"></el-table-column>
        <el-table-column prop="date" label="时间"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="primary" icon="el-icon-edit"  circle @click="edit(scope.row)"></el-button>
            <el-popconfirm
                style="margin-left:10px"
                confirm-button-text='确定'
                cancel-button-text='取消'
                icon="el-icon-info"
                icon-color="red"
                title="確定刪除？"
                @confirm="del(scope.row.id)"
            >
              <el-button type="danger" icon="el-icon-delete"  slot="reference" circle></el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

    <div style="text-align: center">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[10,20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

  </div>
</template>


<script>


export default {
  data() {
    return {
      form: {},
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      dialogFormVisible: false,
      teachers: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      content: '',
      viewDialogVis: false
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/product/list", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {

        this.tableData = res.data.records
        this.total = res.data.total

      })

    },

    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/product/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)
      this.request.post("/product/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    save() {
      this.request.post("/product", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    }

  }
}
</script>

<style scoped>

</style>

<template>
  <div style="padding: 10px">
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入订单编号" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
    >
      <el-table-column
          width="70"
          prop="id"
          label="ID"
          sortable
      >
      </el-table-column>
      <el-table-column
          width="150"
          prop="name"
          label="周边名称">
      </el-table-column>
      <el-table-column
          width="160"
          prop="orderNo"
          label="订单编号">
      </el-table-column>
      <el-table-column
          prop="payPrice"
          label="付款金额">
      </el-table-column>
      <el-table-column
          prop="username"
          label="付款人">
      </el-table-column>
      <el-table-column
          width="150"
          prop="createTime"
          label="创建时间">
      </el-table-column>
      <el-table-column
          width="150"
          prop="payTime"
          label="付款时间">
      </el-table-column>
      <el-table-column label="支付状态">
        <template slot-scope="scope">
          <span v-if="scope.row.state === 0">
            <el-tag type="danger">
              未支付
            </el-tag>
          </span>
          <span v-if="scope.row.state === 1">
            <el-tag type="success">
              已支付
            </el-tag>
          </span>
          <span v-if="scope.row.state === 2" style="color: orange">
            <el-tag type="info">
              已超时
            </el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240">
        <template slot-scope="scope">

          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button  type="danger">删除</el-button>
            </template>
          </el-popconfirm>
          <el-button type="primary" style="margin-left: 10px" @click="handlePay(scope.row)" v-if="scope.row.state === 0">
              支付
          </el-button>
        </template>

      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
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
      user: {},
      loading: true,
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.loading = true
     this.request.get("/order", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.loading = false
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add() {
      this.dialogVisible = true
      this.form = {}
      if (this.$refs['upload']) {
        this.$refs['upload'].clearFiles()  // 清除历史文件列表
      }
    },
    save() {
      if (this.form.id) {  // 更新
        this.request.put("/order", this.form).then(res => {
          console.log(res)
          if (res.code === 200) {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }  else {  // 新增
        this.request.post("/order", this.form).then(res => {
          console.log(res)
          if (res.code === 200) {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }

          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles()  // 清除历史文件列表
        }
      })

    },
    handleDelete(id) {
      console.log(id)
      this.request.delete("/order/" + id).then(res => {
        if (res.code === 200) {
          this.$message({
            type: "success",
            message: "删除成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load()  // 删除之后重新加载表格的数据
      })
    },
    handlePay(row){
      this.request.get("/order/pay/" , {
        params: {
          subject:row.name,
          traceNo:row.orderNo,
          totalAmount:row.payPrice
        }
      }).then(res => {
        window.open(res.data)
      })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>

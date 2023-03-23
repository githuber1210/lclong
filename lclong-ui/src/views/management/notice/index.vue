<template>
  <div>
      <el-input style="width: 200px" placeholder="通告标题" v-model="title"></el-input>
      <el-button icon="el-icon-search"  @click="load"></el-button>
      <el-button icon="el-icon-plus"    @click="add"></el-button>
      <el-popconfirm
          style="margin-left: 10px"
          confirm-button-text='确定'
          cancel-button-text='取消'
          icon="el-icon-info"
          icon-color="red"
          title="確定刪除？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" icon="el-icon-delete" ></el-button>
      </el-popconfirm>

    <el-date-picker
        style="margin-left: 10px"
        v-model="value1"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="yyyy-MM-dd HH:mm:ss"
    >
    </el-date-picker>
    <el-button @click="findByTimeRange" icon="el-icon-search"></el-button>
    <el-button icon="el-icon-refresh" @click="refresh"></el-button>

    <div style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);margin-top: 20px">
      <el-table :data="tableData"  @selection-change="handleSelectionChange">
        <el-table-column type="selection"></el-table-column>
        <el-table-column prop="title" label="通告标题"></el-table-column>
        <el-table-column prop="author" label="通告发布者"></el-table-column>
        <el-table-column prop="createTime" label="通告发布时间"></el-table-column>
        <el-table-column>
          <template slot-scope="scope">
            <el-button @click="$router.push('/noticeDetail?id='+scope.row.id)" icon="el-icon-search" circle></el-button>
            <el-button @click="edit(scope.row)" icon="el-icon-edit" type="primary" circle></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div style="padding: 30px 0;text-align: center">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5,10,15]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog :visible.sync="dialogFormVisible">
      <el-form >
        <el-form-item label="文章标题">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item>
          <mavon-editor v-model="form.content"  ref="md"  :ishljs="true" @imgAdd="imgAdd"/>
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

import axios from "axios";
import {serverIp} from "../../../../public/config";
export default {
  name: "Notice",
  data() {
    return {
      form: {},
      tableData: [],
      title: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 8,
      total: 0,
      dialogFormVisible: false,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      content: '',
      value1: ''
    }
  },
  created() {
    this.load()
  },
  methods: {
    imgAdd(pos, $file) {
      let $vm = this.$refs.md
      const formData = new FormData();
      formData.append('file', $file);
      axios({
        url: 'http://' + serverIp + ':9999/file/upload',
        method: 'post',
        data: formData,
        headers: {'Content-Type': 'multipart/form-data'},
      }).then((res) => {
        //将返回的url替换到文本原位置![...](./0) -> ![...](url)
        $vm.$img2Url(pos, res.data);
      })
    },
    load() {
      this.request.get("/notice/list", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title,
        }
      }).then(res => {
        if (res.code == 200){
          this.tableData = res.data.records
          this.total = res.data.total
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    add() {
      this.dialogFormVisible = true
      this.form = {}
    },
    edit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  
      this.request.post("/notice/delete/batch", ids).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.load()
        }
      })
    },
    save() {
      if (this.form.id != undefined) {
        this.request.put("/notice/update", this.form).then(res => {
          if (res.code === 200) {
            this.$message.success("修改成功!")
            this.dialogFormVisible = false
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      } else {
        this.request.post("/notice/create", this.form).then(res => {
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
    findByTimeRange(){
    if (!this.value1){
      this.$message.error("请选择时间")
    }else {
      this.request.get("/retrieve/notice",{
        params:{
          start:this.value1.at(0),
          end:this.value1.at(1)
        }
      }).then(res=>{
        if (res.code === 200){
          this.tableData = res.data
        }
      })
    }},
    refresh(){
      this.title=''
      this.value1=''
      this.load()
    }

  }
}
</script>

<style scoped>

</style>

<template>
 <div>
      <el-input style="width: 200px" placeholder="文件名称" v-model="keyword"></el-input>
      <el-button icon="el-icon-search"  @click="load"></el-button>
      <el-button icon="el-icon-refresh" @click="refresh"></el-button>
      <el-upload  :action="'http://' + serverIp + ':9999/file/upload'"
                  :show-file-list="false"
                  :on-success="handleFileUploadSuccess"
                  :headers="headerStr"
                  style="display: inline-block;margin-left:10px">
          <el-button icon="el-icon-plus" ></el-button>
       </el-upload>    
        <el-popconfirm
          style="margin-left: 10px"
          confirm-button-text='确定'
          cancel-button-text='取消'
          icon="el-icon-info"
          icon-color="red"
          title="確定刪除?"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference" icon="el-icon-delete"></el-button>
      </el-popconfirm>
   <div style="box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);margin-top: 20px">
      <el-table :data="tableData"   @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="name" label="文件名称" width="150"></el-table-column>
        <el-table-column prop="size" label="文件大小"></el-table-column>
        <el-table-column prop="type" label="文件类型"></el-table-column>
        <el-table-column prop="time" label="文件上传时间" width="200"></el-table-column>
        <el-table-column prop="url" label="文件路径" width="450"></el-table-column>
        <el-table-column label="下载" width="80px">
          <template slot-scope="scope">
            <el-button icon="el-icon-download" circle @click="download(scope.row.url)"></el-button>
          </template>
        </el-table-column>
        <el-table-column label="预览" width="80px">
          <template slot-scope="scope">
            <el-button icon="el-icon-search" circle  @click="preview(scope.row.url)" disabled></el-button>
          </template>
        </el-table-column>
      </el-table>
   </div>

    <div style="padding: 30px 0;text-align: center">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5,10,20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {serverIp} from "../../../../public/config";

export default {
  name: "File",
  data() {
    return {
      serverIp: serverIp,
      tableData: [],
      keyword: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      headerStr: {
        Authorization:localStorage.getItem("token") ? JSON.parse(localStorage.getItem("token")) : null,
      }
    }
  },
  created() {
    this.load()

  },
  methods: {
    load() {
      this.request.get("/file/list", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          keyword: this.keyword,
        }
      }).then(res => {
        if (res.code == 200){
          this.tableData = res.data.records
          this.total = res.data.total
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)
      this.request.post("/file/delete/batch", ids).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    refresh(){
      this.keyword = ''
      this.load()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    handleFileUploadSuccess() {
      this.$message.success("上传成功")
      this.load()
    },
    download(url) {
      window.open(url)
    },
    preview(url){
      window.open('https://file.keking.cn/onlinePreview?url=' + encodeURIComponent(window.btoa((url))))
    }

  }
}
</script>

<style scoped>

</style>

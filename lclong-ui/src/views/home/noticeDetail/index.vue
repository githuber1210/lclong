<template>
  <div style="color: #666">
    <div style="margin: 20px 0; ">
      <div class="pd-10" style="font-size: 20px; color: #3F5EFB; cursor: pointer">{{ notice.name }}</div>
      <div style="font-size: 14px; margin-top: 10px">
        <i class="el-icon-user-solid"></i> <span>{{ notice.author }}</span>
        <i class="el-icon-time" style="margin-left: 10px"></i> <span>{{ notice.createTime }}</span>
      </div>
    </div>

    <div style="margin: 20px 0">
      <mavon-editor
          class="md"
          :value="notice.content"
          :subfield="false"
          :defaultOpen="'preview'"
          :toolbarsFlag="false"
          :editable="false"
          :scrollStyle="true"
          :ishljs="true"
      />
    </div>

    <div style="margin: 30px 0">
      <div style="margin: 10px 0">
        <div style="border-bottom: 1px solid #6c87f5; padding: 10px 0; font-size: 20px">评论</div>
        <div style="padding: 10px 0">
          <el-input size="small"  v-model="commentForm.content"></el-input>
        </div>
        <div class="pd-10" style="text-align: right">
          <el-button icon="el-icon-position" type="primary" @click="save"></el-button>
        </div>
      </div>

      <div>
        <div v-for="item in comments" :key="item.id" style="border-bottom: 1px solid #ccc; padding: 10px 0; ">

          <div style="display: flex">
            <div style="width: 100px; text-align: center">
              <el-image :src="item.avatar" style="width: 50px; height: 50px; border-radius: 50%"></el-image>
            </div>
            <div style="flex: 1; font-size: 14px; padding: 5px 0; line-height: 25px">
              <b>{{ item.username }}：</b>
              <span>{{ item.content }}</span>

              <div style="display: flex; line-height: 20px; margin-top: 5px">
                <div style="width: 200px;">
                  <i class="el-icon-time"></i><span style="margin-left: 5px">{{ item.time }}</span>
                </div>
                <div style="text-align: right; flex: 1">
                  <el-button style="margin-left: 5px" type="text" @click="handleReply(item.id)">回复</el-button>
                  <el-button type="text" style="color: red" @click="del(item.id)" v-if="user.id === item.userId">删除</el-button>
                </div>
              </div>
            </div>
          </div>

          <div v-if="item.children.length"  style="padding-left: 200px;">
            <div v-for="subItem in item.children" :key="subItem.id"  style="border: 1px solid #3a8ee6; padding: 20px 20px">
              <div style="font-size: 14px; padding: 5px 0; line-height: 25px">
                <div>
                  <b style="color: #3a8ee6" v-if="subItem.puname">回复@{{ subItem.puname }}</b>
                </div>
                <div style="padding-left: 5px">
                  <b>{{ subItem.username }}：</b> <span>{{ subItem.content }}</span>
                </div>
                <div style="display: flex; line-height: 20px; margin-top: 5px; padding-left: 5px">
                  <div style="width: 200px;">
                    <i class="el-icon-time"></i><span style="margin-left: 5px">{{ subItem.time }}</span>
                  </div>
                  <div style="text-align: right; flex: 1">
                    <el-button style="margin-left: 5px" type="text" @click="handleReply(subItem.id)">回复</el-button>
                    <el-button type="text" style="color: red" @click="del(subItem.id)" v-if="user.id === subItem.userId">删除</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>


    <el-dialog title="回复" :visible.sync="dialogFormVisible">
      <el-form>
        <el-form-item label="回复内容">
          <el-input type="textarea" v-model="commentForm.contentReply"></el-input>
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
  name: "NoticeDetail",
  data() {
    return {
      notice: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      comments: [],
      commentForm: {},
      id: this.$route.query.id,
      dialogFormVisible: false
    }
  },
  created() {
    this.load()
    this.loadComment()
  },
  methods: {
    load() {
      this.request.get("/notice/retrieve/" + this.id).then(res => {
        this.notice = res.data
      })
    },
    loadComment() {
      this.request.get("/comment/tree/" + this.id).then(res => {
        this.comments = res.data
      })
    },
    save() {
      this.commentForm.bid = this.id
      if (this.commentForm.contentReply) {
        this.commentForm.content = this.commentForm.contentReply
      }
      this.request.post("/comment", this.commentForm).then(res => {
        if (res.code === 200) {
          this.$message.success("评论成功")
          this.commentForm = {}
          this.loadComment()
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {
      this.request.delete("/comment/" + id).then(res => {
        if (res.code === 200) {
          this.$message.success("删除成功")
          this.loadComment()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleReply(pid) {
      this.commentForm = { pid: pid }
      this.dialogFormVisible = true
    }
  }
}
</script>

<style scoped>

</style>

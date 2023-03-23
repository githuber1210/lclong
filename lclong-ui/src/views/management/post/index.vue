<template>
  <el-row>
    <el-col :span="24">
      <el-form  ref="dataForm"
                :model="editDataModel"
                label-position="right"
                abel-width="0"
                :show-message="false"
                :status-icon="false">
            <!-- 标题 -->
            <el-form-item  prop="postTitle">
              <el-input v-model="editDataModel.postTitle" maxlength="100" placeholder="请输入标题..." style="width: 750px" />
              <el-button v-if="editMode === 'm'" type="danger" @click="handleDelete" style="margin-left: 10px">
                删除
              </el-button>
              <el-button v-if="editDataModel.postStatus === 'DRAFT'" @click="saveData('DRAFT')"  style="margin-left: 10px">
                保存草稿
              </el-button>
              <el-button type="primary" @click="pubButtonClick">
                发表
              </el-button>
            </el-form-item>

        <el-form-item prop="postContent">
          <mavon-editor v-model="editDataModel.postContent"
                        ref="md"
                        :style="'height:'+ mdEditorHeight + ';box-shadow:none;border:1px solid #efefef;'"
                        @change="handleEditorChange"
                        :toolbars="toolbars" >
          </mavon-editor>
        </el-form-item>
      </el-form>
    </el-col>

    <el-dialog title="发表选项" :visible="pubDialogShow" :show-close="false" width="800px">
      <el-form ref="pubForm":model="editDataModel" label-position="right" label-width="100px">
        <el-form-item label="标签">
          <el-select v-model="selectedTagArray" class="full-row" filterable :multiple-limit="5" multiple placeholder="可输入文字查询">
            <el-option v-for="item in allTagList" :key="item.postTagId" :label="item.description" :value="item.postTagId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="摘要">
          <el-input v-model="editDataModel.postExcerpt" :rows="3" :autosize="{ minRows: 3, maxRows: 3}" type="textarea" placeholder="请输入摘要" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="封面图">
          <div @mouseover="articleCoverOpLayerShow = true" @mouseleave="articleCoverOpLayerShow = false">
            <el-upload :action="uploadUrl"
                       :show-file-list="false"
                       :on-success="handleArticleCoverSuccess"
            >
              <el-image v-if="articleCoverUrl" :src="articleCoverUrl" fit="cover" class="article-cover"></el-image>
              <i v-else class="el-icon-plus cover-uploader-icon"></i>
              <div v-show="articleCoverUrl && articleCoverOpLayerShow" class="op-layer">
                <el-button type="danger" size="mini" @click.stop="articleCoverUrl=''">
                  <i class="el-icon-refresh"></i>删除</el-button>
              </div>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="指定时间">
          <el-date-picker
              v-model="editDataModel.postDate"
              value-format="yyyy-MM-dd HH:mm:ss"
              type="datetime"
              placeholder="选择日期时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="text-right">
        <el-button @click="pubDialogShow = false">取消</el-button>
        <el-button type="primary" @click="saveData('PUBLISHED')">确定</el-button>
      </div>
    </el-dialog>
  </el-row>
</template>
<script>
import { getById, deleteData, createData, updateData, getTagList, uploadUrl } from '@/api/articles'
import { getTextFormHtml } from '@/util/tool'
export default {
  name: 'post',
  data() {
    return {
      uploadUrl,

      editDataModel: {
        postsId: undefined,
        postTitle: '',
        postContent: '', // 正文
        htmlContent: '', // html内容，现在转化了，前端直接查询展示就好了
        postExcerpt: '', // 摘要
        postType: 'POST',
        postStatus: 'DRAFT',
        termTaxonomyId: '',
        cover: '',
        tags: ''
      },

      // 所有标签列表
      allTagList: [],
      // 已经选择的标签
      selectedTagArray: [],
      articleCoverUrl: '',
      articleCoverOpLayerShow: false,

      // 文章标题相关设置数据
      otherSettings: {
        articleCoverUrl: ''
        // isBold: false, // 是否粗体
        // textColor: 'green', // 链接文字的颜色，目前支持蓝色和绿色
        // rightButton: { // 右侧按钮设置
        //   show: false, // 是否显示右侧按钮
        //   bgColor: '', // 按钮背景色：绿色(green)、蓝色(blue)、黑色(默认，没有值就是黑色)
        //   textContent: '查看更多', // 按钮上的文字
        //   linkType: '0', // 链接类型
        //   linkTo: '' // 按钮链接是否链接本文章或者可以设置任意外链，如果不填写，就链接本篇文章
        // }
      },

      // 当前编辑页面的id
      editId: null,
      // 新增时候的所属栏目id
      columnId: null,
      // 编辑模式
      editMode: null,

      // 发表弹窗可见性
      pubDialogShow: false,
      // md编辑器高度变量
      mdEditorHeight: '700px',
      // md编辑器加载项
      toolbars: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: false, // 上角标
        subscript: false, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: false, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: false, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: false, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: false, // 左对齐
        aligncenter: false, // 居中
        alignright: false, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true // 预览
      }
    }
  },
  mounted() {
    this.getSystemTagList().then(() => {
      if (this.$route.query.aid) {
        this.editId = this.$route.query.aid
        this.editMode = 'm'
        this.loadData()
      } else {
        this.editMode = 'n'
        this.columnId = this.$route.query.cid
        this.editDataModel.termTaxonomyId = this.columnId
      }
    })
    // 使md编辑器高度，随浏览器窗口自动变化
    let that = this
    that.mdEditorHeight = (window.innerHeight - 55 - 98) + 'px'
    window.addEventListener('resize', () => {
      that.mdEditorHeight = (window.innerHeight - 55 - 98) + 'px'
    })
  },
  methods: {
    // 加载编辑的数据
    loadData() {
      getById({ postsId: this.editId }).then(res => {
        this.editDataModel = res.data
        if (this.editDataModel.cover) {
          this.articleCoverUrl = this.editDataModel.cover
        }
        // 赋值界面标签数组对象
        if (this.editDataModel.tagsName) {
          let tempArr = res.tagsName.split(',')
          tempArr.forEach(x => {
            this.selectedTagArray.push(parseInt(x))
          })
        }
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 查询标签列表方法
    getSystemTagList() {
      return getTagList({
        page: 1, pageSize: 1000
      }).then(res => {
        res.data.items.forEach((item, i) => {
          item.oriIndex = i
          item.selected = false
        })
        this.allTagList = res.data.items
      })
    },

    // 上传文章封面图成功后回调函数
    handleArticleCoverSuccess(res) {
      this.articleCoverUrl = res
    },


    // md编辑器内容change事件
    handleEditorChange(value, transToHtml) {
      this.editDataModel.htmlContent = transToHtml
    },

    saveData(stateSetting) {
      this.editDataModel.postStatus = stateSetting
      this.otherSettings.articleCoverUrl = this.articleCoverUrl
      this.editDataModel.attribute = JSON.stringify(this.otherSettings)
      if (this.selectedTagArray.length > 0) {
        this.editDataModel.tags = this.selectedTagArray.join(',')
      }
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const saveFunc = this.editMode === 'n' ? createData : updateData
          saveFunc(this.editDataModel).then(() => {
            this.$notify({
              title: '成功',
              message: '保存成功',
              type: 'success',
              duration: 2000
            })
            this.$router.push('/admin/blog')
          })
        }
      })
    },
    // 发表按钮点击方法
    pubButtonClick() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (!this.editDataModel.postExcerpt && this.editDataModel.htmlContent) {
            let allText = (getTextFormHtml(this.editDataModel.htmlContent)).trim()
            this.editDataModel.postExcerpt = allText.length > 100 ? allText.substr(0, 97) + '...' : allText
          }
          this.pubDialogShow = true
        }
      })
    },
    // 文章删除方法
    handleDelete() {
      const confirmMes = '是否确认删除该文章？'
      this.$confirm(confirmMes, '系统提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteData({ postsId: this.editId }).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.$router.push('/blog')
        })
      })
    }
  },
}
</script>

<template>
  <div>
      <el-input v-model="table.queryAttribute.postTitleKeyword" placeholder="请输入标题" style="width: 300px"></el-input>
      <el-select style="margin-left:10px;" v-model="table.queryAttribute.postStatus" clearable placeholder="文章状态">
        <el-option v-for="item in statusList" :label="item.label" :value="item.value" :key="item.value">{{item.label}}</el-option>
      </el-select>
      <el-button style="margin-left:10px;"  icon="el-icon-search" @click="search"></el-button>
      <el-button  style="margin-left: 10px;" type="primary" @click="handleCreate">
        写博客
      </el-button>
      <el-table ref="multipleTable"
                :key="table.tableKey"
                :data="table.tableData"
                border
                fit
                highlight-current-row
               @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection" width="55px" />
        <el-table-column label="封面图" width="80px" align="center" class-name="article-cover-col">
          <template slot-scope="{row}">
            <el-popover v-if="row.attribute && row.attribute.articleCoverUrl" placement="right" trigger="hover">
              <el-image style="width: 360px; height: 240px" :src="row.attribute.articleCoverUrl" fit="cover"></el-image>
              <el-image slot="reference" style="width: 69px; height: 46px" :src="row.attribute.articleCoverUrl" fit="cover"></el-image>
            </el-popover>
            <el-image v-else :src="defaultArticleCoverSrc" fit="fill"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="标题" prop="postTitle">
          <template slot-scope="{row}">
            <span v-if="row.menuOrder !== 0" style="color: red">置顶中..</span>
            <el-link type="primary" @click="handleUpdate(row)">{{row.postTitle}}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="摘要" prop="postExcerpt" width="150px" ></el-table-column>
        <el-table-column label="作者" prop="username" width="100px" align="center" ></el-table-column>
        <el-table-column label="所属标签"  width="200px">
          <template slot-scope="scope">
            <span  v-for="tag in scope.row.tags" :key="tag.index">
              <el-tag style="margin-left: 3px">
                 {{tag.description}}
              </el-tag>
            </span>
          </template>
        </el-table-column>

        <el-table-column label="操作/发布时间" prop="postDate" width="155px" align="center"></el-table-column>
        <el-table-column label="状态" prop="postStatus" width="80px" :formatter="statusFilter" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.postStatus == 'DRAFT' ? 'info':row.postStatus == 'DELETED'?'danger':'success'">
              {{ statusFilter(row) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="置顶" prop="menuOrder" width="80px" align="center">
          <template slot-scope="{row}">
            <el-switch v-model="row.menuOrder"
                       :active-value="1"
                       :inactive-value="0"
                       @change="handleSettingOnTop($event, row)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200px">
          <template slot-scope="{row,$index}">
            <el-button type="primary" @click="handleUpdate(row)">
              编辑
            </el-button>
            <el-button v-if="row.status!='deleted'" type="danger" @click="handleDelete(row,$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

    <div style="text-align: center;margin-top: 15px">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="table.queryAttribute.page"
          background
          :page-size="table.queryAttribute.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[5,10,15,20]"
          :total="table.queryAttribute.total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { getList, deleteData, setTop, cancelTop } from '@/api/articles'

export default {
  data() {
    return {
      defaultArticleCoverSrc: require('@/assets/logo.png'),

      statusList: [
        { value: 'PUBLISHED', label: '已发布' },
        { value: 'DRAFT', label: '草稿箱' },
        { value: 'DELETED', label: '回收站' }
      ],

      table: {
        queryAttribute: {
          page: 1,
          pageSize: 10,
          total: 0,
          orderBy: 'menu_order',
          asc: false,
          postTitleKeyword: '',
          postStatus: 'PUBLISHED'
        },
        tableKey: 0,
        tableData: [],
        selectedRowList: []
      },
      
      editAttribute: {
        postsId: undefined,
        postTitle: '',
        postDate: '',
        postContent: '',
        postExcerpt: '',
        menuOrder: '',
        postType: 'POST',
        postStatus: 'DRAFT',
        termTaxonomyId: '',
        attribute: '',
        tags: ''
      }

    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    getList() {
      getList(this.table.queryAttribute).then(res => {
        this.table.tableData = res.data.items
        this.table.queryAttribute.total = res.data.total
      })
    },

    search() {
      this.table.queryAttribute.page = 1
      this.getList()
    },

    handleSizeChange(val) {
      this.table.queryAttribute.pageSize = val
      this.search()
    },

    handleCurrentChange(val) {
      this.table.queryAttribute.page = val
      this.getList()
    },

    statusFilter(row) {
      let statusText = ''
      const filterArr = this.statusList.filter(item => item.value === row.postStatus)
      if (filterArr.length > 0) {
        statusText = filterArr[0].label
      }
      return statusText
    },

    handleSelectionChange(val) {
      this.table.selectedRowList = val
    },

    handleCreate() {
      this.$router.push('/admin/post')
    },

    handleUpdate(row) {
      this.$router.push(`/admin/post?aid=${row.postsId}`)
    },

    handleDelete(row) {
      const confirmMes = '是否确认删除该文章？'
      this.$confirm(confirmMes, '系统提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteData({ postsId: row.postsId }).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        })
      })
    },
    handleSettingOnTop(status, row) {
      const reqFunc = status === 0 ? cancelTop : setTop
      reqFunc(row.postsId).then(() => {
        this.$notify({
          title: '成功',
          message: '操作成功',
          type: 'success',
          duration: 2000
        })
        this.getList()
      })
    }

  }
}
</script>

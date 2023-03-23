<template>
  <div>
    <div style="width: 990px; margin-left: auto; margin-right: auto;">
      <el-card style="text-align: left" >
        <div v-for="blogs in tableInfo.tableData" :key="blogs.postsId">
          <div style="float:left;width:85%;height: 150px;">
            <router-link style="text-decoration:none;color: #3F5EFB" :to="{ path:'/blogsDetail',query:{ id: blogs.postsId } }">
              <div style="display: flex">
                <strong style="font-size: 20px;flex: 1;">
                  {{ blogs.postTitle }}
                    <span v-if="blogs.menuOrder !== 0" style="color: red">置顶中..</span>
                </strong>
                <div>
                <span  v-for="tag in blogs.tags" :key="tag.index">
                  <el-tag style="margin-left: 3px" type="success">
                     {{tag.description}}
                  </el-tag>
                </span>
                </div>
              </div>
            </router-link>
            <el-divider content-position="left">
              {{blogs.postExcerpt}}
            </el-divider>
          </div>
            <el-image  v-if="blogs.queryAttribute && blogs.queryAttribute.articleCoverUrl"
                       style="margin:18px 0 0 30px;width:100px;height: 100px"
                       :src="blogs.queryAttribute.articleCoverUrl"
                       fit="cover">
            </el-image>
            <el-image v-else :src="defaultArticleCoverSrc"
                      style="margin:18px 0 0 30px;width:100px;height: 100px"
                      fit="fill">
            </el-image>
          <el-divider></el-divider>
          </div>
      </el-card>
    </div>

    <div style="text-align: center;margin-top: 15px">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="tableInfo.queryAttribute.page"
          background
          :page-size="tableInfo.queryAttribute.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[5,10,15,20]"
          :total="tableInfo.queryAttribute.total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
import { getList } from '@/api/articles'
export default {
  data() {
    return {
      defaultArticleCoverSrc: require('@/assets/logo.png'),
      tableInfo: {
        queryAttribute: {
          page: 1,
          pageSize: 10,
          total: 0,
          orderBy: 'menu_order',
          asc: false,
          postTitleKeyword: '',
          postStatus: 'PUBLISHED'
        },
        tableData: [],
      },
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    handleSizeChange(val) {
      this.tableInfo.queryAttribute.pageSize = val
      this.getList()
    },

    handleCurrentChange(val) {
      this.tableInfo.queryAttribute.page = val
      this.getList()
    },
    getList() {
      getList(this.tableInfo.queryAttribute).then(res => {
        this.tableInfo.tableData = res.data.items
        this.tableInfo.queryAttribute.total = res.data.total
      })
    },
  }
}
</script>

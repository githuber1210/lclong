<template>
  <div>
    <el-form ref="form" :model="resData" label-position="right" label-width="120px">
      <el-form-item label="网站域名">
        <el-input v-model="resData.domain"></el-input>
      </el-form-item>
      <el-form-item label="网站名称" prop="siteName">
        <el-input v-model="resData.name"></el-input>
      </el-form-item>
      <el-form-item label="网站介绍">
        <el-input v-model="resData.introduce"></el-input>
      </el-form-item>
      <el-form-item label="网站所有者">
        <el-input v-model="resData.author"></el-input>
      </el-form-item>
      <el-form-item label="网站所有者介绍">
        <el-input v-model="resData.authorIntroduce"></el-input>
      </el-form-item>
      <el-form-item label="ICP备案信息">
        <el-input v-model="resData.icp"></el-input>
      </el-form-item>
      <el-form-item label="公安网备案信息">
        <el-input v-model="resData.psb"></el-input>
      </el-form-item>
      <el-form-item label="联系电话">
        <el-input v-model="resData.tel"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="resData.email"></el-input>
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button @click="saveData">保存网站信息</el-button>
    </div>
  </div>
</template>
<script>
import { getSiteConfigInfo } from '@/api/site'
export default {
  data() {
    return {
      resData: {}
    }
  },
  methods: {
    // 加载数据方法
    loadData() {
      getSiteConfigInfo().then(res => {
        this.resData = res.data
      })
    }
  },
  mounted() {
    this.loadData()
  }
}
</script>
<style scoped>

</style>

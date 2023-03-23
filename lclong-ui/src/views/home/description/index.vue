<template>
  <div style="display: flex">
    <div>
      <img src="http://43.138.224.38:9999/file/84bb438a4b804663b83d85048f7d09dc.png" style="width: 350px">
    </div>
    <div style="width: 700px;">
      <el-form ref="form" :model="resData"  label-position="right" label-width="120px">
        <el-form-item label="网站域名">
          <el-input v-model="resData.domain" disabled></el-input>
        </el-form-item>
        <el-form-item label="网站名称" prop="siteName">
          <el-input v-model="resData.name"  maxlength="100" disabled></el-input>
        </el-form-item>
        <el-form-item label="网站介绍">
          <el-input v-model="resData.introduce"  maxlength="1000" disabled></el-input>
        </el-form-item>
        <el-form-item label="网站所有者">
          <el-input v-model="resData.author" disabled></el-input>
        </el-form-item>
        <el-form-item label="网站所有者介绍">
          <el-input v-model="resData.authorIntroduce" disabled ></el-input>
        </el-form-item>
        <el-form-item label="ICP备案信息">
          <el-input v-model="resData.icp" disabled></el-input>
        </el-form-item>
        <el-form-item label="公安网备案信息">
          <el-input v-model="resData.psb" disabled></el-input>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="resData.tel" disabled></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="resData.email" disabled></el-input>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
import { getSiteConfigInfo } from '@/api/site'
export default {
  data() {
    return {
      resData: {},
    }
  },
  methods: {
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

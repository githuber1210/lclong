<template>
  <div>
    <el-form ref="form" :model="form" label-position="right" label-width="120px">
      <el-form-item label="网站域名">
        <el-input v-model="form.domain"></el-input>
      </el-form-item>
      <el-form-item label="网站名称" prop="siteName">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="网站介绍">
        <el-input v-model="form.introduce"></el-input>
      </el-form-item>
      <el-form-item label="网站所有者">
        <el-input v-model="form.author"></el-input>
      </el-form-item>
      <el-form-item label="网站所有者介绍">
        <el-input v-model="form.authorIntroduce"></el-input>
      </el-form-item>
      <el-form-item label="ICP备案信息">
        <el-input v-model="form.icp"></el-input>
      </el-form-item>
      <el-form-item label="公安网备案信息">
        <el-input v-model="form.psb"></el-input>
      </el-form-item>
      <el-form-item label="联系电话">
        <el-input v-model="form.tel"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email"></el-input>
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button @click="saveData">保存网站信息</el-button>
    </div>
  </div>
</template>

<script>
import { getSiteConfigInfo,updateSiteConfig } from '@/api/site'
export default {
  data() {
    return {
      form: {}
    }
  },
  methods: {
    loadData() {
      getSiteConfigInfo().then(res => {
        this.form = res.data
      })
    },
    saveData(){
      updateSiteConfig(this.form).then(res => {
        if(res.code === 200){
          this.$message.success("保存成功")
          this.loadData()
        }
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

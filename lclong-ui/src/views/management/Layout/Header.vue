<template>
  <div style="line-height: 60px; display: flex;">
    <div style="flex:1;">
      <el-breadcrumb separator="/" style="display: inline-block; margin-left: 20px">
        <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentMenu }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-dropdown>
        <span>{{ user.username }}</span><i class="el-icon-arrow-down el-icon--right"></i>
      <el-dropdown-menu slot="dropdown" >
        <el-dropdown-item >
          <router-link to="/" style="text-decoration: none">网站首页</router-link>
        </el-dropdown-item>
        <el-dropdown-item >
          <router-link to="/admin/person" style="text-decoration: none">个人信息</router-link>
        </el-dropdown-item>
        <el-dropdown-item>
          <b @click="logout">登出系统</b>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import router from "@/router";

export default {
  name: "Header",
  props: {
    collapseBtn: String,
    user: Object
  },
  computed: {
    currentMenu () {
      return this.$store.state.currentMenu;
    }
  },
  data() {
    return {
    }
  },
  methods:{
    logout() {
      this.request.post("/logout").then(res =>{
        if(res.code === 200){
          localStorage.removeItem("user")
          localStorage.removeItem("menus")
          localStorage.removeItem("token")
          router.push("/login")
          this.$message.success("登出系统成功!")
        }
      })
    },
  }
}
</script>

<style scoped>
</style>

<template>
  <div style="display: flex;">
    <div style="margin-top: 10px">
      <img src='@/assets/logo.png' width="40px">
    </div>

    <div style="flex: 1">
      <el-menu
          :default-active="currentPath"
          router
          mode="horizontal"
          text-color="#222"
          active-text-color="green"
      >
        <el-menu-item v-for="(item,i) in navList" :key="i" :index="item.name">
          {{ item.navItem }}
        </el-menu-item>
      </el-menu>
    </div>

    <div style="width: 200px;margin-top: 20px">
      <div v-if="!user.username" style="text-align: right; padding-right: 30px">
        <el-button @click="$router.push('/login')">登录</el-button>
        <el-button @click="$router.push('/register')">注册</el-button>
      </div>
      <div v-else>
        <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
          <div style="display: inline-block">
            <img :src="user.avatarUrl" alt=""
                 style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
            <span>登录用户:{{ user.username }}</span>
          </div>
          <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
            <el-dropdown-item style="font-size: 14px; padding: 5px 0">
              <router-link to="/admin/home">个人中心</router-link>
            </el-dropdown-item>
            <el-dropdown-item style="font-size: 14px; padding: 5px 0">
              <b @click="logout">登出系统</b>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script>
import router from "@/router";

export default {
  name:'myHeader',
  data () {
    return {
      navList: [
        {name: '/home',navItem: 'LCLONG-WEB'},
        {name: '/blogs', navItem: '博客列表'},
        {name: '/product', navItem: '物品列表'},
        {name: '/material', navItem: '素材列表'},
        {name: '/notice', navItem: '网站公告'},
        {name: '/description', navItem: '网站说明'}

      ],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}

    }
  },
  computed: {
    hoverBackground () {
      return '#ffd04b'
    },
    currentPath () {
      var x = this.$route.path.indexOf('/', 1)
      if (x !== -1) {
        return this.$route.path.substring(0, x)
      } else {
        return this.$route.path
      }
    },
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
a{
  text-decoration: none;
}

span {
  pointer-events: none;
}
</style>

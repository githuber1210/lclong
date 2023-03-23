<template>
  <el-container style="min-height: 100vh">

    <el-aside style="width: 200px">
      <Aside style="overflow: hidden;position: fixed;width: 200px" />
    </el-aside>

    <el-container>
      <el-header style="padding: 0">
        <Header :user="user" />
      </el-header>

      <el-main style="background-color: #fdfcfc">
        <router-view @refreshUser="getUser" />
      </el-main>

    </el-container>
  </el-container>
</template>

<script>

import Aside from "@/views/management/Layout/Aside.vue";
import Header from "@/views/management/Layout/Header.vue";

export default {
  name: 'Home',
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : ""
    }
  },
  components: {
    Aside,
    Header
  },

  methods: {
    getUser() {
      let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : ""
      this.user = user;
      if (username) {
        this.request.get("/user/username/" + username).then(res => {
          this.user = res.data
        })
      }
    }
  }
}
</script>


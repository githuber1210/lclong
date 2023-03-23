<template>
  <el-container>
    <el-header style="height: 50px">
      <MyHeader>
      </MyHeader>
    </el-header>
    <el-main>
      <router-view/>
    </el-main>
    <el-footer>

      <div style="text-align: center">
        <img style="width: 20px; "
             src="http://www.npc.gov.cn/npc/kgfb/202101/d8c025c904b94636a8e1799b652f86be/images/bd02b85b8ad745b8aeaea2731daa91b7.png" >
        <a target="_blank" style="color: #5c6b77;text-decoration: none;" href="https://beian.miit.gov.cn/">
          闽ICP备2023002723号
        </a>
        <p>
          &copy copyright @2023 lclong
        </p>
      </div>

    </el-footer>
  </el-container>
</template>

<script>
import MyHeader from "@/views/home/container/myHeader.vue";
export default {
  data(){
    return{
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  components: {
    MyHeader
  }
}
</script>

<style scoped>

</style>
<template>
  <div class="container">
    <el-card  shadow="always" class="card">
      <div style="display: flex">
          <img src="../../assets/image/picture.png" style="width: 400px">
        <div>
          <div class="title"><b>LCLONG-ADMIN</b></div>
          <div style="margin: 50px;">
            <el-form :model="user" :rules="rules" ref="userForm">
              <el-form-item prop="username" >
                <el-input prefix-icon="el-icon-user" v-model="user.username" placeholder="用户名"></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input prefix-icon="el-icon-lock" v-model="user.password" placeholder="请输入密码" show-password ></el-input>
              </el-form-item>
<!--              <el-form-item>-->
<!--                <el-checkbox v-model="remember">记住我</el-checkbox>-->
<!--                <span @click="" style="margin-left: 100px;color: #8c939d">忘记密码?</span>-->
<!--              </el-form-item>-->
              <el-form-item >
                <el-button type="primary" @click="login" style="width:40%;border-radius:15px">登录</el-button>
                <el-button  style="border-radius: 15px;margin-left: 20px">
                  <router-link to="/register" style="text-decoration: none">创建账户</router-link>
                </el-button>
              </el-form-item>
              <span style="font-size:2px;"> 或者您也可以通过以下方式登录 </span>
              <el-form-item style="margin-top: 10px">
                <el-button type="info" icon="el-icon-message" @click="$router.push('mailLogin')" circle></el-button>
                <el-button type="info" icon="el-icon-mobile-phone" @click="$router.push('telLogin')" circle></el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </el-card>

  </div>
</template>

<script>
import {setRoutes} from "@/router";

export default {
  name: "Login",
  data() {
    return {
      user: {},
      remember:'',
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          this.request.post("/login", this.user).then(res => {
            if (res.code === 200) {
              const token = res.data.token;
              localStorage.setItem("token",JSON.stringify(token))
              this.getUserInfo()
            } else {
              this.$message.error(res.msg)
            }

          })
        }
      });
    },
    getUserInfo(){
      this.request.get("/userInfo").then(res => {
        if (res.code === 200) {
          localStorage.setItem("user",JSON.stringify(res.data.user))
          localStorage.setItem("menus",JSON.stringify(res.data.menus))
          setRoutes()
          this.$message.success("登入系统成功!")
          this.$router.push("/")
        } else {
          this.$message.error(res.msg)
        }

      })
    }
  }
}
</script>

<style>
.container {
  height: 100vh;
  overflow: hidden;
  /*noinspection CssUnknownTarget*/
  background-image: url("../../assets/image/bg.jpg");
  background-repeat: no-repeat;
  background-size: 100% 80%;
}

.card {
  margin: 135px auto;
  background-color: #fff;
  width: 800px;
  height: 400px;
  padding: 20px;
  border-radius: 10px;
  display: flex;
}

.title {
  text-align: center;
  font-size: 24px
}
</style>

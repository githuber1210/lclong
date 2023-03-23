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
                <el-input prefix-icon="el-icon-user" v-model="user.username" placeholder="请输入用户名"></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input prefix-icon="el-icon-lock" v-model="user.password" placeholder="请输入密码" show-password ></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input prefix-icon="el-icon-lock" v-model="user.confirm" placeholder="请再次输入密码" show-password ></el-input>
              </el-form-item>
              <el-form-item prop="telephone">
                <el-input prefix-icon="el-icon-phone" v-model="user.telephone" placeholder="请输入手机号" ></el-input>
              </el-form-item>
              <el-form-item prop="code">
                <el-input  v-model="user.code" placeholder="验证码" style="width: 150px;margin-right: 30px;"  show-word-limit maxlength="6"></el-input>
                <span @click="handleSendCode" style="cursor: pointer">获取验证码</span>
              </el-form-item>
              <el-form-item style="text-align: center">
                <el-button type="primary" @click="register" style="width:40%;border-radius:15px">
                  注册
                </el-button>
              </el-form-item>

            </el-form>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import router, {setRoutes} from "@/router";

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
        confirm: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
      }
    }
  },
  methods: {
    router() {
      return router
    },
    register() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if (this.user.password !== this.user.confirm) {
            this.$message.error("两次输入的密码不同!")
            return false
          }
          this.request.post("/register", this.user).then(res => {
            if (res.code === 200) {
              this.$message.success("注册成功")
              this.$router.push("/login")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    },
    handleSendCode(){
      this.$message.success("发送成功！");
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

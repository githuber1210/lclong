<template>
  <div class="container">
    <el-card shadow="always" class="card">
      <div style="display: flex">
        <img src="../../assets/image/picture.png" style="width: 400px" />
        <div>
          <div class="title"><b>LCLONG-ADMIN</b></div>
          <div style="margin: 50px;">
            <el-form :model="user" :rules="rules" ref="userForm">
              <el-form-item prop="email">
                <el-input
                    prefix-icon="el-icon-user"
                    v-model="user.email"
                    placeholder="请输入邮箱"
                ></el-input>
              </el-form-item>
              <div style="display: flex">
                <el-form-item prop="verificationCode" style="width: 110px;">
                  <el-input
                      prefix-icon="el-icon-lock"
                      v-model="user.verificationCode"
                      placeholder="验证码"
                  >
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-button
                      v-if="!countdown"
                      @click="sendVerificationCode"
                      style="width: 100%; border-radius: 15px;margin-left: 20px"
                  >
                    发送验证码
                  </el-button>
                  <el-button
                      v-else
                      type="info"
                      disabled
                      style="width: 100%; border-radius: 15px;margin-left: 20px"
                  >
                    {{ countdown }}秒后再获取
                  </el-button>
                </el-form-item>
              </div>
<!--              <el-form-item>-->
<!--                <el-checkbox v-model="remember">记住我</el-checkbox>-->
<!--                <span @click="" style="margin-left: 100px;color: #8c939d">忘记密码?</span>-->
<!--              </el-form-item>-->
              <el-form-item>
                <el-button
                    type="primary"
                    @click="login"
                    style="width: 40%; border-radius: 15px"
                >
                  登录
                </el-button>
                <el-button style="border-radius: 15px; margin-left: 20px">
                  <router-link
                      to="/register"
                      style="text-decoration: none"
                  >
                    创建账户
                  </router-link>
                </el-button>
              </el-form-item>
              <span style="font-size: 2px;">或者您也可以通过以下方式登录</span>
              <el-form-item style="margin-top: 10px">
                <el-button
                    type="info"
                    icon="el-icon-lock"
                    @click="$router.push('login')"
                    circle
                ></el-button>
                <el-button
                    type="info"
                    icon="el-icon-mobile-phone"
                    @click="$router.push('telLogin')"
                    circle
                ></el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { setRoutes } from "@/router";

export default {
  name: "Login",
  data() {
    return {
      user: {},
      remember: "",
      countdown: 0,
      rules: {
        email: [
          {required: true, message: "请输入邮箱", trigger: "blur"},
        ],
        verificationCode: [
          {required: true, message: "请输入验证码", trigger: "blur"},
        ],
      },
    }
  },
  methods: {
    sendVerificationCode() {
      if (!this.user.email) {
        this.$message.warning("请先输入邮箱！");
        return;
      }
      // 发送验证码请求
      this.request.get("/getVerifyCodesByEmail", {params:{
          email : this.user.email
        }
      }).then(res => {
        if (res.code === 200) {
          this.$message.success("验证码已发送，请注意查收！");
          this.countdown = 60; // 重新设置倒计时为60秒
          // 开始倒计时
          this.timer = setInterval(() => {
            if (this.countdown > 0) {
              this.countdown--;
            } else {
              clearInterval(this.timer);
            }
          }, 1000);
        } else {
          this.$message.error(res.msg);
        }
      })
    },
    login() {
      // 验证表单
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          // 发送登录请求
          this.request.get("/VerifyCodesByEmail", {params:{
              email: this.user.email,
              code: this.user.verificationCode
            }
          }).then(res => {
            if (res.code === 200) {
              const token = res.data.token;
              localStorage.setItem("token", JSON.stringify(token));
              this.getUserInfo();
            } else {
              this.$message.error(res.msg);
            }
          });
        }
      });
    },
    getUserInfo() {
      this.request.get("/userInfo").then(res => {
        if (res.code === 200) {
          localStorage.setItem("user", JSON.stringify(res.data.user))
          localStorage.setItem("menus", JSON.stringify(res.data.menus))
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

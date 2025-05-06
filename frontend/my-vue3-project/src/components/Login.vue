<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>{{ isRegistering ? "用户注册" : "用户登录" }}</h2>
        </div>
      </template>

      <el-form
        ref="loginForm"
        :model="loginForm"
        :rules="loginRules"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">{{
            isRegistering ? "注册" : "登录"
          }}</el-button>
          <el-button @click="toggleRegister">{{
            isRegistering ? "已有账号？登录" : "没有账号？注册"
          }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import axios from "axios";
import { ElMessage } from "element-plus";

export default {
  data() {
    return {
      loginForm: {
        username: "",
        password: "",
      },
      loginRules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
      loading: false,
      isRegistering: false,
    };
  },
  methods: {
    handleSubmit() {
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          this.loading = true;
          try {
            const apiEndpoint = this.isRegistering
              ? "/api/Signup"
              : "/api/login";
            const response = await axios.post(apiEndpoint, this.loginForm, {
              headers: {
                "Content-Type": "application/json",
              },
            });

            if (response.status === 200) {
              if (this.isRegistering) {
                ElMessage.success(
                  `注册成功！欢迎您，${this.loginForm.username}！`
                );
                this.toggleRegister(); // 注册成功后切换到登录界面
              } else {
                // 保存用户名到本地存储
                localStorage.setItem("username", this.loginForm.username);

                // 登录成功处理
                if (this.loginForm.username === "Admin") {
                  this.$router.push({ name: "AdminPage" });
                } else if (/^DOC\d+$/.test(this.loginForm.username)) {
                  this.$router.push({ name: "PatientManage" });
                } else if (/^\d+$/.test(this.loginForm.username)) {
                  this.$router.push({
                    name: "PatientPage",
                    params: { id: this.loginForm.username },
                  });
                } else {
                  ElMessage.info("登录成功，正在跳转...");
                  // 可以添加一个默认跳转或者根据后端返回的角色信息跳转
                  this.$router.push({ path: "/" }); // 默认跳转到首页
                }
              }
            }
          } catch (error) {
            let errorMessage = "操作失败，请稍后重试";
            if (error.response) {
              if (error.response.status === 401) {
                errorMessage = this.isRegistering
                  ? "用户名已存在或格式不正确"
                  : "用户名或密码错误";
              } else {
                errorMessage =
                  error.response.data?.message ||
                  `服务器错误 (${error.response.status})`;
              }
            } else if (error.request) {
              errorMessage = "网络错误，请检查连接";
            }
            ElMessage.error(errorMessage);
            console.error(
              this.isRegistering ? "注册错误:" : "登录错误:",
              error
            );
          } finally {
            this.loading = false;
          }
        } else {
          ElMessage.error("请填写完整的登录信息！");
          return false;
        }
      });
    },
    toggleRegister() {
      this.isRegistering = !this.isRegistering;
      this.resetForm();
    },
    resetForm() {
      this.$refs.loginForm.resetFields();
    },
  },
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f2f6fc; /* Light background color, typical for admin interfaces */
}

.login-card {
  width: 400px;
  max-width: 90%;
}

.card-header {
  display: flex;
  justify-content: center;
  align-items: center;
  padding-bottom: 20px;
}

.el-form {
  padding: 20px;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-button {
  margin-right: 10px;
}
</style>

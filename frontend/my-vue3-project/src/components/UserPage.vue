<template>
  <div class="user-page">
    <div class="background"></div>
    <!-- 背景层 -->
    <div class="user-content">
      <el-row justify="center" align="middle" class="header">
        <h2>欢迎来到患者管理平台</h2>
      </el-row>
      <el-row class="button-group">
        <el-button type="primary" @click="logout">退出登录</el-button>
        <el-link
          type="primary"
          :underline="false"
          @click="$router.push('/patient-manage')"
          >进入患者管理</el-link
        >
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const logout = () => {
  router.push({ name: "login" });
};
</script>

<style scoped>
.user-page {
  position: relative;
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(
    rgba(173, 216, 230, 0.8),
    rgba(135, 206, 235, 0.6)
  ); /* 医疗风格渐变背景 */
}

.background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url("@/assets/fig1.png") no-repeat center center;
  background-size: cover;
  filter: blur(5px); /* 模糊背景图片 */
  opacity: 0.8;
  z-index: 1;
}

.user-content {
  position: relative;
  z-index: 2;
  padding: 40px;
  border-radius: 15px;
  background: rgba(255, 255, 255, 0.9); /* 半透明白色背景 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  text-align: center;
}

.header h2 {
  color: #1c4b6d; /* 深蓝色标题 */
  margin-bottom: 30px;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
}

/* 直接为 Element Plus 组件设置样式 */
.el-button {
  background-color: #1c4b6d;
  color: white;
  border-radius: 25px;
  padding: 12px 30px;
  font-size: 16px;
  border: none;
}

.el-button:hover {
  background-color: #1a3e5e;
}

.el-link {
  color: #1c4b6d;
  font-size: 16px;
  font-weight: 500;
}

.el-link:hover {
  color: #1a3e5e;
}
</style>

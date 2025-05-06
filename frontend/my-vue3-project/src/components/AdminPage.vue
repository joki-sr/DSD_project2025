<template>
  <div class="admin-container">
    <el-card class="admin-card" shadow="always">
      <template #header>
        <div class="card-header">
          <h2>医生与患者关系管理</h2>
        </div>
      </template>

      <el-divider content-position="left">注册医生</el-divider>
      <el-form :inline="true" :model="registerForm" class="form">
        <el-form-item label="用户名">
          <el-input v-model="registerForm.username" placeholder="医生用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="医生密码"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            :loading="registerLoading"
            @click="registerDoctor"
            >注册</el-button
          >
        </el-form-item>
      </el-form>

      <el-divider content-position="left">添加医生-患者关系</el-divider>
      <el-form :inline="true" :model="relationForm" class="form">
        <el-form-item label="医生姓名">
          <el-input v-model="relationForm.doctor" placeholder="医生姓名" />
        </el-form-item>
        <el-form-item label="患者姓名">
          <el-input v-model="relationForm.patient" placeholder="患者姓名" />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            :loading="addRelationLoading"
            @click="addRelation"
            >添加</el-button
          >
        </el-form-item>
      </el-form>

      <el-divider content-position="left">医生-患者关系列表</el-divider>
      <el-table
        :data="relations"
        style="width: 100%"
        :loading="relationsLoading"
        key="relationsTable"
      >
        <el-table-column prop="doctor" label="医生">
          <template #default="scope">
            <el-input v-model="scope.row.doctor" placeholder="医生姓名" />
          </template>
        </el-table-column>
        <el-table-column prop="patient" label="患者">
          <template #default="scope">
            <el-input v-model="scope.row.patient" placeholder="患者姓名" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button
              size="small"
              type="primary"
              :loading="updateLoading[scope.$index]"
              @click="updateRelation(scope.$index, scope.row)"
              >保存</el-button
            >
            <el-button
              size="small"
              type="danger"
              :loading="deleteLoading[scope.$index]"
              @click="deleteRelation(scope.$index)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <el-divider content-position="left" style="margin-top: 30px"
        >已注册医生账户</el-divider
      >
      <el-table
        :data="doctors"
        style="width: 100%"
        :loading="doctorsLoading"
        key="doctorsTable"
      >
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="name" label="医生" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import axios from "axios";
import { ElMessage } from "element-plus";

export default {
  name: "AdminPage",
  data() {
    return {
      relations: [],
      doctors: [],
      relationForm: { doctor: "", patient: "" },
      registerForm: { username: "", password: "" },
      registerLoading: false,
      addRelationLoading: false,
      relationsLoading: false,
      doctorsLoading: false,
      updateLoading: {},
      deleteLoading: {},
    };
  },
  methods: {
    async addRelation() {
      if (!this.relationForm.doctor || !this.relationForm.patient) {
        ElMessage.warning("医生和患者姓名都不能为空！");
        return;
      }
      this.addRelationLoading = true;
      console.log("开始添加医生-患者关系:", this.relationForm);
      try {
        const response = await axios.post(
          "/api/admin/relations",
          this.relationForm
        );
        console.log("添加医生-患者关系响应:", response);
        if (response.data && response.data.status === 201) {
          ElMessage.success("关系添加成功！");
          await this.fetchRelations(); // 确保关系列表重新加载
          this.relationForm.doctor = "";
          this.relationForm.patient = "";
        } else {
          ElMessage.error("添加关系失败！");
        }
      } catch (error) {
        console.error("添加关系失败:", error);
        ElMessage.error("添加关系失败，请检查网络或服务器！");
      } finally {
        this.addRelationLoading = false;
        console.log("添加医生-患者关系完成");
      }
    },
    async registerDoctor() {
      if (!this.registerForm.username || !this.registerForm.password) {
        ElMessage.warning("用户名和密码都不能为空！");
        return;
      }
      this.registerLoading = true;
      console.log("开始注册医生:", this.registerForm);
      try {
        const response = await axios.post("/api/admin/doctors", {
          username: this.registerForm.username,
        }); // 只发送 username
        console.log("注册医生响应:", response);
        if (response.data && response.data.status === 201) {
          ElMessage.success("医生注册成功！");
          await this.fetchDoctors();
          this.registerForm.username = "";
          this.registerForm.password = "";
        } else if (response.data && response.data.status === 409) {
          ElMessage.warning(response.data.message || "用户名已存在！");
        } else {
          ElMessage.error("医生注册失败！");
        }
      } catch (error) {
        console.error("医生注册失败:", error);
        ElMessage.error("医生注册失败，请检查网络或服务器！");
      } finally {
        this.registerLoading = false;
        console.log("医生注册完成");
      }
    },
    async deleteRelation(index) {
      const relationToDelete = this.relations[index];
      this.$confirm(
        `确定要删除医生 "${relationToDelete.doctor}" 和患者 "${relationToDelete.patient}" 的关系吗?`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(async () => {
          // 修改了这里，直接使用对象展开运算符更新 loading 状态
          this.deleteLoading = { ...this.deleteLoading, [index]: true };
          console.log("开始删除医生-患者关系，ID:", relationToDelete.id);
          try {
            const response = await axios.delete(
              `/api/admin/relations/${relationToDelete.id}`
            );
            console.log("删除医生-患者关系响应:", response);
            if (response.status === 204) {
              this.relations.splice(index, 1);
              ElMessage.success("删除成功！");
            } else {
              ElMessage.error("删除失败！");
            }
          } catch (error) {
            console.error("删除关系失败:", error);
            ElMessage.error("删除失败，请检查网络或服务器！");
          } finally {
            // 修改了这里，创建新的 loading 对象来触发视图更新
            const newDeleteLoading = { ...this.deleteLoading };
            delete newDeleteLoading[index];
            this.deleteLoading = newDeleteLoading;
            console.log("删除医生-患者关系完成");
          }
        })
        .catch(() => {
          console.log("取消删除");
        });
    },
    async updateRelation(index, row) {
      if (!row.doctor || !row.patient) {
        ElMessage.warning("医生和患者姓名都不能为空！");
        return;
      }
      // 修改了这里，直接使用对象展开运算符更新 loading 状态
      this.updateLoading = { ...this.updateLoading, [index]: true };
      console.log("开始更新医生-患者关系，ID:", row.id, "数据:", row);
      try {
        const response = await axios.put(`/api/admin/relations/${row.id}`, row);
        console.log("更新医生-患者关系响应:", response);
        if (response.data && response.data.status === 200) {
          // 直接修改 relations 数组中的对象
          this.relations.splice(index, 1, { ...this.relations[index], ...row });
          ElMessage.success("修改成功！");
        } else {
          ElMessage.error("修改失败！");
        }
      } catch (error) {
        console.error("修改关系失败:", error);
        ElMessage.error("修改失败，请检查网络或服务器！");
      } finally {
        // 修改了这里，创建新的 loading 对象来触发视图更新
        const newUpdateLoading = { ...this.updateLoading };
        delete newUpdateLoading[index];
        this.updateLoading = newUpdateLoading;
        console.log("更新医生-患者关系完成");
      }
    },
    async fetchRelations() {
      this.relationsLoading = true;
      console.log("开始加载医生-患者关系");
      try {
        const response = await axios.get("/api/admin/relations");
        console.log("加载医生-患者关系响应:", response);
        if (response.data && response.data.status === 200) {
          this.relations = response.data.data;
          console.log("加载的医生-患者关系数据:", this.relations);
        } else {
          ElMessage.error("加载医生-患者关系失败！");
        }
      } catch (error) {
        console.error("加载医生-患者关系失败:", error);
        ElMessage.error("加载医生-患者关系失败，请检查网络或服务器！");
      } finally {
        this.relationsLoading = false;
        console.log("加载医生-患者关系完成");
      }
    },
    async fetchDoctors() {
      this.doctorsLoading = true;
      console.log("开始加载医生账户");
      try {
        const response = await axios.get("/api/admin/doctors");
        console.log("加载医生账户响应:", response);
        if (response.data && response.data.status === 200) {
          this.doctors = response.data.data;
          console.log("加载的医生账户数据:", this.doctors);
        } else {
          ElMessage.error("加载医生账户失败！");
        }
      } catch (error) {
        console.error("加载医生账户失败:", error);
        ElMessage.error("加载医生账户失败，请检查网络或服务器！");
      } finally {
        this.doctorsLoading = false;
        console.log("加载医生账户完成");
      }
    },
  },
  async mounted() {
    console.log("组件 AdminPage mounted，开始加载数据");
    this.relationsLoading = true;
    this.doctorsLoading = true;
    try {
      await Promise.all([this.fetchRelations(), this.fetchDoctors()]);
    } finally {
      this.relationsLoading = false;
      this.doctorsLoading = false;
    }
  },
};
</script>

<style scoped>
.admin-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f2f6fc;
}

.admin-card {
  width: 800px;
  max-width: 90%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.form {
  margin-bottom: 20px;
}

.el-divider {
  margin: 20px 0;
}
</style>

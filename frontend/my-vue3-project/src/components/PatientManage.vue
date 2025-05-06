<template>
  <div class="patient-manage">
    <el-container class="content-container">
      <el-header class="header">
        <h2>患者管理</h2>
        <el-button type="primary" @click="goBack">返回用户界面</el-button>
      </el-header>

      <el-main class="main-content">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-input
              v-model="searchQuery"
              placeholder="搜索患者姓名..."
              prefix-icon="Search"
              class="search-input"
            />
          </el-col>
        </el-row>

        <el-row :gutter="20" class="patient-list">
          <el-col
            v-for="patient in filteredPatients"
            :key="patient.id"
            :xs="24"
            :sm="12"
            :md="8"
            :lg="6"
          >
            <el-card class="patient-card">
              <div class="patient-header">
                <h3>{{ patient.name }}</h3>
                <div class="button-group">
                  <el-button type="text" @click="toggleDetails(patient.id)">
                    {{
                      expandedPatients.includes(patient.id) ? "收起" : "详情"
                    }}
                  </el-button>
                  <el-button type="text" @click="measureDataAction(patient.id)">
                    测量数据
                  </el-button>
                </div>
              </div>

              <div v-if="expandedPatients.includes(patient.id)">
                <el-collapse v-model="activePanel">
                  <el-collapse-item name="measurement">
                    <template #title>
                      <span class="collapse-title">测量数据</span>
                    </template>
                    <el-button @click="showMeasurementData(patient.id)"
                      >查看3D数据</el-button
                    >
                  </el-collapse-item>

                  <el-collapse-item name="history">
                    <template #title>
                      <span class="collapse-title">历史数据</span>
                    </template>
                    <el-button @click="showHistoryData(patient.id)"
                      >查看历史记录</el-button
                    >
                  </el-collapse-item>
                </el-collapse>

                <div v-if="currentPatientId === patient.id">
                  <div v-if="show3DData">
                    <h4>3D测量数据</h4>
                    <el-image
                      style="width: 100%"
                      src="@/assets/3d-measurement.gif"
                      fit="contain"
                    />
                  </div>

                  <div v-if="historyData.length">
                    <h4>历史记录</h4>
                    <el-timeline>
                      <el-timeline-item
                        v-for="(record, index) in historyData"
                        :key="index"
                        :timestamp="record.date"
                        placement="top"
                      >
                        <el-card>
                          <h5>{{ record.type }} | {{ record.summary }}</h5>
                          <el-table
                            v-if="expandedReports.includes(index)"
                            :data="[record.data]"
                            border
                          >
                            <el-table-column prop="运动幅度" label="运动幅度" />
                            <el-table-column prop="得分" label="得分" />
                          </el-table>
                        </el-card>
                      </el-timeline-item>
                    </el-timeline>
                  </div>

                  <el-progress
                    v-if="progress > 0 && progress < 100"
                    :percentage="progress"
                    :text-inside="true"
                    :stroke-width="24"
                  />
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import axios from "axios";
import Papa from "papaparse";
import { useRouter, useRoute } from "vue-router";
import { ElMessage } from "element-plus";

// 路由和数据初始化
const router = useRouter();
const route = useRoute();

// 响应式数据
const searchQuery = ref("");
const patients = ref([]);
const expandedPatients = ref([]);
const show3DData = ref(false);
const historyData = ref([]);
const currentPatientId = ref(null);
const progress = ref(0);
const allReports = ref([]);
const expandedReports = ref([]);
//const gaitColumns = ['L1', 'L2', 'L3', 'L4', 'L5', 'L6', 'R1', 'R2', 'R3', 'R4', 'R5', 'R6'];
const activePanel = ref([]);

// 计算属性
const filteredPatients = computed(() => {
  if (!Array.isArray(patients.value)) {
    console.error("patients.value 不是数组:", patients.value);
    return [];
  }
  return patients.value.filter((patient) =>
    patient.name.includes(searchQuery.value)
  );
});

// 生命周期钩子
onMounted(() => {
  fetchPatients();
  fetchCSVData();
  handleMeasureResult();
});

// 方法定义
async function fetchPatients() {
  try {
    // 从登录的用户信息中获取用户名
    const username =
      localStorage.getItem("username") || sessionStorage.getItem("username");

    // 带上用户名参数请求API
    const res = await axios.get("/api/patients", {
      params: { doctorUsername: username },
    });

    if (res.data.code === 200) {
      patients.value = Array.isArray(res.data.data) ? res.data.data : [];
    } else {
      ElMessage.error("获取患者列表失败");
    }
  } catch (err) {
    ElMessage.error("获取患者列表失败");
  }
}

function handleMeasureResult() {
  const { measureResult, patientId } = route.query;
  if (measureResult && patientId) {
    const result = JSON.parse(measureResult);
    const patient = patients.value.find((p) => p.id === parseInt(patientId));
    if (patient) {
      patient.history = patient.history || [];
      patient.history.unshift({
        date: new Date().toLocaleDateString(),
        data: result,
      });
    }
  }
}

function goBack() {
  router.push({ name: "user" });
}

function toggleDetails(patientId) {
  const index = expandedPatients.value.indexOf(patientId);
  if (index > -1) {
    expandedPatients.value.splice(index, 1);
  } else {
    expandedPatients.value.push(patientId);
  }
  resetView();
}

function resetView() {
  show3DData.value = false;
  historyData.value = [];
  currentPatientId.value = null;
  activePanel.value = [];
}

function showMeasurementData(patientId) {
  currentPatientId.value = patientId;
  router.push({
    name: "Patient3DView",
    params: { id: patientId },
  });
}

function showHistoryData(patientId) {
  router.push({
    name: "PatientPage", // 跳转到 PatientPage
    params: { id: patientId }, // 通过动态路由参数传递患者 ID
  });
}

function measureDataAction(patientId) {
  router.push({
    name: "BlueTooth",
    query: { patientId },
  });
}

async function fetchCSVData() {
  try {
    console.log("开始获取CSV数据...");
    const res = await axios.get("/api/sentiment_data.csv", {
      headers: {
        Accept: "text/csv",
      },
      // 确保返回文本而不是JSON
      responseType: "text",
    });

    console.log("CSV数据获取成功，内容长度:", res.data.length);
    console.log("CSV数据内容预览:", res.data.substring(0, 100));

    if (!res.data || res.data.trim() === "") {
      console.warn("获取到的CSV数据为空");
      ElMessage.warning("CSV数据为空");
      return;
    }

    Papa.parse(res.data, {
      header: true,
      skipEmptyLines: true,
      complete: (result) => {
        console.log("CSV解析成功，行数:", result.data.length);
        console.log("CSV表头:", result.meta.fields);

        if (result.data && result.data.length > 0) {
          allReports.value = result.data.map((item, index) => {
            // 调试输出
            console.log(`处理第${index + 1}行CSV数据:`, item);

            let motionRange = [];
            let score = [];

            try {
              // 尝试解析运动幅度和得分字段
              // 处理可能的引号包裹和格式问题
              if (item.运动幅度) {
                const cleanedMotion = item.运动幅度.replace(/^"|"$/g, "");
                motionRange = JSON.parse(cleanedMotion);
                console.log(
                  `成功解析运动幅度数据: ${motionRange.length}个数据点`
                );
              }

              if (item.得分) {
                const cleanedScore = item.得分.replace(/^"|"$/g, "");
                score = JSON.parse(cleanedScore);
                console.log(`成功解析得分数据: ${score.length}个数据点`);
              }
            } catch (parseError) {
              console.error(`第${index + 1}行JSON解析错误:`, parseError);
              console.error(`原始运动幅度值:`, item.运动幅度);
              console.error(`原始得分值:`, item.得分);

              // 尝试使用替代方法解析
              try {
                if (item.运动幅度 && typeof item.运动幅度 === "string") {
                  // 尝试提取方括号中的内容并分割
                  const match = item.运动幅度.match(/\[(.*)\]/);
                  if (match && match[1]) {
                    motionRange = match[1]
                      .split(",")
                      .map((num) => parseInt(num.trim(), 10));
                    console.log("使用替代方法解析运动幅度成功");
                  }
                }

                if (item.得分 && typeof item.得分 === "string") {
                  const match = item.得分.match(/\[(.*)\]/);
                  if (match && match[1]) {
                    score = match[1]
                      .split(",")
                      .map((num) => parseInt(num.trim(), 10));
                    console.log("使用替代方法解析得分成功");
                  }
                }
              } catch (altError) {
                console.error("替代解析方法也失败:", altError);
              }
            }

            return {
              date: item.date || "",
              type: item.type || "",
              summary: item.summary || "",
              data: {
                运动幅度: Array.isArray(motionRange) ? motionRange : [],
                得分: Array.isArray(score) ? score : [],
              },
            };
          });

          console.log("报告数据处理完成:", allReports.value.length, "条记录");

          // 验证处理后的数据
          if (allReports.value.length > 0) {
            console.log("第一条记录示例:", {
              date: allReports.value[0].date,
              type: allReports.value[0].type,
              summary: allReports.value[0].summary,
              运动幅度Count: allReports.value[0].data.运动幅度.length,
              得分Count: allReports.value[0].data.得分.length,
            });
          }
        } else {
          console.warn("CSV解析结果为空或无效");
          ElMessage.warning("CSV数据为空或格式不正确");
        }
      },
      error: (error) => {
        console.error("CSV解析错误:", error);
        ElMessage.error("CSV解析失败: " + error.message);
      },
    });
  } catch (err) {
    console.error("CSV加载失败:", err);
    if (err.response) {
      console.error("响应状态:", err.response.status);
      console.error("响应数据:", err.response.data);
    }
    ElMessage.error("CSV加载失败: " + (err.message || "未知错误"));
  }
}
</script>

<style scoped>
.patient-manage {
  height: 100vh;
  background: linear-gradient(
    rgba(173, 216, 230, 0.8),
    rgba(135, 206, 235, 0.6)
  );
}

.content-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0;
  margin-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.patient-list {
  margin-top: 20px;
}

.patient-card {
  border-radius: 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}
.patient-card:hover {
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.patient-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button-group {
  display: flex;
  gap: 10px;
}

.collapse-title {
  font-weight: bold;
  color: #409eff;
}

.search-input {
  margin-bottom: 20px;
}

.el-timeline {
  margin-top: 20px;
}

.el-timeline-item__timestamp {
  color: #909399;
}
</style>

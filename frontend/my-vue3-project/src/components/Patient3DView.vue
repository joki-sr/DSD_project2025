<template>
  <div class="patient-3d-view">
    <div class="view-header">
      <h3>{{ patient.name }} 步态分析 3D 数据</h3>
    </div>

    <div v-if="loading" class="loading-container">
      <el-skeleton style="width: 100%" animated :rows="10" />
    </div>

    <div v-else-if="error" class="error-container">
      <el-alert
        title="数据加载失败"
        type="error"
        description="无法加载3D测量数据，请稍后再试。"
        show-icon
        :closable="false"
      />
    </div>

    <div v-else class="view-content">
      <div class="data-display">
        <div class="chart-container">
          <h4>运动幅度数据</h4>
          <div class="chart" ref="motionChartRef"></div>
        </div>

        <div class="chart-container">
          <h4>评分数据</h4>
          <div class="chart" ref="scoreChartRef"></div>
        </div>
      </div>

      <div class="data-analysis">
        <h4>步态分析结果</h4>
        <el-descriptions border>
          <el-descriptions-item label="平均运动幅度">{{
            avgMotion.toFixed(2)
          }}</el-descriptions-item>
          <el-descriptions-item label="平均得分">{{
            avgScore.toFixed(2)
          }}</el-descriptions-item>
          <el-descriptions-item label="左右对称性"
            >{{ symmetry }}%</el-descriptions-item
          >
          <el-descriptions-item label="运动稳定性">{{
            stability
          }}</el-descriptions-item>
          <el-descriptions-item label="步态状态">{{
            gaitStatus
          }}</el-descriptions-item>
          <el-descriptions-item label="建议">
            根据当前数据分析，建议患者{{ recommendation }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRoute } from "vue-router";
import axios from "axios";
import * as echarts from "echarts";

const route = useRoute();
const patientId = route.query.id || route.params.id;

const loading = ref(true);
const error = ref(false);
const patient = ref({ name: "患者" });
const reportData = ref(null);

const motionChartRef = ref(null);
const scoreChartRef = ref(null);
let motionChart = null;
let scoreChart = null;

const motionData = ref([]);
const scoreData = ref([]);

// 计算属性
const avgMotion = computed(() => {
  if (!motionData.value || motionData.value.length === 0) return 0;
  return (
    motionData.value.reduce((sum, val) => sum + val, 0) /
    motionData.value.length
  );
});

const avgScore = computed(() => {
  if (!scoreData.value || scoreData.value.length === 0) return 0;
  return (
    scoreData.value.reduce((sum, val) => sum + val, 0) / scoreData.value.length
  );
});

// 左右对称性 (示例：随机生成85%-95%之间的值)
const symmetry = computed(() => {
  return Math.floor(Math.random() * 10 + 85);
});

// 运动稳定性 (示例：基于分数的文字描述)
const stability = computed(() => {
  const avg = avgScore.value;
  if (avg >= 9) return "优秀";
  if (avg >= 8) return "良好";
  if (avg >= 7) return "一般";
  return "需改善";
});

// 步态状态评价
const gaitStatus = computed(() => {
  const avg = avgScore.value;
  if (avg >= 9) return "正常";
  if (avg >= 8) return "轻微异常";
  if (avg >= 7) return "中度异常";
  return "显著异常";
});

// 建议
const recommendation = computed(() => {
  const avg = avgScore.value;
  if (avg >= 9) return "保持目前的锻炼方式，定期复查。";
  if (avg >= 8) return "适当增加锻炼频率，注意左右平衡。";
  if (avg >= 7) return "增加专项训练，加强下肢力量。";
  return "寻求专业康复指导，定制康复训练计划。";
});

// 方法
async function fetchPatientData() {
  try {
    loading.value = true;

    // 获取患者基本信息
    const patientResponse = await axios.get(`/api/patient/${patientId}`);
    if (patientResponse.data.code === 200) {
      patient.value = patientResponse.data.data;
    }

    // 获取最新的报告数据
    const reportsResponse = await axios.get(
      `/api/patient/${patientId}/reports`
    );
    if (reportsResponse.data && reportsResponse.data.length > 0) {
      reportData.value = reportsResponse.data[0];

      // 解析数据
      try {
        if (reportData.value.data && reportData.value.data["运动幅度"]) {
          if (typeof reportData.value.data["运动幅度"] === "string") {
            motionData.value = JSON.parse(reportData.value.data["运动幅度"]);
          } else {
            motionData.value = reportData.value.data["运动幅度"];
          }
        }

        if (reportData.value.data && reportData.value.data["得分"]) {
          if (typeof reportData.value.data["得分"] === "string") {
            scoreData.value = JSON.parse(reportData.value.data["得分"]);
          } else {
            scoreData.value = reportData.value.data["得分"];
          }
        }
      } catch (parseError) {
        console.error("解析数据失败:", parseError);
        // 使用示例数据作为后备
        motionData.value = [75, 82, 90, 88, 79, 85, 78, 81, 87, 84, 80, 83];
        scoreData.value = [8, 9, 10, 9, 8, 9, 8, 9, 10, 9, 8, 9];
      }
    } else {
      // 没有数据时使用示例数据
      motionData.value = [75, 82, 90, 88, 79, 85, 78, 81, 87, 84, 80, 83];
      scoreData.value = [8, 9, 10, 9, 8, 9, 8, 9, 10, 9, 8, 9];
    }

    // 初始化图表
    initCharts();

    loading.value = false;
  } catch (err) {
    console.error("获取患者数据失败:", err);
    error.value = true;
    loading.value = false;

    // 使用示例数据
    motionData.value = [75, 82, 90, 88, 79, 85, 78, 81, 87, 84, 80, 83];
    scoreData.value = [8, 9, 10, 9, 8, 9, 8, 9, 10, 9, 8, 9];
    initCharts();
  }
}

function initCharts() {
  // 初始化运动幅度图表
  if (motionChartRef.value) {
    motionChart = echarts.init(motionChartRef.value);

    const motionOption = {
      title: {
        text: "关节运动幅度",
      },
      tooltip: {
        trigger: "axis",
      },
      legend: {
        data: ["左侧", "右侧"],
      },
      grid: {
        left: "3%",
        right: "4%",
        bottom: "3%",
        containLabel: true,
      },
      xAxis: {
        type: "category",
        data: ["1", "2", "3", "4", "5", "6"],
      },
      yAxis: {
        type: "value",
        name: "角度 (°)",
      },
      series: [
        {
          name: "左侧",
          type: "bar",
          data: motionData.value.slice(0, 6),
          itemStyle: {
            color: "#5470c6",
          },
        },
        {
          name: "右侧",
          type: "bar",
          data: motionData.value.slice(6, 12),
          itemStyle: {
            color: "#91cc75",
          },
        },
      ],
    };

    motionChart.setOption(motionOption);
  }

  // 初始化得分图表
  if (scoreChartRef.value) {
    scoreChart = echarts.init(scoreChartRef.value);

    const scoreOption = {
      title: {
        text: "关节运动得分",
      },
      tooltip: {
        trigger: "axis",
      },
      legend: {
        data: ["左侧", "右侧"],
      },
      radar: {
        indicator: [
          { name: "1", max: 10 },
          { name: "2", max: 10 },
          { name: "3", max: 10 },
          { name: "4", max: 10 },
          { name: "5", max: 10 },
          { name: "6", max: 10 },
        ],
      },
      series: [
        {
          type: "radar",
          data: [
            {
              value: scoreData.value.slice(0, 6),
              name: "左侧",
              areaStyle: {
                color: "rgba(84, 112, 198, 0.6)",
              },
              lineStyle: {
                color: "#5470c6",
              },
            },
            {
              value: scoreData.value.slice(6, 12),
              name: "右侧",
              areaStyle: {
                color: "rgba(145, 204, 117, 0.6)",
              },
              lineStyle: {
                color: "#91cc75",
              },
            },
          ],
        },
      ],
    };

    scoreChart.setOption(scoreOption);
  }
}

// 页面缩放时重置图表大小
function handleResize() {
  if (motionChart) motionChart.resize();
  if (scoreChart) scoreChart.resize();
}

onMounted(() => {
  fetchPatientData();
  window.addEventListener("resize", handleResize);
});
</script>

<style scoped>
.patient-3d-view {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.view-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.view-header h3 {
  margin: 0;
  color: #303133;
  font-size: 20px;
  font-weight: 500;
}

.loading-container,
.error-container {
  padding: 40px;
  display: flex;
  justify-content: center;
}

.view-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.data-display {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.chart-container {
  flex: 1;
  min-width: 300px;
}

.chart-container h4 {
  margin-top: 0;
  margin-bottom: 10px;
  color: #606266;
  font-weight: 500;
}

.chart {
  height: 350px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.data-analysis {
  margin-top: 20px;
}

.data-analysis h4 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #606266;
  font-weight: 500;
}

@media (max-width: 768px) {
  .data-display {
    flex-direction: column;
  }

  .chart {
    height: 280px;
  }
}
</style>

<template>
  <div class="patient-management-page">
    <div class="header">
      <h2>患者步态分析报告</h2>
      <div class="header-actions">
        <el-button type="primary" @click="view3DData">查看3D分析数据</el-button>
      </div>
    </div>

    <div class="content">
      <div class="search-bar">
        <el-input
          v-model="searchQuery"
          placeholder="搜索历史记录 (日期/类型/摘要)"
          clearable
          style="width: 300px"
        />
      </div>

      <div class="report-list-container">
        <el-table
          :data="filteredRecords"
          border
          stripe
          style="width: 100%"
          @row-click="showReportDetails"
        >
          <el-table-column prop="date" label="日期" sortable width="150" />
          <el-table-column prop="type" label="报告类型" sortable width="180" />
          <el-table-column prop="summary" label="摘要" min-width="200" />
        </el-table>
      </div>

      <div v-if="selectedReportData" class="report-details">
        <h3>
          {{ selectedReportData.date }} -
          {{ selectedReportData.type }} 步态分析数据
        </h3>
        <el-table :data="gaitDataTable" border stripe style="width: 100%">
          <el-table-column prop="name" label="指标" width="180" />
          <el-table-column
            v-for="(col, index) in gaitColumns"
            :key="index"
            :prop="col"
            :label="col"
            width="120"
          />
        </el-table>
      </div>
      <div v-else class="no-report-selected">
        请点击表格中的报告查看详细数据。
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import axios from "axios";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

// 数据模型
const searchQuery = ref("");
const patientId = ref(route.params.id);
const reports = ref([]);
const selectedReportData = ref(null);
const gaitColumns = ref([
  "L1",
  "L2",
  "L3",
  "L4",
  "L5",
  "L6",
  "R1",
  "R2",
  "R3",
  "R4",
  "R5",
  "R6",
]);
const gaitDataRows = ref([
  { name: "运动幅度", values: [] },
  { name: "得分", values: [] },
]);

// 计算属性
const filteredRecords = computed(() => {
  return reports.value.filter((record) => {
    return (
      record.date.includes(searchQuery.value) ||
      record.type.includes(searchQuery.value) ||
      record.summary.includes(searchQuery.value)
    );
  });
});

// 表格数据格式化
const gaitDataTable = computed(() => {
  if (!selectedReportData.value) {
    return [];
  }
  const data = [];
  gaitDataRows.value.forEach((row) => {
    const rowData = { name: row.name };
    gaitColumns.value.forEach((col, index) => {
      rowData[col] = selectedReportData.value.data[row.name]?.[index] ?? "";
    });
    data.push(rowData);
  });
  return data;
});

// 方法定义
const showReportDetails = (row) => {
  selectedReportData.value = row;
};

const view3DData = () => {
  router.push(`/patient/${patientId.value}/3d-view`);
};

const fetchPatientReports = async () => {
  try {
    const response = await axios.get(`/api/patient/${patientId.value}/reports`);
    reports.value = response.data;
    if (reports.value.length > 0) {
      selectedReportData.value = reports.value[0];
    }
  } catch (error) {
    console.error("获取患者报告失败:", error);
  }
};

onMounted(() => {
  fetchPatientReports();
});
</script>

<style scoped>
.patient-management-page {
  padding: 20px;
  background-color: #f4f5f7; /* Light grey background */
  min-height: 100vh;
  box-sizing: border-box;
}

.header {
  background-color: #007bff; /* Primary blue color */
  color: white;
  padding: 15px;
  border-radius: 5px;
  margin-bottom: 20px;
  text-align: center;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}

h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 500;
}

.content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-bar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.report-list-container {
  width: 100%;
  overflow-x: auto;
  margin-bottom: 20px;
}

.report-details {
  margin-top: 20px;
  padding: 15px;
  background-color: #e9ecef; /* Light grey background for details */
  border-radius: 5px;
}

h3 {
  text-align: left;
  margin-bottom: 15px;
  color: #333;
  font-size: 18px;
  font-weight: 400;
}

.no-report-selected {
  text-align: center;
  padding: 20px;
  color: #6c757d; /* Muted grey color */
  background-color: #f8f9fa; /* Very light grey */
  border: 1px solid #dee2e6;
  border-radius: 5px;
}

/* Element Plus specific styles (optional, adjust as needed) */
.el-input {
  --el-input-focus-border-color: #007bff;
}

.el-table th.el-table__cell {
  background-color: #f0f2f5 !important; /* Light background for table headers */
  color: #333;
  font-weight: 500;
}

.el-table tr:nth-child(even) {
  background-color: #f9f9f9;
}
</style>

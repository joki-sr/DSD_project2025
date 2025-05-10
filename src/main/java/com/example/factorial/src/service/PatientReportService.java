package com.example.factorial.src.service;

import com.example.factorial.src.entity.Patient;
import com.example.factorial.src.entity.PatientReport;
import com.example.factorial.src.entity.ReportData;
import com.example.factorial.src.repository.PatientReportRepository;
import com.example.factorial.src.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 患者报告服务，处理患者报告管理的业务逻辑
 */
@Service
public class PatientReportService {

    private final PatientReportRepository reportRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public PatientReportService(
            PatientReportRepository reportRepository,
            PatientRepository patientRepository) {
        this.reportRepository = reportRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * 获取患者的所有报告
     * 
     * @param patientId 患者ID
     * @return 包含报告列表的响应
     */
    public Map<String, Object> getReportsByPatientId(String patientId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查患者是否存在
            Patient patient = patientRepository.findById(patientId).orElse(null);
            if (patient == null) {
                response.put("status", 404);
                response.put("message", "未找到指定的患者");
                response.put("data", null);
                return response;
            }
            
            // 获取该患者的所有报告
            List<PatientReport> reports = reportRepository.findByPatientIdOrderByDateDesc(patientId);
            
            // 转换为前端需要的格式
            List<Map<String, Object>> reportList = new ArrayList<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            for (PatientReport report : reports) {
                Map<String, Object> reportMap = new HashMap<>();
                reportMap.put("date", dateFormat.format(report.getReportDate()));
                reportMap.put("type", report.getType());
                reportMap.put("summary", report.getSummary());
                
                // 获取报告数据
                ReportData reportData = report.getReportData();
                if (reportData != null) {
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("标准幅度", reportData.getStandardRange());
                    dataMap.put("运动幅度", reportData.getMotionRange());
                    dataMap.put("差异", reportData.getDifference());
                    reportMap.put("reportData", dataMap);
                } else {
                    reportMap.put("reportData", new HashMap<>());
                }
                
                reportList.add(reportMap);
            }
            
            response.put("status", 200);
            response.put("data", reportList);
            response.put("message", "查询成功");
            
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "服务器内部错误");
            response.put("data", null);
        }
        
        return response;
    }

    /**
     * 获取单个报告
     * 
     * @param reportId 报告ID
     * @return 报告信息
     */
    public PatientReport getReportById(String reportId) {
        return reportRepository.findByReportId(reportId);
    }

    /**
     * 更新报告类型和摘要
     * 
     * @param reportId 报告ID
     * @param type 报告类型
     * @param summary 报告摘要
     * @return 包含更新结果的响应
     */
    @Transactional
    public Map<String, Object> updateReportTypeAndSummary(String reportId, String type, String summary) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查报告是否存在
            PatientReport report = reportRepository.findByReportId(reportId);
            if (report == null) {
                response.put("status", 404);
                response.put("message", "未找到指定的报告");
                response.put("data", null);
                return response;
            }
            
            // 更新报告
            boolean updated = reportRepository.updateReportTypeAndSummary(reportId, type, summary);
            
            if (updated) {
                // 构建响应数据
                Map<String, Object> updatedFields = new HashMap<>();
                updatedFields.put("type", type);
                updatedFields.put("summary", summary);
                updatedFields.put("updatedAt", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new java.util.Date()));
                
                Map<String, Object> reportData = new HashMap<>();
                reportData.put("reportId", reportId);
                reportData.put("updatedFields", updatedFields);
                
                response.put("status", 200);
                response.put("message", "报告更新成功");
                response.put("data", reportData);
            } else {
                response.put("status", 500);
                response.put("message", "更新失败");
                response.put("data", null);
            }
            
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "服务器内部错误");
            response.put("data", null);
        }
        
        return response;
    }

    /**
     * 创建新报告
     * 
     * @param patientId 患者ID
     * @param rawDataId 原始数据ID
     * @param reportData 报告数据
     * @return 新创建的报告
     */
    @Transactional
    public PatientReport createReport(String patientId, String rawDataId, ReportData reportData) {
        // 创建新报告
        PatientReport report = new PatientReport();
        report.setPatientId(patientId);
        report.setReportDate(new java.util.Date());
        report.setType("自动生成");
        report.setSummary("系统自动生成的报告");
        report.setReportData(reportData);
        report.setRawDataId(rawDataId);
        
        // 保存报告
        return reportRepository.save(report);
    }
} 
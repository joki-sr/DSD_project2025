package com.example.factorial.src.service;

import com.example.factorial.src.entity.Patient;
import com.example.factorial.src.entity.ReportData;
import com.example.factorial.src.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * CSV处理服务，处理IMU数据CSV文件上传和处理
 */
@Service
public class CSVProcessingService {
    
    private final PatientRepository patientRepository;
    private final PatientReportService reportService;
    
    @Autowired
    public CSVProcessingService(
            PatientRepository patientRepository,
            PatientReportService reportService) {
        this.patientRepository = patientRepository;
        this.reportService = reportService;
    }
    
    /**
     * 处理CSV文件上传
     * 
     * @param file CSV文件
     * @param patientId 患者ID
     * @return 处理结果
     */
    @Transactional
    public Map<String, Object> processCSVUpload(MultipartFile file, String patientId) {
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
            
            // 解析CSV文件
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            double standardRange = 0;
            double motionRange = 0;
            double difference = 0;
            
            // 简单处理，实际应该根据需求进行复杂的数据分析
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                // 这里只是示例代码，实际处理应更复杂
                if (values.length >= 3) {
                    try {
                        double value1 = Double.parseDouble(values[0].trim());
                        double value2 = Double.parseDouble(values[1].trim());
                        standardRange = Math.max(standardRange, value1);
                        motionRange = Math.max(motionRange, value2);
                    } catch (NumberFormatException e) {
                        // 跳过非数字行
                    }
                }
            }
            
            difference = Math.abs(standardRange - motionRange);
            
            // 创建报告数据
            ReportData reportData = new ReportData();
            reportData.setStandardRange(standardRange);
            reportData.setMotionRange(motionRange);
            reportData.setDifference(difference);
            
            // 生成原始数据ID
            String rawDataId = UUID.randomUUID().toString().replace("-", "");
            
            // 创建报告
            reportService.createReport(patientId, rawDataId, reportData);
            
            // 构建响应数据
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("patientId", patientId);
            resultData.put("fileName", file.getOriginalFilename());
            resultData.put("standardRange", standardRange);
            resultData.put("motionRange", motionRange);
            resultData.put("difference", difference);
            
            response.put("status", 201);
            response.put("message", "文件上传成功，报告已生成");
            response.put("data", resultData);
            
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "服务器内部错误：" + e.getMessage());
            response.put("data", null);
        }
        
        return response;
    }
} 
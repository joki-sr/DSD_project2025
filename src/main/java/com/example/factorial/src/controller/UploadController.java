package com.example.factorial.src.controller;

import com.example.factorial.src.service.CSVProcessingService;
import com.example.factorial.src.service.PatientReportService;
import com.example.factorial.src.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 上传控制器，处理CSV文件上传和报告更新
 */
@RestController
public class UploadController {

    private final CSVProcessingService csvService;
    private final PatientReportService reportService;
    private final AuthenticationService authService;

    @Autowired
    public UploadController(
            CSVProcessingService csvService,
            PatientReportService reportService,
            AuthenticationService authService) {
        this.csvService = csvService;
        this.reportService = reportService;
        this.authService = authService;
    }

    /**
     * 上传IMU数据CSV文件
     * 
     * @param file CSV文件
     * @param patientId 患者ID
     * @return 上传结果
     */
    @PostMapping("/api/upload/csv")
    public ResponseEntity<Map<String, Object>> uploadCSV(
            @RequestParam("file") MultipartFile file,
            @RequestParam("patientId") String patientId) {
        
        // 验证请求参数
        if (file == null || file.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", "请求错误：未提供文件");
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        }
        
        if (patientId == null || patientId.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", "请求错误：缺少患者ID");
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        }
        
        // 验证文件类型
        if (!file.getOriginalFilename().endsWith(".csv")) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", "请求错误：文件类型必须为CSV");
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        }
        
        // 验证当前用户是否有权限操作该患者数据
        if (!authService.canAccessPatientData(patientId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 403);
            response.put("message", "权限不足，无法为该患者上传数据");
            response.put("data", null);
            return ResponseEntity.status(403).body(response);
        }
        
        // 调用服务层处理CSV上传
        Map<String, Object> result = csvService.processCSVUpload(file, patientId);
        int statusCode = (int) result.get("status");
        return ResponseEntity.status(statusCode).body(result);
    }

    /**
     * 更新报告信息
     * 
     * @param reportId 报告ID
     * @param request 更新请求
     * @return 更新结果
     */
    @PutMapping("/api/report/{reportId}")
    public ResponseEntity<Map<String, Object>> updateReport(
            @PathVariable String reportId,
            @RequestBody Map<String, String> request) {
        
        // 验证请求参数
        if (reportId == null || reportId.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", "请求错误：缺少报告ID");
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        }
        
        String type = request.get("type");
        String summary = request.get("summary");
        
        if (type == null || summary == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", "请求错误：缺少必需字段");
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        }
        
        // 调用服务层更新报告
        Map<String, Object> result = reportService.updateReportTypeAndSummary(reportId, type, summary);
        int statusCode = (int) result.get("status");
        return ResponseEntity.status(statusCode).body(result);
    }
} 
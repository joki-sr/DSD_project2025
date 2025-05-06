package com.example.factorial.src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PatientController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取患者列表
     * 如果指定了doctorUsername参数，则返回该医生关联的患者
     * 否则返回所有患者
     */
    @GetMapping("/patients")
    public ResponseEntity<Map<String, Object>> getPatients(@RequestParam(required = false) String doctorUsername) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql;
            List<Map<String, Object>> patients;
            
            if (doctorUsername != null && !doctorUsername.trim().isEmpty()) {
                // 查询指定医生的患者
                sql = "SELECT id_card as id, name, birth_date, gender, phone, address FROM dsd.patient WHERE doctor_username = ?";
                patients = jdbcTemplate.queryForList(sql, doctorUsername);
            } else {
                // 查询所有患者
                sql = "SELECT id_card as id, name, birth_date, gender, phone, address FROM dsd.patient";
                patients = jdbcTemplate.queryForList(sql);
            }
            
            response.put("code", 200);
            response.put("message", "获取患者列表成功");
            response.put("data", patients);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", 500);
            response.put("message", "获取患者列表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取指定患者的详细信息
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Map<String, Object>> getPatientById(@PathVariable String patientId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql = "SELECT id_card as id, name, birth_date, gender, phone, address FROM dsd.patient WHERE id_card = ?";
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, patientId);
            
            if (result.isEmpty()) {
                response.put("code", 404);
                response.put("message", "未找到指定患者");
                return ResponseEntity.status(404).body(response);
            }
            
            response.put("code", 200);
            response.put("message", "获取患者信息成功");
            response.put("data", result.get(0));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", 500);
            response.put("message", "获取患者信息失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取患者报告列表
     */
    @GetMapping("/patient/{patientId}/reports")
    public ResponseEntity<List<Map<String, Object>>> getPatientReports(@PathVariable String patientId) {
        try {
            // 从数据库中查询患者报告
            String sql = "SELECT report_date as date, report_type as type, summary, " +
                         "motion_range as '运动幅度', score as '得分' " +
                         "FROM dsd.patient_reports WHERE patient_id = ? " +
                         "ORDER BY report_date DESC";
            
            List<Map<String, Object>> reports = jdbcTemplate.queryForList(sql, patientId);
            
            // 如果没有找到报告，返回空列表而不是错误
            if (reports.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>());
            }
            
            // 处理数据格式，确保JSON字符串被正确处理
            for (Map<String, Object> report : reports) {
                // 创建data对象存储运动幅度和得分
                Map<String, Object> data = new HashMap<>();
                data.put("运动幅度", report.get("运动幅度"));
                data.put("得分", report.get("得分"));
                
                // 从report中移除单独的运动幅度和得分字段
                report.remove("运动幅度");
                report.remove("得分");
                
                // 添加data对象
                report.put("data", data);
            }
            
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
    /**
     * 获取CSV格式的情感分析数据
     */
    @GetMapping("/sentiment_data.csv")
    public ResponseEntity<String> getSentimentDataCsv() {
        try {
            // 查询情感数据
            String sql = "SELECT date, type, summary, motion_range as '运动幅度', score as '得分' FROM dsd.sentiment_data ORDER BY date";
            List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
            
            // 如果数据库中没有数据，返回一些静态数据
            if (data.isEmpty()) {
                // 添加一些静态测试数据
                StringBuilder csvContent = new StringBuilder();
                csvContent.append("date,type,summary,运动幅度,得分\n");
                csvContent.append("2023-01-15,步态分析,正常步态,\"[75, 82, 90, 88, 79, 85, 78, 81, 87, 84, 80, 83]\",\"[8, 9, 10, 9, 8, 9, 8, 9, 10, 9, 8, 9]\"\n");
                csvContent.append("2023-02-10,步态分析,轻度异常,\"[70, 75, 82, 80, 72, 78, 74, 76, 81, 79, 73, 77]\",\"[7, 8, 9, 8, 7, 8, 7, 8, 9, 8, 7, 8]\"\n");
                csvContent.append("2023-03-20,步态分析,略有改善,\"[78, 85, 92, 90, 82, 88, 80, 84, 89, 86, 83, 85]\",\"[8, 9, 10, 10, 9, 9, 8, 9, 10, 9, 9, 9]\"\n");
                csvContent.append("2023-04-05,步态分析,显著进步,\"[82, 88, 95, 92, 86, 90, 84, 87, 93, 89, 85, 88]\",\"[9, 10, 10, 10, 9, 10, 9, 9, 10, 10, 9, 9]\"\n");
                
                return ResponseEntity.ok()
                        .header("Content-Type", "text/csv;charset=UTF-8")
                        .header("Content-Disposition", "attachment; filename=\"sentiment_data.csv\"")
                        .body(csvContent.toString());
            }
            
            // 构建CSV内容
            StringBuilder csvContent = new StringBuilder();
            
            // 添加CSV头
            csvContent.append("date,type,summary,运动幅度,得分\n");
            
            // 添加数据行 - 正确处理CSV格式，对包含逗号的字段加引号
            for (Map<String, Object> row : data) {
                csvContent.append(row.get("date")).append(",")
                         .append(row.get("type")).append(",")
                         .append(row.get("summary")).append(",")
                         .append("\"").append(row.get("运动幅度")).append("\",")
                         .append("\"").append(row.get("得分")).append("\"\n");
            }
            
            return ResponseEntity.ok()
                    .header("Content-Type", "text/csv;charset=UTF-8")
                    .header("Content-Disposition", "attachment; filename=\"sentiment_data.csv\"")
                    .body(csvContent.toString());
        } catch (Exception e) {
            e.printStackTrace();
            // 返回一个友好的错误消息
            return ResponseEntity.status(500)
                    .header("Content-Type", "text/plain;charset=UTF-8")
                    .body("Error generating CSV: " + e.getMessage());
        }
    }

    /**
     * 处理CSV文件上传，与患者关联
     */
    @PostMapping("/upload/csv")
    public ResponseEntity<Map<String, Object>> uploadCsvFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("patientId") String patientId) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (file.isEmpty()) {
            response.put("code", 400);
            response.put("message", "请上传文件");
            return ResponseEntity.status(400).body(response);
        }
        
        try {
            // 检查患者是否存在
            String checkPatientSql = "SELECT COUNT(*) FROM dsd.patient WHERE id_card = ?";
            Integer patientCount = jdbcTemplate.queryForObject(checkPatientSql, Integer.class, patientId);
            
            if (patientCount == null || patientCount == 0) {
                response.put("code", 404);
                response.put("message", "患者不存在，ID: " + patientId);
                return ResponseEntity.status(404).body(response);
            }
            
            // 读取CSV文件内容
            String csvContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            
            // 1. 检查是否存在patient_csv_data表，如果不存在则创建
            try {
                String createTableSql = 
                    "CREATE TABLE IF NOT EXISTS dsd.patient_csv_data (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "patient_id VARCHAR(50) NOT NULL, " +
                    "file_name VARCHAR(255) NOT NULL, " + 
                    "upload_time DATETIME NOT NULL, " +
                    "file_content MEDIUMTEXT NOT NULL, " +  // 使用MEDIUMTEXT存储CSV内容，最大16MB
                    "file_size INT NOT NULL, " +
                    "INDEX (patient_id) " +  // 添加索引以加快查询
                    ")";
                jdbcTemplate.execute(createTableSql);
            } catch (Exception e) {
                System.err.println("创建表失败: " + e.getMessage());
                // 继续执行，因为表可能已经存在
            }
            
            // 2. 保存文件内容到数据库
            String insertFileSql = 
                "INSERT INTO dsd.patient_csv_data (patient_id, file_name, upload_time, file_content, file_size) " +
                "VALUES (?, ?, NOW(), ?, ?)";
            jdbcTemplate.update(insertFileSql, 
                    patientId, 
                    file.getOriginalFilename(), 
                    csvContent, 
                    file.getSize());
            
            // 3. 记录上传日志
            String logSql = "INSERT INTO dsd.data_upload_log (patient_id, file_name, upload_time, status) VALUES (?, ?, NOW(), 'SUCCESS')";
            jdbcTemplate.update(logSql, patientId, file.getOriginalFilename());
            
            // 4. 添加简单的报告记录
            String reportSql = "INSERT INTO dsd.patient_reports (patient_id, report_date, report_type, summary, motion_range, score) VALUES (?, NOW(), '步态分析', '蓝牙测量数据上传', '[]', '[]')";
            jdbcTemplate.update(reportSql, patientId);
            
            // 5. 提取CSV数据进行简单分析
            // 这里只是一个示例，实际项目中需要更复杂的分析
            String[] csvLines = csvContent.split("\\n");
            int dataPoints = csvLines.length > 1 ? csvLines.length - 1 : 0; // 减去表头
            
            response.put("code", 200);
            response.put("message", "文件上传成功");
            response.put("fileName", file.getOriginalFilename());
            response.put("fileSize", file.getSize());
            response.put("dataPoints", dataPoints);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", 500);
            response.put("message", "文件上传失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
} 
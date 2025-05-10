package com.example.factorial.src.controller;

import com.example.factorial.src.service.DoctorPatientRelationService;
import com.example.factorial.src.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 医生控制器，处理医生相关请求
 */
@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorPatientRelationService relationService;
    private final AuthenticationService authService;

    @Autowired
    public DoctorController(
            DoctorPatientRelationService relationService,
            AuthenticationService authService) {
        this.relationService = relationService;
        this.authService = authService;
    }

    /**
     * 获取医生的患者列表
     * 
     * @param doctorId 医生ID
     * @return 患者列表
     */
    @GetMapping("/{doctorId}/patients")
    public ResponseEntity<Map<String, Object>> getDoctorPatients(@PathVariable String doctorId) {
        // 验证医生ID
        if (doctorId == null || doctorId.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", "请求参数错误：缺少医生ID");
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        }
        
        // 验证当前用户是否有权限访问该医生数据
        if (!authService.canAccessDoctorData(doctorId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 403);
            response.put("message", "权限不足，无法查看该医生的患者列表");
            response.put("data", null);
            return ResponseEntity.status(403).body(response);
        }

        // 调用服务层获取患者列表
        Map<String, Object> result = relationService.getPatientsByDoctorId(doctorId);
        int statusCode = (int) result.get("status");
        return ResponseEntity.status(statusCode).body(result);
    }
} 
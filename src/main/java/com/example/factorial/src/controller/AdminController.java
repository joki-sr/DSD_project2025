package com.example.factorial.src.controller;

import com.example.factorial.src.service.DoctorService;
import com.example.factorial.src.service.DoctorPatientRelationService;
import com.example.factorial.src.service.PatientService;
import com.example.factorial.src.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员控制器，处理医生管理、患者管理和医患关系管理
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final DoctorPatientRelationService relationService;
    private final AuthenticationService authService;

    @Autowired
    public AdminController(
            DoctorService doctorService,
            PatientService patientService,
            DoctorPatientRelationService relationService,
            AuthenticationService authService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.relationService = relationService;
        this.authService = authService;
    }

    /**
     * 获取所有医生列表
     */
    @GetMapping("/doctors")
    public ResponseEntity<Map<String, Object>> getAllDoctors() {
        Map<String, Object> result = doctorService.getAllDoctors();
        int statusCode = (int) result.get("status");
        return ResponseEntity.status(statusCode).body(result);
    }

    /**
     * 注册新医生
     */
    @PostMapping("/doctors")
    public ResponseEntity<Map<String, Object>> registerDoctor(@RequestBody Map<String, String> request) {
        // 获取请求参数
        String password = request.get("password");
        String name = request.get("name");
        String phone = request.get("phone");
        String hospital = request.get("hospital");
        String department = request.get("department");

        // 验证请求参数
        if (password == null || name == null || phone == null || hospital == null || department == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", "请求参数错误：缺少必需字段");
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        }

        // 调用服务层处理注册
        Map<String, Object> result = doctorService.registerDoctor(password, name, phone, hospital, department);
        int statusCode = (int) result.get("status");
        return ResponseEntity.status(statusCode).body(result);
    }

    /**
     * 获取所有患者列表
     */
    @GetMapping("/patients")
    public ResponseEntity<Map<String, Object>> getAllPatients() {
        Map<String, Object> result = patientService.getAllPatients();
        int statusCode = (int) result.get("status");
        return ResponseEntity.status(statusCode).body(result);
    }

    /**
     * 获取所有医生-患者关系列表
     */
    @GetMapping("/relations")
    public ResponseEntity<Map<String, Object>> getAllDoctorPatientRelations() {
        Map<String, Object> result = relationService.getAllRelations();
        int statusCode = (int) result.get("status");
        return ResponseEntity.status(statusCode).body(result);
    }

    /**
     * 添加医生-患者关系
     */
    @PostMapping("/relations")
    public ResponseEntity<Map<String, Object>> addDoctorPatientRelation(@RequestBody Map<String, String> request) {
        // 获取请求参数
        String doctorId = request.get("doctorId");
        String patientId = request.get("patientId");

        // 验证请求参数
        if (doctorId == null || patientId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", "请求参数错误：缺少doctorId或patientId");
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        }

        // 调用服务层添加关系
        Map<String, Object> result = relationService.addRelation(doctorId, patientId);
        int statusCode = (int) result.get("status");
        return ResponseEntity.status(statusCode).body(result);
    }

    /**
     * 更新医生-患者关系
     */
    @PutMapping("/relations")
    public ResponseEntity<Map<String, Object>> updateDoctorPatientRelation(@RequestBody Map<String, String> request) {
        // 获取请求参数
        String oldDoctorId = request.get("oldDoctorId");
        String oldPatientId = request.get("oldPatientId");
        String doctorId = request.get("doctorId");
        String patientId = request.get("patientId");

        // 验证请求参数
        if (oldDoctorId == null || oldPatientId == null || doctorId == null || patientId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", "请求参数错误：缺少必需字段");
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        }

        // 调用服务层更新关系
        Map<String, Object> result = relationService.updateRelation(oldDoctorId, oldPatientId, doctorId, patientId);
        int statusCode = (int) result.get("status");
        return ResponseEntity.status(statusCode).body(result);
    }

    /**
     * 删除医生-患者关系
     */
    @DeleteMapping("/relations")
    public ResponseEntity<Map<String, Object>> deleteDoctorPatientRelation(@RequestBody Map<String, String> request) {
        // 获取请求参数
        String doctorId = request.get("doctorId");
        String patientId = request.get("patientId");

        // 验证请求参数
        if (doctorId == null || patientId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 400);
            response.put("message", "请求参数错误：缺少doctorId或patientId");
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        }

        // 调用服务层删除关系
        Map<String, Object> result = relationService.deleteRelation(doctorId, patientId);
        int statusCode = (int) result.get("status");
        return ResponseEntity.status(statusCode).body(result);
    }
} 
package com.example.factorial.src.service;

import com.example.factorial.src.entity.Patient;
import com.example.factorial.src.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 患者服务，处理患者相关的业务逻辑
 */
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * 获取所有患者列表
     * 
     * @return 包含患者列表的响应
     */
    public Map<String, Object> getAllPatients() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 获取所有患者
            List<Patient> patients = patientRepository.findAll();
            
            // 转换为前端需要的格式
            List<Map<String, Object>> patientList = new ArrayList<>();
            for (Patient patient : patients) {
                Map<String, Object> patientMap = new HashMap<>();
                patientMap.put("id", patient.getId());
                patientMap.put("name", patient.getName());
                patientList.add(patientMap);
            }
            
            response.put("status", 200);
            response.put("message", "获取患者列表成功");
            response.put("data", patientList);
            
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "服务器内部错误");
            response.put("data", null);
        }
        
        return response;
    }

    /**
     * 根据ID查找患者
     * 
     * @param id 患者ID
     * @return 患者信息
     */
    public Patient findPatientById(String id) {
        return patientRepository.findById(id).orElse(null);
    }

    /**
     * 根据用户ID查找患者
     * 
     * @param userId 用户ID
     * @return 患者信息
     */
    public Patient findPatientByUserId(Long userId) {
        return patientRepository.findByUserId(userId);
    }

    /**
     * 根据手机号查找患者
     * 
     * @param phone 手机号
     * @return 患者信息
     */
    public Patient findPatientByPhone(String phone) {
        return patientRepository.findByPhone(phone);
    }

    /**
     * 根据身份证/护照号查找患者
     * 
     * @param idNumber 身份证/护照号
     * @return 患者信息
     */
    public Patient findPatientByIdNumber(String idNumber) {
        return patientRepository.findByIdNumber(idNumber);
    }

    /**
     * 检查身份证/护照号是否已存在
     * 
     * @param idNumber 身份证/护照号
     * @return 是否存在
     */
    public boolean existsByIdNumber(String idNumber) {
        return patientRepository.existsByIdNumber(idNumber);
    }
} 
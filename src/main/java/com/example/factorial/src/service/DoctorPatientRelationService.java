package com.example.factorial.src.service;

import com.example.factorial.src.entity.Doctor;
import com.example.factorial.src.entity.DoctorPatientRelation;
import com.example.factorial.src.entity.Patient;
import com.example.factorial.src.repository.DoctorPatientRelationRepository;
import com.example.factorial.src.repository.DoctorRepository;
import com.example.factorial.src.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医生-患者关系服务，处理医患关系管理的业务逻辑
 */
@Service
public class DoctorPatientRelationService {

    private final DoctorPatientRelationRepository relationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public DoctorPatientRelationService(
            DoctorPatientRelationRepository relationRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {
        this.relationRepository = relationRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * 获取所有医患关系列表
     * 
     * @return 包含关系列表的响应
     */
    public Map<String, Object> getAllRelations() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 获取所有关系，包含详细信息
            List<Map<String, Object>> relationDTOs = relationRepository.findAllRelationsWithDetails();
            
            response.put("status", 200);
            response.put("message", "获取医生-患者关系列表成功");
            response.put("data", relationDTOs);
            
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "服务器内部错误");
            response.put("data", null);
        }
        
        return response;
    }

    /**
     * 根据医生ID获取患者列表
     * 
     * @param doctorId 医生ID
     * @return 包含患者列表的响应
     */
    public Map<String, Object> getPatientsByDoctorId(String doctorId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查医生是否存在
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            if (doctor == null) {
                response.put("status", 404);
                response.put("message", "未找到指定的医生");
                response.put("data", null);
                return response;
            }
            
            // 获取该医生的所有患者
            List<Patient> patients = relationRepository.findPatientsByDoctorId(doctorId);
            
            // 转换为前端需要的格式
            List<Map<String, Object>> patientList = new ArrayList<>();
            for (Patient patient : patients) {
                Map<String, Object> patientMap = new HashMap<>();
                patientMap.put("id", patient.getId());
                patientMap.put("name", patient.getName());
                patientMap.put("phone", patient.getPhone());
                patientMap.put("gender", patient.getGender());
                patientMap.put("birthDate", patient.getBirthDate());
                patientMap.put("idNumber", patient.getIdNumber());
                patientList.add(patientMap);
            }
            
            response.put("status", 200);
            response.put("message", "success");
            response.put("data", patientList);
            
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "服务器内部错误");
            response.put("data", null);
        }
        
        return response;
    }

    /**
     * 添加医生-患者关系
     * 
     * @param doctorId 医生ID
     * @param patientId 患者ID
     * @return 包含操作结果的响应
     */
    @Transactional
    public Map<String, Object> addRelation(String doctorId, String patientId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查医生是否存在
            Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
            if (doctor == null) {
                response.put("status", 400);
                response.put("message", "请求参数错误：医生ID '" + doctorId + "' 不存在");
                response.put("data", null);
                return response;
            }
            
            // 检查患者是否存在
            Patient patient = patientRepository.findById(patientId).orElse(null);
            if (patient == null) {
                response.put("status", 400);
                response.put("message", "请求参数错误：患者ID '" + patientId + "' 不存在");
                response.put("data", null);
                return response;
            }
            
            // 检查关系是否已存在
            if (relationRepository.existsByDoctorIdAndPatientId(doctorId, patientId)) {
                response.put("status", 409);
                response.put("message", "该医患关系已存在");
                response.put("data", null);
                return response;
            }
            
            // 创建新关系
            DoctorPatientRelation relation = new DoctorPatientRelation();
            relation.setDoctorId(doctorId);
            relation.setPatientId(patientId);
            
            // 保存关系
            relation = relationRepository.save(relation);
            
            // 构建响应数据
            Map<String, Object> relationData = new HashMap<>();
            relationData.put("doctorId", relation.getDoctorId());
            relationData.put("patientId", relation.getPatientId());
            
            response.put("status", 201);
            response.put("message", "医生-患者关系添加成功");
            response.put("data", relationData);
            
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "服务器内部错误");
            response.put("data", null);
        }
        
        return response;
    }

    /**
     * 更新医生-患者关系
     * 
     * @param oldDoctorId 原医生ID
     * @param oldPatientId 原患者ID
     * @param newDoctorId 新医生ID
     * @param newPatientId 新患者ID
     * @return 包含操作结果的响应
     */
    @Transactional
    public Map<String, Object> updateRelation(
            String oldDoctorId,
            String oldPatientId,
            String newDoctorId,
            String newPatientId) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查原关系是否存在
            if (!relationRepository.existsByDoctorIdAndPatientId(oldDoctorId, oldPatientId)) {
                response.put("status", 404);
                response.put("message", "未找到要更新的医患关系");
                response.put("data", null);
                return response;
            }
            
            // 检查新医生是否存在
            Doctor newDoctor = doctorRepository.findById(newDoctorId).orElse(null);
            if (newDoctor == null) {
                response.put("status", 400);
                response.put("message", "请求参数错误：医生ID '" + newDoctorId + "' 不存在");
                response.put("data", null);
                return response;
            }
            
            // 检查新患者是否存在
            Patient newPatient = patientRepository.findById(newPatientId).orElse(null);
            if (newPatient == null) {
                response.put("status", 400);
                response.put("message", "请求参数错误：患者ID '" + newPatientId + "' 不存在");
                response.put("data", null);
                return response;
            }
            
            // 检查新关系是否已存在（且不是原关系）
            if (!oldDoctorId.equals(newDoctorId) || !oldPatientId.equals(newPatientId)) {
                if (relationRepository.existsByDoctorIdAndPatientId(newDoctorId, newPatientId)) {
                    response.put("status", 409);
                    response.put("message", "该医患关系已存在");
                    response.put("data", null);
                    return response;
                }
            }
            
            // 更新关系
            boolean updated = relationRepository.updateRelation(oldDoctorId, oldPatientId, newDoctorId, newPatientId);
            
            if (updated) {
                // 构建响应数据
                Map<String, Object> relationData = new HashMap<>();
                relationData.put("doctorId", newDoctorId);
                relationData.put("patientId", newPatientId);
                
                response.put("status", 200);
                response.put("message", "更新医生-患者关系成功");
                response.put("data", relationData);
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
     * 删除医生-患者关系
     * 
     * @param doctorId 医生ID
     * @param patientId 患者ID
     * @return 包含操作结果的响应
     */
    @Transactional
    public Map<String, Object> deleteRelation(String doctorId, String patientId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查关系是否存在
            if (!relationRepository.existsByDoctorIdAndPatientId(doctorId, patientId)) {
                response.put("status", 404);
                response.put("message", "未找到要删除的医患关系");
                response.put("data", null);
                return response;
            }
            
            // 删除关系
            relationRepository.deleteByDoctorIdAndPatientId(doctorId, patientId);
            
            response.put("status", 204);
            response.put("message", "删除医生-患者关系成功");
            response.put("data", null);
            
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "服务器内部错误");
            response.put("data", null);
        }
        
        return response;
    }

    /**
     * 检查医生是否有权访问患者数据
     * 
     * @param doctorId 医生ID
     * @param patientId 患者ID
     * @return 是否有权访问
     */
    public boolean relationExists(String doctorId, String patientId) {
        return relationRepository.existsByDoctorIdAndPatientId(doctorId, patientId);
    }
} 
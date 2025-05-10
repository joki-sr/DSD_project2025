package com.example.factorial.src.service;

import com.example.factorial.src.entity.Doctor;
import com.example.factorial.src.entity.User;
import com.example.factorial.src.repository.DoctorRepository;
import com.example.factorial.src.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 医生服务，处理医生相关的业务逻辑
 */
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DoctorService(
            DoctorRepository doctorRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 获取所有医生列表
     * 
     * @return 包含医生列表的响应
     */
    public Map<String, Object> getAllDoctors() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 获取所有医生
            List<Doctor> doctors = doctorRepository.findAll();
            
            // 转换为前端需要的格式
            List<Map<String, Object>> doctorList = new ArrayList<>();
            for (Doctor doctor : doctors) {
                Map<String, Object> doctorMap = new HashMap<>();
                doctorMap.put("id", doctor.getId());
                doctorMap.put("name", doctor.getName());
                doctorMap.put("phone", doctor.getPhone());
                doctorMap.put("hospital", doctor.getHospital());
                doctorMap.put("department", doctor.getDepartment());
                doctorList.add(doctorMap);
            }
            
            response.put("status", 200);
            response.put("message", "获取医生列表成功");
            response.put("data", doctorList);
            
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "服务器内部错误");
            response.put("data", null);
        }
        
        return response;
    }

    /**
     * 注册新医生
     * 
     * @param password 密码
     * @param name 姓名
     * @param phone 电话号码
     * @param hospital 医院
     * @param department 科室
     * @return 包含注册结果的响应
     */
    @Transactional
    public Map<String, Object> registerDoctor(
            String password,
            String name,
            String phone,
            String hospital,
            String department) {
        
        Map<String, Object> response = new HashMap<>();
        
        // 检查手机号或姓名是否已存在
        if (doctorRepository.existsByNameOrPhone(name, phone)) {
            response.put("status", 409);
            response.put("message", "手机号或姓名已被注册");
            response.put("data", null);
            return response;
        }
        
        try {
            // 创建用户
            User user = new User();
            user.setName(name);
            user.setPhone(phone);
            user.setUsername(phone); // 使用手机号作为用户名
            user.setPassword(passwordEncoder.encode(password)); // 加密密码
            user.setUserType("DOCTOR");
            user.setDoctor(true);
            user.setPatient(false);
            user.setAdmin(false);
            
            // 保存用户
            user = userRepository.save(user);
            
            // 创建医生信息
            Doctor doctor = new Doctor();
            doctor.setId(UUID.randomUUID().toString().replace("-", ""));
            doctor.setUserId(user.getId());
            doctor.setName(name);
            doctor.setPhone(phone);
            doctor.setHospital(hospital);
            doctor.setDepartment(department);
            
            // 保存医生信息
            doctor = doctorRepository.save(doctor);
            
            // 构建响应数据
            Map<String, Object> doctorData = new HashMap<>();
            doctorData.put("id", doctor.getId());
            doctorData.put("name", doctor.getName());
            doctorData.put("phone", doctor.getPhone());
            doctorData.put("hospital", doctor.getHospital());
            doctorData.put("department", doctor.getDepartment());
            
            response.put("status", 201);
            response.put("message", "医生注册成功");
            response.put("data", doctorData);
            
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "服务器内部错误，请联系管理员");
            response.put("data", null);
        }
        
        return response;
    }

    /**
     * 根据ID查找医生
     * 
     * @param id 医生ID
     * @return 医生信息
     */
    public Doctor findDoctorById(String id) {
        return doctorRepository.findById(id).orElse(null);
    }

    /**
     * 根据手机号查找医生
     * 
     * @param phone 手机号
     * @return 医生信息
     */
    public Doctor findDoctorByPhone(String phone) {
        return doctorRepository.findByPhone(phone);
    }

    /**
     * 检查手机号或姓名是否已存在
     * 
     * @param name 姓名
     * @param phone 手机号
     * @return 是否存在
     */
    public boolean existsByNameOrPhone(String name, String phone) {
        return doctorRepository.existsByNameOrPhone(name, phone);
    }
} 
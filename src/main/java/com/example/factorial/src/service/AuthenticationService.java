package com.example.factorial.src.service;

import com.example.factorial.src.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * 认证服务，处理用户认证和权限检查
 */
@Service
public class AuthenticationService {

    private final DoctorPatientRelationService relationService;

    @Autowired
    public AuthenticationService(
            DoctorPatientRelationService relationService) {
        this.relationService = relationService;
    }

    /**
     * 获取当前登录用户
     * 
     * @return 当前用户
     */
    public User getCurrentUser() {
        try {
            UsernamePasswordAuthenticationToken authentication = 
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return (User) authentication.getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查当前用户是否有权访问患者数据
     * 
     * @param patientId 患者ID
     * @return 是否有权访问
     */
    public boolean canAccessPatientData(String patientId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // 管理员可以访问所有患者数据
        if (currentUser.isAdmin()) {
            return true;
        }

        // 患者只能访问自己的数据
        if (currentUser.isPatient()) {
            // 假设患者User的ID字段对应Patient表的userId字段
            return patientId.equals(currentUser.getId().toString());
        }

        // 医生只能访问与其有关联的患者数据
        if (currentUser.isDoctor()) {
            // 假设医生User的ID字段对应Doctor表的userId字段
            // 通过DoctorPatientRelationService检查关联
            String doctorId = currentUser.getId().toString();
            return relationExists(doctorId, patientId);
        }

        return false;
    }
    
    /**
     * 检查医患关系是否存在
     * 
     * @param doctorId 医生ID
     * @param patientId 患者ID
     * @return 关系是否存在
     */
    public boolean relationExists(String doctorId, String patientId) {
        return relationService.relationExists(doctorId, patientId);
    }
    
    /**
     * 检查当前用户是否有权访问医生数据
     * 
     * @param doctorId 医生ID
     * @return 是否有权访问
     */
    public boolean canAccessDoctorData(String doctorId) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // 管理员可以访问所有医生数据
        if (currentUser.isAdmin()) {
            return true;
        }

        // 医生只能访问自己的数据
        if (currentUser.isDoctor()) {
            return doctorId.equals(currentUser.getId().toString());
        }

        // 患者可以访问与其有关联的医生数据
        if (currentUser.isPatient()) {
            String patientId = currentUser.getId().toString();
            return relationExists(doctorId, patientId);
        }

        return false;
    }
    
    /**
     * 检查当前用户是否为管理员
     * 
     * @return 是否为管理员
     */
    public boolean isAdmin() {
        User currentUser = getCurrentUser();
        return currentUser != null && currentUser.isAdmin();
    }
    
    /**
     * 检查当前用户是否为医生
     * 
     * @return 是否为医生
     */
    public boolean isDoctor() {
        User currentUser = getCurrentUser();
        return currentUser != null && currentUser.isDoctor();
    }
    
    /**
     * 检查当前用户是否为患者
     * 
     * @return 是否为患者
     */
    public boolean isPatient() {
        User currentUser = getCurrentUser();
        return currentUser != null && currentUser.isPatient();
    }
} 
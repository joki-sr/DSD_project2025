package com.example.factorial.src.service;

import com.example.factorial.src.dto.request.LoginRequest;
import com.example.factorial.src.dto.request.RegisterRequest;
import com.example.factorial.src.dto.response.ApiResponse;
import com.example.factorial.src.dto.response.LoginResponse;
import com.example.factorial.src.dto.response.UserResponse;
import com.example.factorial.src.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 综合认证服务（门面模式）
 * 统一调用其他专门的服务类，提供对外接口
 */
@Service
public class AuthService {
    
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    
    @Autowired
    public AuthService(
            UserService userService,
            JwtService jwtService,
            AuthenticationService authenticationService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }
    
    /**
     * 处理用户登录
     */
    public ApiResponse<LoginResponse> login(LoginRequest loginRequest) {
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }
    
    /**
     * 处理用户注册
     */
    public ApiResponse<UserResponse> register(RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }
    
    /**
     * 获取当前用户
     */
    public User getCurrentUser() {
        return authenticationService.getCurrentUser();
    }
    
    /**
     * 检查用户是否有权访问患者数据
     */
    public boolean canAccessPatientData(String patientId) {
        return authenticationService.canAccessPatientData(patientId);
    }
    
    /**
     * 生成JWT令牌
     */
    public String generateToken(User user) {
        return jwtService.generateToken(user);
    }
    
    /**
     * 验证JWT令牌
     */
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }
    
    /**
     * 从令牌中提取用户名
     */
    public String extractUsername(String token) {
        return jwtService.extractUsername(token);
    }
    
    /**
     * 从令牌中提取角色
     */
    public java.util.List<String> extractRoles(String token) {
        return jwtService.extractRoles(token);
    }
} 
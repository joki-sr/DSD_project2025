package com.example.factorial.src.controller;

import com.example.factorial.src.dto.response.ApiResponse;
import com.example.factorial.src.dto.request.LoginRequest;
import com.example.factorial.src.dto.request.RegisterRequest;
import com.example.factorial.src.dto.response.LoginResponse;
import com.example.factorial.src.dto.response.UserResponse;
import com.example.factorial.src.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器，处理用户登录和注册
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final AuthService authService;

    @Autowired
    public UserController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 处理用户登录请求
     */
    @PostMapping("/session")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        // 验证请求参数
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("缺少 username 或 password 参数"));
        }
        
        // 调用服务层处理登录
        ApiResponse<LoginResponse> result = authService.login(loginRequest);
        
        // 根据状态码设置响应状态
        return ResponseEntity.status(result.getStatus()).body(result);
    }

    /**
     * 处理用户注册请求
     */
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest registerRequest) {
        // 验证请求参数
        if (registerRequest.getName() == null || registerRequest.getPhone() == null || 
            registerRequest.getGender() == null || registerRequest.getBirthDate() == null || 
            registerRequest.getIdType() == null || registerRequest.getIdNumber() == null || 
            registerRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("请求参数错误：缺少必需字段"));
        }
        
        // 调用服务层处理注册
        ApiResponse<UserResponse> result = authService.register(registerRequest);
        
        // 根据状态码设置响应状态
        return ResponseEntity.status(result.getStatus()).body(result);
    }
} 
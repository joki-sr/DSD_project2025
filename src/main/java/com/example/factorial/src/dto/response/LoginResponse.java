package com.example.factorial.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String id;
    private boolean isAdmin;
    private boolean isDoctor;
    private boolean isPatient;
    private String name;
    private String token;
} 
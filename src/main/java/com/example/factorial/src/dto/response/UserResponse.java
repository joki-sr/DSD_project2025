package com.example.factorial.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册响应数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String name;
    private String phone;
    private String gender;
    private String birthDate;
    private String idType;
    private String idNumber;
} 
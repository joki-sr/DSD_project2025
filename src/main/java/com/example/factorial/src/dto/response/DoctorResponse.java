package com.example.factorial.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 医生信息响应数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private String id;
    private String name;
    private String phone;
    private String hospital;
    private String department;
} 
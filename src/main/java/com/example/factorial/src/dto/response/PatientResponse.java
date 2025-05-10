package com.example.factorial.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 患者信息响应数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private String id;
    private String name;
    private String phone;
    private String gender;
    private String birthDate;
    private String idNumber;
} 
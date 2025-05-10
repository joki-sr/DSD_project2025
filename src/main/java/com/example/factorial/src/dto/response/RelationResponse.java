package com.example.factorial.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 医患关系响应数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelationResponse {
    private String doctorId;
    private String doctorName;
    private String patientId;
    private String patientName;
} 
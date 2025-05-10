package com.example.factorial.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 步态报告响应数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private String date;
    private String type;
    private String summary;
    private Map<String, Object> reportData;
} 
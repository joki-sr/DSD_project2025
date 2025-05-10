package com.example.factorial.src.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String name;
    private String phone;
    private String gender;
    private String birthDate;
    private String idType;
    private String idNumber;
    private String password;
} 
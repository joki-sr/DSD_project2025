package com.example.factorial.src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "欢迎使用Demo应用！<br><br>" +
               "可用API端点：<br>" +
               "- GET /api/users - 获取所有用户<br>" +
               "- GET /api/users/{id} - 根据ID获取用户<br>" +
               "- GET /api/users/name/{name} - 根据名称获取用户<br>" +
               "- POST /api/users - 创建新用户<br>" +
               "- PUT /api/users/{id} - 更新用户<br>" +
               "- DELETE /api/users/{id} - 删除用户";
    }
} 
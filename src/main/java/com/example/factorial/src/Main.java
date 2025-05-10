package com.example.factorial.src;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 应用程序主入口
 * 负责启动Spring Boot应用
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.example.factorial.src", 
    "com.example.factorial.src.controller", 
    "com.example.factorial.src.service", 
    "com.example.factorial.src.repository",
    "com.example.factorial.src.config",
    "com.example.factorial.src.advice",
    "com.example.factorial.src.filter"
})
@EntityScan("com.example.factorial.src.entity")
@EnableJpaRepositories("com.example.factorial.src.repository")
public class Main {
    public static void main(String[] args) {
        // 启动Spring应用
        SpringApplication.run(Main.class, args);
        
        System.out.println("医患管理系统后端已启动");
        System.out.println("==============================");
        System.out.println("API接口列表:");
        System.out.println("登录: POST /api/session");
        System.out.println("注册: POST /api/users");
        System.out.println("管理员接口: /api/admin/**");
        System.out.println("医生接口: /api/doctor/**");
        System.out.println("患者接口: /api/patient/**");
        System.out.println("==============================");
    }
}
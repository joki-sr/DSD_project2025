package com.example.factorial.src;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.factorial.src", "com.example.factorial.src.controller", "com.example.factorial.src.service", "com.example.factorial.src.repository"})
public class Main {
    public static void main(String[] args) {
        // 先启动Spring应用
        SpringApplication.run(Main.class, args);
        System.out.println("后端已启动，访问 https://localhost:12345");
        
        // 然后初始化应用业务逻辑
        try {
            //权限树初始化
            PermissionEntityManagement.GetSingleton().INTI();
            //登录验证后返回用户类型，以创建该用户的权限类型
            User user = new User("张三",PermissionEntityManagement.GetSingleton().GetPat());
            //页面初始化,获得了属于该用户权限类型的Pages
            PageManagement.GetSingleton().INTI(user);
        } catch (Exception e) {
            System.out.println("初始化过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
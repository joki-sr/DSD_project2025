package com.example.factorial.src;

public class SignupRequest {
    private String username;
    private String password;

    // 必须有无参构造函数（Spring 反射需要）
    public SignupRequest() {}

    // Getter 和 Setter 方法（必需）
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

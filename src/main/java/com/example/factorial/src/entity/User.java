package com.example.factorial.src.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter @Setter @NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "phone", unique = true)
    private String phone;
    
    @Column(name = "user_type")
    private String userType;
    
    @Column(name = "is_doctor")
    private boolean isDoctor;
    
    @Column(name = "is_patient")
    private boolean isPatient;
    
    @Column(name = "is_admin")
    private boolean isAdmin;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Doctor doctor;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Patient patient;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public User(String username, String password, String name, String phone, String userType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.userType = userType;
        
        // 设置用户角色
        this.isAdmin = "ADMIN".equals(userType);
        this.isDoctor = "DOCTOR".equals(userType);
        this.isPatient = "PATIENT".equals(userType);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
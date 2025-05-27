package com.example.factorial.src.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

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

    /** 联系电话（允许国际区号，可根据需要调整正则） */
    @Column(name = "phonenumber", length = 45)
    @Pattern(regexp = "^\\+?\\d{1,45}$", message = "电话号码格式不正确")
    private String phonenumber;

//    @Enumerated(EnumType.STRING)
//    private Role role;
    /** 角色类型（DOCTOR/PATIENT） */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "ENUM('DOCTOR','PATIENT','ADMIN')")
    private User.RoleType roletype;
    /* ---------- 枚举 ---------- */

    public enum RoleType {
        @JsonProperty("DOCTOR")
        DOCTOR,
        @JsonProperty("PATIENT")
        PATIENT,
        @JsonProperty("ADMIN")
        ADMIN,
    }
    
    public User(String username, String password, User.RoleType roletype, String phonenumber) {
        this.username = username;
        this.password = password;
        this.roletype = roletype;
        this.phonenumber = phonenumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roletype=" + roletype + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                '}';
    }
}
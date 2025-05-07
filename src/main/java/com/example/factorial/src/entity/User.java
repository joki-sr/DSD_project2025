package com.example.factorial.src.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    
    public User(String username, String password, User.RoleType roletype) {
        this.username = username;
        this.password = password;
        this.roletype = roletype;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roletype=" + roletype + '\'' +
                '}';
    }
}
package com.example.factorial.src;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String uName;
    
    @Transient // 这个字段不会保存到数据库
    private PermissionNode permission;
    
    @Column(name = "permission_type")
    private String permissionType;
    
    public User(String n, PermissionNode p){
        uName = n;
        permission = p;
        this.permissionType = (p != null) ? p.getClass().getSimpleName() : "DefaultPermission";
    }
    
    public PermissionBase GetPermission(){
        return this.permission;
    }
}

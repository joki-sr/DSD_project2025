package com.example.factorial.src;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
* 安全代理，使用组合实例的方式进行访问控制
* */
/*
* 安全代理
* 通过该类的接口调用方法
* */
@Component
public class SafeProxy{
    private final DataBase dataBase;
    private String permission;
    
    @Autowired
    public SafeProxy(DataBase dataBase){
        this.dataBase = dataBase;
        this.permission = "default";
    }
    
    public void setPermission(String permission) {
        this.permission = permission;
    }
    
    public int GET(PermissionBase p){
        if(p.check(permission)){
            System.out.println("当前权限可以访问");
            return dataBase.GETDATE();
        }
        else{
            System.out.println("权限不足");
            return 0;
        }
    }
}

package com.example.factorial.src.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * 对应表：doctor
 */
@Entity
@Table(name = "doctor")
@Getter @Setter @NoArgsConstructor
public class Doctor {

    /** 主键：登录用户名 */
    @Id
    @Column(name = "username", nullable = false, length = 255)
    @NotBlank(message = "用户名不能为空")
    @Size(max = 255, message = "用户名长度不能超过 255 字符")
    private String username;

    /** 医生姓名 */
    @Column(name = "docname", length = 45)
    @Size(max = 45, message = "医生姓名长度不能超过 45 字符")
    private String docname;

    /** 所属医院 */
    @Column(name = "hospital", length = 45)
    @Size(max = 45, message = "医院名称长度不能超过 45 字符")
    private String hospital;

    /** 科室 */
    @Column(name = "Department", length = 45)
    @Size(max = 45, message = "科室名称长度不能超过 45 字符")
    private String department;

    /** 联系电话（允许国际区号，可根据需要调整正则） */
    @Column(name = "phonenumber", length = 45)
    @Pattern(regexp = "^\\+?\\d{1,45}$", message = "电话号码格式不正确")
    private String phonenumber;

    /** 业务构造器（可按需扩展） */
    public Doctor(String username,
                  String docname,
                  String hospital,
                  String department,
                  String phonenumber) {
        this.username   = username;
        this.docname    = docname;
        this.hospital   = hospital;
        this.department = department;
        this.phonenumber= phonenumber;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "username='"   + username    + '\'' +
                ", docname='"  + docname     + '\'' +
                ", hospital='" + hospital    + '\'' +
                ", department='"+ department + '\'' +
                ", phonenumber='"+ phonenumber + '\'' +
                '}';
    }
}




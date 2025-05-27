package com.example.factorial.src.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * 对应表：patient
 */
@Entity
@Table(name = "patient")
@Getter @Setter @NoArgsConstructor
public class Patient {

    /** 用户名 —— 主键 */
    @Id
    @Column(name = "username", length = 255, nullable = false)
    @Size(max = 255, message = "用户名长度不能超过 255 字符")
    private String username;

    /** 证件类型（passport / idCard） */
    @Enumerated(EnumType.STRING)
    @Column(name = "id_type", columnDefinition = "ENUM('passport','idCard')")
    private IdType idtype;

    /** 证件号码 */
    @Column(name = "id_number", length = 32)
    @Size(max = 32, message = "真实姓名长度不能超过 32 字符")
    private String idnumber;


    /** 真实姓名 */
    @Column(name = "realname", length = 45)
    @Size(max = 45, message = "真实姓名长度不能超过 45 字符")
    private String realname;

//    /** 出生年份（建议 YYYY） */
//    @Column(name = "birthdate", length = 4)
//    @Pattern(regexp = "^(19|20)\\d{2}$",
//             message = "出生年份必须是 1900–2099 之间的 4 位数字")
//    private String birthyear;
    /** 出生日期（格式 YYYY-MM-DD） */
    @Column(name = "birthdate")
    @Pattern(
            regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
            message = "出生日期必须是格式为 YYYY-MM-DD 的合法日期"
    )
    private String birthdate;



    /** 性别（male / female） */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "ENUM('male','female')")
    private Gender gender;

    /** 联系电话（允许 +86-12345678901 或 11 位手机号） */
    @Column(name = "phonenumber", length = 45)
//    @Pattern(
//        regexp = "^(\\+?\\d{1,4}[- ]?)?\\d{5,20}$",
//        message = "电话号码格式不正确"
//    )
    private String phonenumber;

    /** 对应就诊医生（可为空，45 字符以内） */
    @Column(name = "doc", length = 45)
    @Size(max = 45, message = "医生字段长度不能超过 45 字符")
    private String doc;

    /* ---------- 枚举 ---------- */

    public enum IdType {
        @JsonProperty("passport")
        passport,
        @JsonProperty("idCard")
        idCard
    }

    public enum Gender {
        male, female
    }

    /* ---------- 业务构造器 ---------- */
    public Patient(String username,
                   IdType idtype,
                   String realname,
                   String birthdate,
                   Gender gender,
                   String phonenumber,
                   String doc) {
        this.username    = username;
        this.idtype      = idtype;
        this.realname    = realname;
        this.birthdate   = birthdate;
        this.gender      = gender;
        this.phonenumber = phonenumber;
        this.doc         = doc;
    }

    public Patient(String username,
                   IdType idtype,
                   String realname,
                   String birthdate,
                   Gender gender,
                   String phonenumber) {
        this.username    = username;
        this.idtype      = idtype;
        this.realname    = realname;
        this.birthdate   = birthdate;
        this.gender      = gender;
        this.phonenumber = phonenumber;
    }

    @Override
    public String toString() {
        return "Patient{" +
               "username='"   + username   + '\'' +
               ", idtype="    + idtype     + 
                ", id_number='" + idnumber + '\'' +
                ", realname='" + realname   + '\'' +
               ", birthdate='" + birthdate + '\'' +
               ", gender="    + gender     +
               ", phonenumber='"+ phonenumber + '\'' +
               ", doc='"      + doc        + '\'' +
               '}';
    }
}

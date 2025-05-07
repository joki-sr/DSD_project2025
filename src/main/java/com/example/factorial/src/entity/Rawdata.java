package com.example.factorial.src.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 对应表：rawdata
 */
@Entity
@Table(name = "rawdata")
@Getter @Setter @NoArgsConstructor
public class Rawdata {

    /** 由 (time|username) 计算的 SHA-256 主键，数据库自动生成 */
    @Id
    @Column(name = "hash_PK", length = 64, nullable = false, updatable = false, insertable = false)
    private String hashPK;

    /** 记录时间 */
    @Column(name = "time")
    @PastOrPresent(message = "时间不能在未来")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    /** 上传者用户名 */
    @Column(name = "username", length = 255)
    @Size(max = 255, message = "用户名长度不能超过 255 字符")
    private String username;

    /** 文件大小（字节） */
    @Column(name = "file_size")
    @Min(value = 0,  message = "文件大小不能为负数")
    private Integer fileSize;

    /** 文件内容（MediumText 对应 @Lob + String） */
    @Lob
    @Column(name = "file", columnDefinition = "MEDIUMTEXT")
    private String file;

    /* ---------- 业务构造器 ---------- */
    public Rawdata(LocalDateTime time,
                   String username,
                   Integer fileSize,
                   String file) {
        this.time      = time;
        this.username  = username;
        this.fileSize  = fileSize;
        this.file      = file;
    }

    @Override
    public String toString() {
        return "Rawdata{" +
               "hashPK='"   + hashPK  + '\'' +
               ", time="    + time    +
               ", username='"+ username + '\'' +
               ", fileSize="+ fileSize +
               '}';
    }
}

package com.example.factorial.src.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 对应表：format
 */
@Entity
@Table(name = "format")
@Getter @Setter @NoArgsConstructor
public class Format {

    /** 由 time|username 生成的 SHA-256 主键，数据库自动维护 */
    @Id
    @Column(name = "hash_PK", length = 64, nullable = false,
            insertable = false, updatable = false)
    private String hashPk;

    /** 上传时间 */
    @Column(name = "time")
    private LocalDateTime time;

    /** 上传用户名 */
    @Column(name = "username", length = 255)
    @Size(max = 255, message = "用户名长度不能超过 255 字符")
    private String username;

    /** 文件大小（字节） */
    @Column(name = "file_size")
    @PositiveOrZero(message = "文件大小必须为非负整数")
    private Integer fileSize;

    /** 文件内容（MediumText） */
    @Lob
    @Column(name = "file", columnDefinition = "MEDIUMTEXT")
    private String file;

    /** 业务构造器（time 可在外部设置为 LocalDateTime.now()） */
    public Format(LocalDateTime time,
                  String username,
                  Integer fileSize,
                  String file) {
        this.time     = time;
        this.username = username;
        this.fileSize = fileSize;
        this.file     = file;
    }

    @Override
    public String toString() {
        return "Format{" +
                "hashPk='"   + hashPk   + '\'' +
                ", time="    + time     +
                ", username='"+ username + '\'' +
                ", fileSize="+ fileSize +
                '}';
    }
}

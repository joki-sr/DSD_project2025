package com.example.factorial.src.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "record")
@Getter
@Setter
@NoArgsConstructor
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date time;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "raw_file_path")
    private String rawFilePath;

    @Column(name = "raw_size_kb")
    private Integer rawSizeKb;

    @Lob
    @Column(name = "raw_file", columnDefinition = "LONGBLOB")
    private byte[] rawFile;

    // 其他字段保留但可为 null，无需初始化
    @Column(name = "format_file_path")
    private String formatFilePath;

    @Column(name = "format_size")
    private Integer formatSize;

    @Lob
    @Column(name = "format_file", columnDefinition = "LONGBLOB")
    private byte[] formatFile;

    @Column(name = "report_file_path")
    private String reportFilePath;

    @Column(name = "report_size")
    private Integer reportSize;

    @Lob
    @Column(name = "report_file", columnDefinition = "LONGBLOB")
    private byte[] reportFile;

    // Getter / Setter / 构造函数等略，可用 Lombok 简化

    public Record(Date date, Date time, String username,
                  String rawFilePath, Integer rawSizeKb, byte[] rawFile) {
        this.date = date;
        this.time = time;
        this.username = username;
        this.rawSizeKb = rawSizeKb;
        this.rawFile = rawFile;
        this.formatSize = 0;
        this.formatFile = new byte[0];
        this.reportSize = 0;
        this.reportFile = new byte[0];
    }

    private String Null_var = "Null";

    private String nullableToString(Object obj) {
        return obj == null ? Null_var : obj.toString();
    }
    private String byteArrayToString(byte[] data) {
        return data == null ? Null_var : new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", date=" + nullableToString(date) +
                ", time=" + nullableToString(time) +
                ", username='" + nullableToString(username) + '\'' +

                ", rawFilePath='" + nullableToString(rawFilePath) + '\'' +
                ", rawSize(Kb)=" + nullableToString(rawSizeKb) +
                ", rawFile=" + byteArrayToString(rawFile) +

                ", formatFilePath='" + nullableToString(formatFilePath) + '\'' +
                ", formatSize(Kb)=" + nullableToString(formatSize) +
                ", formatFile=" + byteArrayToString(formatFile) +

                ", reportFilePath=" + nullableToString(reportFilePath) +
                ", reportSize(Kb)=" + nullableToString(reportSize) +
                ", reportFile=" + byteArrayToString(reportFile) +
                '}';
    }


}


//@Entity
//@Table(name = "record")
//@Getter @Setter @NoArgsConstructor
//public class Record {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "date")
//    @Temporal(TemporalType.DATE)
//    private Date date;
//
//    @Column(name = "time")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date time;
//
//    @Column(name = "username", nullable = false, length = 50)
//    private String username;
//
//    @Column(name = "raw_file_path")
//    private String rawFilePath;
//
//    @Column(name = "raw_size_kb")
//    private Integer rawSizeKb;
//
//    @Lob  // @Lob 注解会告诉 JPA：这是一个大型对象字段。
//    @Column(name = "raw_file", columnDefinition = "LONGBLOB")  // 对 MySQL，可省略 columnDefinition
//    private byte[] rawFile;
//
//    @Column(name = "format_file_path")
//    private String formatFilePath;
//
//    @Column(name = "format_size")
//    private Integer formatSize;
//
//    @Lob
//    @Column(name = "format_file", columnDefinition = "LONGBLOB")  // 对 MySQL，可省略 columnDefinition
//    private byte[] formatFile;
//
//    @Column(name = "report_file_path")
//    private String reportFilePath;
//
//    @Column(name = "report_size")
//    private Integer reportSize;
//
//    @Lob
//    @Column(name = "report_file")
//    private byte[] reportFile;
//
//    public Record(Date date, Date time, String username,
//                  Integer rawSizeKb, byte[] rawFile,
//                  Integer formatSize, byte[] formatFile,
//                  Integer reportSize, byte[] reportFile) {
//        this.date = date;
//        this.time = time;
//        this.username = username;
//        this.rawSizeKb = rawSizeKb;
//        this.rawFile = rawFile;
//        this.formatSize = formatSize;
//        this.formatFile = formatFile;
//        this.reportSize = reportSize;
//        this.reportFile = reportFile;
//    }
//
//    @Override
//    public String toString() {
//        return "Record{" +
//                "id=" + id +
//                ", date=" + date +
//                ", time=" + time +
//                ", username='" + username + '\'' +
//                ", rawSizeKb=" + rawSizeKb +
//                ", rawFile(bytes)=" + (rawFile == null ? "null" : rawFile.length + " bytes") +
//                ", formatSize=" + formatSize +
//                ", formatFile(bytes)=" + (formatFile == null ? "null" : formatFile.length + " bytes") +
//                ", reportSize=" + reportSize +
//                ", reportFile(bytes)=" + (reportFile == null ? "null" : reportFile.length + " bytes") +
//                '}';
//    }
//}

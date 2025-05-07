package com.example.factorial.src.repository;

import com.example.factorial.src.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Report 实体的数据访问层
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, String> {

    /** 根据用户名查询所有报告 */
    List<Report> findByUsername(String username);

    /** 查询指定时间区间内的报告 */
    List<Report> findByTimeBetween(LocalDateTime start, LocalDateTime end);

    /** 按文件大小范围查询报告 */
    List<Report> findByFileSizeBetween(Integer minBytes, Integer maxBytes);
}

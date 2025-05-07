package com.example.factorial.src.repository;

import com.example.factorial.src.entity.Rawdata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Rawdata 实体的数据访问层
 */
@Repository
public interface RawdataRepository extends JpaRepository<Rawdata, String> {

    /** 根据用户名查询记录 */
    List<Rawdata> findByUsername(String username);

    /** 查询指定时间段的上传记录 */
    List<Rawdata> findByTimeBetween(LocalDateTime start, LocalDateTime end);

    /** 按文件大小范围查询 */
    List<Rawdata> findByFileSizeBetween(Integer minBytes, Integer maxBytes);
}

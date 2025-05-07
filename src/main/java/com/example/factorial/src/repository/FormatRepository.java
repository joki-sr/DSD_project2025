package com.example.factorial.src.repository;

import com.example.factorial.src.entity.Format;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Format 实体的数据访问层
 */
@Repository
public interface FormatRepository extends JpaRepository<Format, String> {

    /** 按用户名分页查询格式化文件 */
    Page<Format> findByUsername(String username, Pageable pageable);
}

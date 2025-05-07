package com.example.factorial.src.repository;

import com.example.factorial.src.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Doctor 实体的数据访问层
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {

    /** 通过用户名查询医生 */
    Doctor findByUsername(String username);
}

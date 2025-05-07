package com.example.factorial.src.repository;

import com.example.factorial.src.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Patient 实体的数据访问层
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    /** 根据真实姓名模糊查询 */
    List<Patient> findByRealnameContaining(String realname);

    /** 根据就诊医生查询患者列表 */
    List<Patient> findByDoc(String doc);

    Patient findByUsername(String username);
}

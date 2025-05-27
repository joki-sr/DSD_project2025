package com.example.factorial.src.repository;

import com.example.factorial.src.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Doctor 实体的数据访问层
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {

    /** 通过用户名查询医生 */
    Doctor findByUsername(String username);

    List<Doctor> findByHospital(String hospital);

    List<Doctor> findByDocname(String docname);

    List<Doctor> findByPhonenumber(String phone);

    List<Doctor> findByDocnameOrPhonenumber(@Size(max = 45, message = "医生姓名长度不能超过 45 字符") String docname, @Pattern(regexp = "^\\+?\\d{1,45}$", message = "电话号码格式不正确") String phonenumber);

    void deleteByUsername(@NotBlank(message = "用户名不能为空") @Size(max = 255, message = "用户名长度不能超过 255 字符") String username);


    List<Doctor> findByDepartment(@Size(max = 45, message = "科室名称长度不能超过 45 字符") String department);
}

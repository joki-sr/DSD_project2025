package com.example.factorial.src;

import com.example.factorial.src.entity.Doctor;
import com.example.factorial.src.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class DatabaseTestDoctor {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    @Transactional
    public void testDoctorCRUD() {
        // 创建一个医生对象
        Doctor doctor = new Doctor(
                "DOC0001",               // username
                "张三",                 // docname
                "协和医院",             // hospital
                "神经内科",             // department
                "+8613912345678"       // phonenumber
        );

        // 保存医生
        Doctor savedDoctor = doctorRepository.save(doctor);
        System.out.println("保存后的医生: " + savedDoctor);

        // 查询医生
        Doctor foundDoctor = doctorRepository.findByUsername("dr001");
        System.out.println("查询到的医生: " + foundDoctor);

        // 查询所有医生
        System.out.println("所有医生列表：");
        List<Doctor> allDoctors = doctorRepository.findAll();
        allDoctors.forEach(System.out::println);

        // 更新医生信息
        foundDoctor.setPhonenumber("+8613987654321");
        Doctor updatedDoctor = doctorRepository.save(foundDoctor);
        System.out.println("更新后的电话: " + updatedDoctor.getPhonenumber());

        // 删除医生
        doctorRepository.delete(updatedDoctor);
        Doctor deletedDoctor = doctorRepository.findByUsername("dr001");
        System.out.println("删除后查询结果: " + deletedDoctor);
    }
}

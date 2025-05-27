package com.example.factorial.src;

import com.example.factorial.src.entity.Doctor;
import com.example.factorial.src.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//public class DatabaseTestDoctor {
//
//    @Autowired
//    private DoctorRepository doctorRepository;
//
//    @Test
//    @Transactional
//    public void testDoctorCRUD() {
//        // 创建一个医生对象
//        Doctor doctor = new Doctor(
//                "DOC0001",               // username
//                "张三",                 // docname
//                "协和医院",             // hospital
//                "神经内科",             // department
//                "+8613912345678"       // phonenumber
//        );
//
//        // 保存医生
//        Doctor savedDoctor = doctorRepository.save(doctor);
//        System.out.println("保存后的医生: " + savedDoctor);
//
//        // 查询医生
//        Doctor foundDoctor = doctorRepository.findByUsername("dr001");
//        System.out.println("查询到的医生: " + foundDoctor);
//
//        // 查询所有医生
//        System.out.println("所有医生列表：");
//        List<Doctor> allDoctors = doctorRepository.findAll();
//        allDoctors.forEach(System.out::println);
//
//        // 更新医生信息
//        foundDoctor.setPhonenumber("+8613987654321");
//        Doctor updatedDoctor = doctorRepository.save(foundDoctor);
//        System.out.println("更新后的电话: " + updatedDoctor.getPhonenumber());
//
//        // 删除医生
//        doctorRepository.delete(updatedDoctor);
//        Doctor deletedDoctor = doctorRepository.findByUsername("dr001");
//        System.out.println("删除后查询结果: " + deletedDoctor);
//    }
//}
@SpringBootTest
public class DatabaseTestDoctor {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    @Transactional
    public void testDoctorCRUD() {

        // Step 0: 查询所有医生
        List<Doctor> allDoctors1 = doctorRepository.findAll();
        assertFalse(allDoctors1.isEmpty(), "医生列表不应为空");
        System.out.println("插入前");
        allDoctors1.forEach(System.out::println);

        // Step 1: 创建并保存医生
        Doctor doctor = new Doctor(
                "dr001",
                "张三",
                "协和医院",
                "神经内科",
                "+8613800000000"
        );
        doctorRepository.save(doctor);

        // Step 2: 通过 ID 查询
        Optional<Doctor> foundOpt = doctorRepository.findById("dr001");
        assertTrue(foundOpt.isPresent(), "应该能找到该医生");
        Doctor found = foundOpt.get();
        assertEquals("张三", found.getDocname());

        List<Doctor> doctor_zhangsan = doctorRepository.findByDocname("张三");
        List<Doctor> doctor_phone = doctorRepository.findByPhonenumber("13321506673");
        List<Doctor> doctor_name_or_phone = doctorRepository.findByDocnameOrPhonenumber("张三", "13321506673");
        List<Doctor> doctor_hospital = doctorRepository.findByHospital("协和医院");
        List<Doctor> doctor_dept = doctorRepository.findByDepartment("神经内科");
        Doctor founddr001 = doctorRepository.findByUsername("dr001");

        // Step 3: 查询所有医生
        List<Doctor> allDoctors = doctorRepository.findAll();
        assertFalse(allDoctors.isEmpty(), "医生列表不应为空");
        allDoctors.forEach(System.out::println);

        // Step 4: 查询某医院的医生
        List<Doctor> hospitalDoctors = doctorRepository.findByHospital("协和医院");
        assertTrue(hospitalDoctors.stream().anyMatch(d -> d.getUsername().equals("dr001")));

        // Step 5: 修改信息后保存
        found.setPhonenumber("+9999900000000");
        doctorRepository.save(found);
        // Step 0: 查询所有医生
        List<Doctor> allDoctors2 = doctorRepository.findAll();
        assertFalse(allDoctors2.isEmpty(), "医生列表不应为空");
        System.out.println("插入并update后");
        allDoctors2.forEach(System.out::println);

        Doctor updated = doctorRepository.findById("dr001")
                .orElseThrow();
        assertEquals("+9999900000000", updated.getPhonenumber());

        // Step 6: 删除医生
//        doctorRepository.deleteById("dr001");
        doctorRepository.deleteByUsername("dr001");

        Optional<Doctor> deleted = doctorRepository.findById("dr001");
        assertFalse(deleted.isPresent(), "医生应已被删除");



        // Step 0: 查询所有医生
        List<Doctor> allDoctors3 = doctorRepository.findAll();
        assertFalse(allDoctors3.isEmpty(), "医生列表不应为空");
        System.out.println("删除后");
        allDoctors3.forEach(System.out::println);
    }
}

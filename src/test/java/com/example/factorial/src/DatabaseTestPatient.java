package com.example.factorial.src;

import com.example.factorial.src.entity.Patient;
import com.example.factorial.src.entity.Patient.Gender;
import com.example.factorial.src.entity.Patient.IdType;
import com.example.factorial.src.repository.PatientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class DatabaseTestPatient {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    @Transactional
    public void testPatientCRUD() {
        // 创建一个测试患者
        Patient patient = new Patient(
                "test_user_001",
                Patient.IdType.idCard,
                "张三",
                "1995",
                Patient.Gender.male,
                "+8613712345678",
                "test_doctor"
        );

        // ====== 1. 测试保存 save() ======
        Patient saved = patientRepository.save(patient);
        Assertions.assertNotNull(saved);
        Assertions.assertEquals("张三", saved.getRealname());

        // ====== 2. 测试 findById() ======
        String username = "test_user_001";
        Patient foundById = patientRepository.findById("test_user_001")
                .orElseThrow(() -> new RuntimeException("patient with username "+username+ "not found"));
        Assertions.assertNotNull(foundById);
        Assertions.assertEquals("张三", foundById.getRealname());

        // findByIdType (passport / idCard)
        List<Patient> patient_chinese = patientRepository.findByIdType(Patient.IdType.idCard);
        //findByRealname
        List<Patient> patient_hanfei = patientRepository.findByRealname("韩非");
        List<Patient> patient_birthyear = patientRepository.findByBirthyear("1990");
        List<Patient> patient_male =  patientRepository.findByGender(Gender.male);
        Patient patient_phone = patientRepository.findByPhonenumber("13321506673");
        List<Patient> patient_by_doctor = patientRepository.findByDoc("DOC0000");


        // ====== 3. 模拟其他字段的查找 ======

        // 由于没有idNumber/userId字段，请你根据实体调整实现（这里是虚构的字段）
        // 这里假设 username 就是 idNumber
//        boolean exists = patientRepository.existsByIdNumber("test_user_001");
//        Assertions.assertTrue(exists);

        Patient foundByPhone = patientRepository.findByPhonenumber("+8613712345678");
        Assertions.assertNotNull(foundByPhone);
        Assertions.assertEquals("张三", foundByPhone.getRealname());

        Patient foundByName = patientRepository.findByUsername("张三");
        Assertions.assertNotNull(foundByName);
        Assertions.assertEquals("test_user_001", foundByName.getUsername());

        // ====== 4. findAll() ======
        List<Patient> all = patientRepository.findAll();
        Assertions.assertFalse(all.isEmpty());

        // ====== 5. deleteById() ======
        patientRepository.deleteById("test_user_001");
        Patient afterDelete = patientRepository.findById("test_user_001")
                .orElseThrow(() -> new RuntimeException("patient with username "+username+ "not found"));
        Assertions.assertNull(afterDelete);
    }
}

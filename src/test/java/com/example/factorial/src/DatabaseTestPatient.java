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
                "1995-11-22",
                Patient.Gender.male,
                "+8613712345678",
                "test_doctor"
        );

        Patient patient2 = new Patient(
                "test_user_002",
                Patient.IdType.idCard,
                "李三",
                "2000-11-22",
                Patient.Gender.male,
                "+8613712345678"
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
        List<Patient> patient_chinese = patientRepository.findByIdtype(Patient.IdType.idCard);
//        Patient pt_123 = patientRepository.findById

        //findByRealname
        List<Patient> patient_hanfei = patientRepository.findByRealname("韩非");
        List<Patient> patient_birthyear = patientRepository.findByBirthdate("1990-11-11");
        List<Patient> patient_male =  patientRepository.findByGender(Gender.male);
        Patient patient_phone = patientRepository.findByPhonenumber("13321506673");
        List<Patient> patient_by_doctor = patientRepository.findByDoc("DOC0000");


        // ====== 3. 模拟其他字段的查找 ======

        Patient foundByPhone = patientRepository.findByPhonenumber("+8613712345678");
        Assertions.assertNotNull(foundByPhone);
        Assertions.assertEquals("张三", foundByPhone.getRealname());

        Patient foundByName = patientRepository.findByUsername("test_user_001");
        if(foundByName != null) {
            System.out.println("foundByUsername:"+foundByName);
        }else{
            System.out.println("foundByName test_user_001 is null");
        }

        //模糊
        List<Patient> patient_san = patientRepository.findByRealnameContaining("三");
        System.out.println("模糊查找xx三xx：" + patient_san);

        // ====== 4. findAll() ======
        List<Patient> all = patientRepository.findAll();
        Assertions.assertFalse(all.isEmpty());

        // ====== 5. deleteById() ======
        try {
            patientRepository.deleteById("test_user_001");
            Patient afterDelete = patientRepository.findById("test_user_001")
                    .orElseThrow(() -> new RuntimeException("patient with username " + username + "not found"));
        }catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

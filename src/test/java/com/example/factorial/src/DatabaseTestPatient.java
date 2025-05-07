package com.example.factorial.src;

import com.example.factorial.src.entity.Patient;
import com.example.factorial.src.entity.Patient.Gender;
import com.example.factorial.src.entity.Patient.IdType;
import com.example.factorial.src.repository.PatientRepository;
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
        Patient patient = new Patient("patient1", IdType.idCard, "张三", "1995", Gender.male, "+8613800138000", "doctorA");
        Patient saved = patientRepository.save(patient);
        System.out.println("保存的病人: " + saved);

        Patient found = patientRepository.findByUsername("patient1");
        System.out.println("查询到的病人: " + found);

        System.out.println("查询所有病人: ");
        List<Patient> all = patientRepository.findAll();
        all.forEach(System.out::println);
        patientRepository.delete(found);
        System.out.println("删除后查询结果: " + patientRepository.findByUsername("patient1"));
    }
}

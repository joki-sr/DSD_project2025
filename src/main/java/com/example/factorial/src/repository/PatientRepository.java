package com.example.factorial.src.repository;

import com.example.factorial.src.entity.Patient;
import com.example.factorial.src.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPatientId(String patientId);
    Optional<Patient> findByIdNumber(String idNumber);
    Optional<Patient> findByPhone(String phone);
    Optional<Patient> findByUser(User user);
    boolean existsByPatientId(String patientId);
    boolean existsByIdNumber(String idNumber);
    boolean existsByPhone(String phone);
    List<Patient> findByNameContainingIgnoreCase(String name);
} 
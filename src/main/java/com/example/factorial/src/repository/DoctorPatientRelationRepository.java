package com.example.factorial.src.repository;

import com.example.factorial.src.entity.Doctor;
import com.example.factorial.src.entity.DoctorPatientRelation;
import com.example.factorial.src.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorPatientRelationRepository extends JpaRepository<DoctorPatientRelation, Long> {
    List<DoctorPatientRelation> findByDoctor(Doctor doctor);
    List<DoctorPatientRelation> findByPatient(Patient patient);
    List<DoctorPatientRelation> findByDoctorAndStatus(Doctor doctor, String status);
    List<DoctorPatientRelation> findByPatientAndStatus(Patient patient, String status);
    Optional<DoctorPatientRelation> findByDoctorAndPatient(Doctor doctor, Patient patient);
    Optional<DoctorPatientRelation> findByDoctorAndPatientAndStatus(Doctor doctor, Patient patient, String status);
    boolean existsByDoctorAndPatientAndStatus(Doctor doctor, Patient patient, String status);
} 
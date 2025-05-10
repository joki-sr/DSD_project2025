package com.example.factorial.src.repository;

import com.example.factorial.src.entity.Doctor;
import com.example.factorial.src.entity.Patient;
import com.example.factorial.src.entity.PatientReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientReportRepository extends JpaRepository<PatientReport, Long> {
    Optional<PatientReport> findByReportId(String reportId);
    List<PatientReport> findByPatient(Patient patient);
    List<PatientReport> findByDoctor(Doctor doctor);
    List<PatientReport> findByPatientAndReportType(Patient patient, String reportType);
    List<PatientReport> findByPatientAndReportDateBetween(Patient patient, LocalDateTime startDate, LocalDateTime endDate);
    List<PatientReport> findByPatientAndDoctorAndReportDateBetween(Patient patient, Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);
    boolean existsByReportId(String reportId);
} 
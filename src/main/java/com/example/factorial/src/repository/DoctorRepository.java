package com.example.factorial.src.repository;

import com.example.factorial.src.entity.Doctor;
import com.example.factorial.src.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByDoctorId(String doctorId);
    Optional<Doctor> findByPhone(String phone);
    Optional<Doctor> findByUser(User user);
    boolean existsByDoctorId(String doctorId);
    boolean existsByPhone(String phone);
    List<Doctor> findByNameContainingIgnoreCase(String name);
    List<Doctor> findByDepartment(String department);
    List<Doctor> findByTitle(String title);
} 
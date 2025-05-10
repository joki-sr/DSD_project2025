package com.example.factorial.src.repository;

import com.example.factorial.src.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByPhone(String phone);
    boolean existsByPhone(String phone);
    boolean existsByPhoneOrUsername(String phone, String username);
} 
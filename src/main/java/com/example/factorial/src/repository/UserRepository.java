package com.example.factorial.src.repository;

import com.example.factorial.src.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByuName(String uName);
} 
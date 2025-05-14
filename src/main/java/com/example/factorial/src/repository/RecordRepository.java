package com.example.factorial.src.repository;

import com.example.factorial.src.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findByUsername(String username);

    Optional<Record> findByUsernameAndTime(String username, Date time_seconds);

    List<Record> date(Date date);

    List<Record> findByDate(Date date);

    Optional<Record> findByUsernameAndDate(String username, Date date);
}

package com.example.factorial.src;

import com.example.factorial.src.entity.Rawdata;
import com.example.factorial.src.repository.RawdataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class DatabaseTestRawdata {

    @Autowired
    private RawdataRepository rawdataRepository;

    @Test
    @Transactional
    public void testRawdataCRUD() {
        Rawdata rawdata = new Rawdata(LocalDateTime.now(), "sensoruser", 2048, "raw data content");
        Rawdata saved = rawdataRepository.save(rawdata);
        System.out.println("保存的 rawdata: " + saved);

        List<Rawdata> all = rawdataRepository.findAll();
        all.forEach(System.out::println);

        rawdataRepository.delete(saved);
        System.out.println("删除后记录是否存在: " + rawdataRepository.findById(saved.getHashPK()).orElse(null));
    }
}


package com.example.factorial.src;

import com.example.factorial.src.entity.Format;
import com.example.factorial.src.repository.FormatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class DatabaseTestFormat {

    @Autowired
    private FormatRepository formatRepository;

    @Test
    @Transactional
    public void testFormatCRUD() {
        Format format = new Format(LocalDateTime.now(), "testuser", 1024, "sample file content");
        Format saved = formatRepository.save(format);
        System.out.println("保存后的 format: " + saved);

        List<Format> all = formatRepository.findAll();
        all.forEach(System.out::println);

        formatRepository.delete(saved);
        System.out.println("删除后记录是否存在: " + formatRepository.findById(saved.getHashPk()).orElse(null));
    }
}

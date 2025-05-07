package com.example.factorial.src;

import com.example.factorial.src.entity.Record;
import com.example.factorial.src.repository.RecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@SpringBootTest
public class DatabaseTestRecord {

    @Autowired
    private RecordRepository recordRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testInsertCsvRawFile() throws IOException {
        // 1. 获取当前时间
        Date now = new Date();

        // 2. 加载 CSV 文件内容
        String csvPath = "D:\\development\\DSD\\DSD_project2025\\src\\test\\java\\com\\example\\factorial\\src\\imu_data_2025-04-17-06-17-43.csv";  // 你可以修改为你的 CSV 路径
        byte[] rawContent = Files.readAllBytes(Paths.get(csvPath));
        int sizeKb = rawContent.length / 1024;

        // 3. 创建并设置 Record
        Record record = new Record();
        record.setDate(now);
        record.setTime(now);
        record.setUsername("csvuser001");

        record.setRawFilePath(csvPath);
        record.setRawFile(rawContent);
        record.setRawSizeKb(sizeKb);

        // 4. 保存
        Record saved = recordRepository.save(record);
        System.out.println("已插入记录 ID: " + saved.getId());
    }
}

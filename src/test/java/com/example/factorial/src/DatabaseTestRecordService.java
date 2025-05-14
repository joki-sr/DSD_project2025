package com.example.factorial.src;

import com.example.factorial.src.entity.Record;
import com.example.factorial.src.service.RecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DatabaseTestRecordService {

    @Autowired
    private RecordService recordService;

    @Test
    @Transactional
    @Rollback(false)
    public void testInsertTwoCsvRecords() throws IOException, ParseException, InterruptedException {
        String username = "patient001";
        String csvPath1 = "D:\\development\\DSD\\DSD_project2025\\src\\main\\java\\com\\example\\factorial\\src\\dataProcess\\raw\\2025-04-17-06-17-43.1.csv";
        String csvPath2 = "D:\\development\\DSD\\DSD_project2025\\src\\main\\java\\com\\example\\factorial\\src\\dataProcess\\raw\\2025-04-17-06-17-43.2.csv";

        System.out.println("recordService = " + recordService);
        assertNotNull(recordService);

        List<Record> records = recordService.insertTwoCsvRecords(username, csvPath1, csvPath2);

        if (records == null) {
            System.out.println("debug1: records 是 null");
        } else {
            for (Record record : records) {
                if (record == null) {
                    System.out.println("debug2 record 是 null");
                } else {
                    System.out.println("debug3 插入成功：\n" + record.toString());
                }
            }
        }

//        for (Record record : records) {
//            System.out.println("插入成功：\n" + record.toString());
//        }
    }
}

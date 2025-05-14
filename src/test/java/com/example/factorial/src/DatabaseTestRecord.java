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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class DatabaseTestRecord {

    @Autowired
    private RecordRepository recordRepository;

//    public void insert2Record() throws IOException, ParseException {
//        // 1.获取日期
//        // 匹配形如 2025-04-17-06-17-43 的部分
//        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2})");
//        Matcher matcher = pattern.matcher(csvPath1);
//
//        if (!matcher.find()) {
//            System.out.println("未找到匹配的日期格式！");
//            return false;
//        } else {
//            String dateStr = matcher.group(1);
//            System.out.println("提取的字符串日期: " + dateStr);
//
//            // 将字符串转换为 Date 对象
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//            Date date = sdf.parse(dateStr);
//            System.out.println("转换后的 Date 对象: " + date);
//        }
//
//        // 2. 获取文件内容
//        byte[] rawContent1 = Files.readAllBytes(Paths.get(csvPath1));
//        int sizeKb = rawContent1.length / 1024;
//
//        // 3. 创建并设置 Record
//        Record record = new Record();
//        record.setDate(date);
//        record.setTime(now);
//        record.setUsername("csvuser001");
//
//        record.setRawFilePath(csvPath);
//        record.setRawFile(rawContent);
//        record.setRawSizeKb(sizeKb);
//
//        // 4. 保存
//        Record saved = recordRepository.save(record);
//        System.out.println("已插入记录 ID: " + saved.getId());
//    }

    @Test
    @Transactional
    @Rollback(false)
    public void testInsertCsvRawFile() throws IOException, ParseException {

        String csvPath1 = "D:\\development\\DSD\\DSD_project2025\\src\\test\\java\\com\\example\\factorial\\src\\2025-04-17-06-17-43.1.csv";  // 你可以修改为你的 CSV 路径
        String csvPath2 = "D:\\development\\DSD\\DSD_project2025\\src\\test\\java\\com\\example\\factorial\\src\\2025-04-17-06-17-43.2.csv";  // 你可以修改为你的 CSV 路径

        // 1. 从文件名获取日期

        // 匹配形如  YYYY-MM-DD-HH-mm-ss的部分（如2025-04-17-06-17-43）
        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2})");
        Matcher matcher = pattern.matcher(csvPath1);

        Date date;
        if (!matcher.find()) {
            System.out.println("未找到匹配的日期格式！");
            return;
        } else {
            String dateStr = matcher.group(1);
            System.out.println("提取的字符串日期: " + dateStr);

            // 将字符串转换为 Date 对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            date = sdf.parse(dateStr);
            System.out.println("转换后的 Date 对象: " + date);
        }


        // 2. 加载 CSV 文件内容
        byte[] rawContent1 = Files.readAllBytes(Paths.get(csvPath1));
        int sizeKb1 = rawContent1.length / 1024;

        // 3. 创建并设置 Record
        Record record = new Record();
        record.setDate(date);
        record.setTime(date);
        record.setUsername("patient001");

        record.setRawFilePath(csvPath1);
        record.setRawFile(rawContent1);
        record.setRawSizeKb(sizeKb1);

        // 4. 保存
        Record saved = recordRepository.save(record);
        System.out.println("已插入记录" + saved.toString());

        //--------------------------------存入第二个csv-----------------------

        // 2. 加载 CSV 文件内容
        byte[] rawContent2 = Files.readAllBytes(Paths.get(csvPath2));
        int sizeKb2 = rawContent2.length / 1024;

        // 3. 创建并设置 Record
        Record record2 = new Record();
        record2.setDate(date);
        record2.setTime(date);
        record2.setUsername("patient001");

        record2.setRawFilePath(csvPath2);
        record2.setRawFile(rawContent2);
        record2.setRawSizeKb(sizeKb2);

        // 4. 保存
        Record saved2 = recordRepository.save(record2);

        System.out.println("已插入记录" + saved2.toString());
    }
}

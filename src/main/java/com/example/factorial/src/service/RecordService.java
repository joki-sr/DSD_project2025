package com.example.factorial.src.service;
import com.example.factorial.src.dataProcess.DataManager;
import com.example.factorial.src.entity.Record;
import com.example.factorial.src.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    public List<Record> insertTwoCsvRecords(String username, String csvPath1, String csvPath2) throws IOException, ParseException, InterruptedException {

//        System.out.println("cccc");
        DataManager dataMgr = DataManager.getInstance();

//        System.out.println("dddd");

        // 提取两个路径中的时间戳
        String ts1 = extractTimestamp(csvPath1);
        String ts2 = extractTimestamp(csvPath2);

        if (!ts1.equals(ts2)) {
            throw new IllegalArgumentException("两个CSV文件的时间戳不一致！");
        }
        System.out.println("eeee");


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        java.util.Date utilDate = sdf.parse(ts1);
        java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());

        //------------------------第一个record------------------------
        // clean第一个csv
        String csvCleanedPath1 = dataMgr.rawToCleaned(csvPath1);

        // 读取第一个 CSV
        byte[] content1 = Files.readAllBytes(Paths.get(csvPath1));
        // 读取第一个clean data
        byte[] cleanedContent1 = Files.readAllBytes(Paths.get(csvCleanedPath1));


        Record record1 = new Record();
        record1.setDate(timestamp);
        record1.setTime(timestamp);
        record1.setUsername(username);
        //csv raw
        record1.setRawFilePath(csvPath1);
        record1.setRawFile(content1);
        record1.setRawSizeKb(content1.length / 1024);
        //csv cleaned
        record1.setFormatFilePath(csvCleanedPath1);
        record1.setFormatSize(cleanedContent1.length / 1024);
        record1.setFormatFile(cleanedContent1);

        // save
        Record saved1 = recordRepository.save(record1);

        //------------------------第一个record end------------------------

        //------------------------第二个record------------------------
        // clean第2个csv
        String csvCleanedPath2 = dataMgr.rawToCleaned(csvPath2);
        // 读取第2个 CSV
        byte[] content2 = Files.readAllBytes(Paths.get(csvPath2));
        // 读取第2个clean data
        byte[] cleanedContent2 = Files.readAllBytes(Paths.get(csvCleanedPath2));

        Record record2 = new Record();
        record2.setDate(timestamp);
        record2.setTime(timestamp);
        record2.setUsername(username);
        //raw
        record2.setRawFilePath(csvPath2);
        record2.setRawFile(content2);
        record2.setRawSizeKb(content2.length / 1024);
        //cleaned
        record2.setFormatFilePath(csvCleanedPath2);
        record2.setFormatSize(cleanedContent2.length / 1024);
        record2.setFormatFile(cleanedContent2);
        // save
        Record saved2 = recordRepository.save(record2);
        //------------------------第二个record end------------------------

        return Arrays.asList(saved1, saved2);
    }

    private String extractTimestamp(String path) {
        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2})");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new IllegalArgumentException("CSV 文件路径不包含有效的时间戳: " + path);
        }
    }
}

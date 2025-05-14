package com.example.factorial.src;

import com.example.factorial.src.entity.Record;
import com.example.factorial.src.repository.RecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DatabaseTestRecordCRUD {

    @Autowired
    private RecordRepository recordRepository;

    @Test
    public void testCreateAndReadRecord() {
        Date now = new Date();
        byte[] rawData = "hello raw".getBytes(StandardCharsets.UTF_8);

        Record record = new Record(now, now, "test_user", "path/to/raw", 123, rawData);
        Record saved = recordRepository.save(record);

        assertThat(saved.getId()).isNotNull();
        Optional<Record> found = recordRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("test_user");
    }

    @Test
    public void testUpdateRecord() {
        Date now = new Date();
        Record record = new Record(now, now, "update_user", "raw/path", 100, "data".getBytes());
        Record saved = recordRepository.save(record);

        saved.setRawSizeKb(456);
        saved.setFormatFilePath("new/path");
        recordRepository.save(saved);

        Optional<Record> updated = recordRepository.findById(saved.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getRawSizeKb()).isEqualTo(456);
        assertThat(updated.get().getFormatFilePath()).isEqualTo("new/path");
    }

    @Test
    public void testDeleteRecord() {
        Date now = new Date();
        Record record = new Record(now, now, "delete_user", "del/path", 10, "bytes".getBytes());
        Record saved = recordRepository.save(record);
        Long id = saved.getId();

        recordRepository.deleteById(id);
        assertThat(recordRepository.findById(id)).isNotPresent();
    }

    @Test
    public void testFindByUsernameAndTime() {
        Date now = new Date();
        String username = "find_user";

        Record record = new Record(now, now, username, "some/path", 12, "abc".getBytes());
        recordRepository.save(record);

        // 假设你定义了 findByUsernameAndTime(String username, Date time)
        Optional<Record> found1 = recordRepository.findByUsernameAndTime(username, now);
        assertThat(found1).isPresent();
        assertThat(found1.get().getUsername()).isEqualTo(username);

        Optional<Record> found2 = recordRepository.findByUsernameAndDate(username, now);


        List<Record> foundRecords = recordRepository.findByUsername(username);
        List<Record> foundTime = recordRepository.findByDate(new Date());
    }
}

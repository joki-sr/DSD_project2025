package com.example.factorial.src;

import com.example.factorial.src.entity.User;
import com.example.factorial.src.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class DatabasePerformanceTest {

    @Autowired
    private UserRepository userRepository;

    /**
     * 性能测试入口方法：运行所有子测试
     */
    @Test
    public void runAllPerformanceTests() throws InterruptedException {
        System.out.println("\n====== 性能测试（单次查找） ======");
        testSingleQuery();

        System.out.println("\n====== 性能测试（批量查找1000） ======");
        testBatchQuery();

        System.out.println("\n====== 性能测试（100线程并发查找） ======");
        testConcurrentAccess();
    }

    /**
     * 测试：查找一个用户所需时间
     */
    private void testSingleQuery() {
        String username = "admin"; // 确保这个用户名存在
        long start = System.currentTimeMillis();
        User user = userRepository.findByUsername(username);
        long end = System.currentTimeMillis();
        if (user != null) {
            System.out.println("查找用户 " + username + " 成功，耗时: " + (end - start) + " ms");
        } else {
            System.out.println("未找到用户: " + username);
        }
    }

    /**
     * 测试：查找前1000个用户的耗时（要求数据库中已有1000条数据）
     */
    private void testBatchQuery() {
        long start = System.currentTimeMillis();
        List<User> users = userRepository.findAll();
        long end = System.currentTimeMillis();
        int count = Math.min(users.size(), 1000);
        System.out.println("共查得用户: " + users.size() + " 条，前 " + count + " 条耗时: " + (end - start) + " ms");
    }

    /**
     * 测试：100 个线程并发查找同一用户名
     */
    private void testConcurrentAccess() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        String username = "admin"; // 确保该用户存在

        long start = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    User user = userRepository.findByUsername(username);
                    if (user == null) {
                        System.out.println("线程未查到用户");
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long end = System.currentTimeMillis();
        System.out.println("100个线程并发访问查找用户 " + username + " 耗时: " + (end - start) + " ms");

        executor.shutdown();
    }
}

package com.example.factorial.src;

import com.example.factorial.src.repository.UserRepository;
import com.example.factorial.src.entity.User; // 确保这个是你的 User 类路径
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class DatabaseTest {

    @Autowired
    private UserRepository userRepository; // ✅ 保证是 private 成员变量 + 注入

    @Test
    @Transactional
    public void testUserCRUD() {
        // 创建测试用户
        User user = new User("testuser", "password123");

        // 保存用户
        User savedUser = userRepository.save(user);
        System.out.println("保存后的用户ID: " + savedUser.getId());

        // 查询用户
        User foundUser = userRepository.findByUsername("testuser");
        System.out.println("查询到的用户: " + foundUser.toString());
        
        // 查询所有用户
        System.out.println("查询所有的用户: ");
        List<User> all = userRepository.findAll();
        all.forEach(System.out::println);

        // 更新密码
        foundUser.setPassword("newpassword");
        User updatedUser = userRepository.save(foundUser);
        System.out.println("更新后的密码: " + updatedUser.getPassword());

        // 删除用户
        userRepository.delete(updatedUser);
        User deletedUser = userRepository.findByUsername("testuser");
        System.out.println("删除后查询结果: " + deletedUser);
    }
}

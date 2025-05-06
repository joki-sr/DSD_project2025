package com.example.factorial.src;

import com.example.factorial.src.repository.UserRepository;
import com.example.factorial.src.entity.User; // 根据你的实际路径修改
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class TestUserCRUD {

    @Autowired
    private UserRepository userRepository;

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
        System.out.println("查询到的用户: " + foundUser);

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

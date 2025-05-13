package com.example.factorial.src;

import com.example.factorial.src.repository.UserRepository;
import com.example.factorial.src.entity.User; // 确保这个是你的 User 类路径
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class DatabaseTestUser {

    @Autowired
    private UserRepository userRepository; // ✅ 保证是 private 成员变量 + 注入

    @Test
    @Transactional
    public void testUserCRUD() {
        // 创建测试用户
        User user = new User("testuser", "password123", User.RoleType.PATIENT);

        // 保存用户
        User savedUser = userRepository.save(user);
        System.out.println("保存后的用户ID: " + savedUser.getId());

        // 查询用户
        User foundUser = userRepository.findByUsername("testuser");
        System.out.println("查询到的用户: " + foundUser.toString());

        // 更新用户
        foundUser.setPassword("newpassword");
        foundUser.setRoletype(User.RoleType.DOCTOR);
        foundUser.setUsername("newusername");
        User updatedUser = userRepository.save(foundUser);
        System.out.println("更新后的用户: " + foundUser.toString());

        // 删除用户
//        userRepository.deleteById(updatedUser.getId());
        if (userRepository.findByUsername(updatedUser.getUsername()) != null) {
            userRepository.deleteByUsername(updatedUser.getUsername());
//              userRepository.deleteById(updatedUser.getId());

            System.out.println("deleteByUsername: " + updatedUser.getUsername());
        }else{
            System.out.println(updatedUser.getUsername() + "not found");
        }
        User deletedUser = userRepository.findByUsername("testuser");
        System.out.println("删除后查询结果: " + deletedUser);

        //findById
        User admin = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User with id 1 not found"));
        System.out.println("findById(): " + admin.toString());

        // findById 异常
        Long notExistId = 999L;
        try {
            User notExistUser = userRepository.findById(notExistId)
                    .orElseThrow(() -> new RuntimeException("User with id "+notExistId+ "not found"));
            System.out.println("findById(): " + admin.toString());
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }


        //findAll
        // 查询所有用户
        System.out.println("findByAll(): ");
        List<User> all = userRepository.findAll();
        all.forEach(System.out::println);

        // findByUsername
        boolean found = false;
        admin = userRepository.findByUsername("admin");
        System.out.println("findByUsername():"+admin.toString());

        try {
            User notExistUser = userRepository.findByUsername("notExistUser");
            System.out.println("findByUsername():" + notExistUser.toString());
        }catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // findByUserType
        System.out.println("findByRoletype(): ");
        List<User> patients = userRepository.findByRoletype(User.RoleType.PATIENT);
        patients.forEach(System.out::println);


    }
}

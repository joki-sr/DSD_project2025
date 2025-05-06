package com.example.factorial.src;

import com.example.factorial.src.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataBase extends Base {
    private final UserRepository userRepository;
    
    @Autowired
    public DataBase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public int GETDATE() {
        // 计算用户数量作为示例
        return (int) userRepository.count();
    }

    @Override
    public int GET(PermissionBase p) {
        // 这里可以根据权限检查用户访问权限
        if (p != null) {
            return 1; // 有权限
        }
        return 0; // 无权限
    }
    
    // 获取所有用户
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // 根据用户名查找用户
    public User findUserByName(String uName) {
        return userRepository.findByuName(uName);
    }
    
    // 保存用户
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

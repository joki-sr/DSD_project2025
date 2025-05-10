package com.example.factorial.src.service;

import com.example.factorial.src.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义UserDetailsService实现类，用于从数据库加载用户信息
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 先尝试通过用户名查找
        User user = userService.findByUsername(username);
        
        // 如果找不到，则尝试通过手机号查找
        if (user == null) {
            user = userService.findByPhone(username);
        }
        
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        
        // 创建用户权限列表
        List<SimpleGrantedAuthority> authorities = buildUserAuthority(user);
        
        // 返回Spring Security的UserDetails对象
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
    
    /**
     * 构建用户权限
     * @param user 用户对象
     * @return 权限列表
     */
    private List<SimpleGrantedAuthority> buildUserAuthority(User user) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // 添加用户类型角色
        if (user.getUserType() != null && !user.getUserType().isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getUserType()));
        }
        
        // 添加特定角色
        if (user.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (user.isDoctor()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_DOCTOR"));
        }
        if (user.isPatient()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_PATIENT"));
        }
        
        return authorities;
    }
} 
package com.example.factorial.src.service;

import com.example.factorial.src.dto.request.RegisterRequest;
import com.example.factorial.src.dto.response.ApiResponse;
import com.example.factorial.src.dto.response.LoginResponse;
import com.example.factorial.src.dto.response.UserResponse;
import com.example.factorial.src.entity.User;
import com.example.factorial.src.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 用户服务类，处理用户注册登录相关业务逻辑
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 处理用户登录
     * @param username 用户名/手机号
     * @param password 密码
     * @return 包含用户信息和token的响应
     */
    public ApiResponse<LoginResponse> login(String username, String password) {
        try {
            // 尝试认证用户
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            
            // 认证成功，获取用户信息
            User user = userRepository.findByPhone(username);
            if (user == null) {
                // 如果通过手机号没找到，尝试通过用户名查找
                user = userRepository.findByUsername(username);
            }
            
            if (user == null) {
                return ApiResponse.error("用户名或密码错误", 401);
            }
            
            // 生成JWT令牌
            String token = jwtService.generateToken(user);
            
            // 构建响应数据
            LoginResponse loginResponse = new LoginResponse(
                user.getId().toString(),
                user.isAdmin(),
                user.isDoctor(),
                user.isPatient(),
                user.getName(),
                token
            );
            
            return ApiResponse.success(loginResponse, "登录成功", 200);
            
        } catch (AuthenticationException e) {
            return ApiResponse.error("用户名或密码错误", 401);
        }
    }

    /**
     * 处理用户注册
     * @param registerRequest 注册请求对象
     * @return 包含用户信息的响应
     */
    @Transactional
    public ApiResponse<UserResponse> register(RegisterRequest registerRequest) {
        return register(
            registerRequest.getName(),
            registerRequest.getPhone(),
            registerRequest.getGender(),
            registerRequest.getBirthDate(),
            registerRequest.getIdType(),
            registerRequest.getIdNumber(),
            registerRequest.getPassword()
        );
    }
    
    /**
     * 处理用户注册
     * @param name 姓名
     * @param phone 手机号
     * @param gender 性别
     * @param birthDate 出生日期
     * @param idType 证件类型
     * @param idNumber 证件号码
     * @param password 密码
     * @return 包含用户信息的响应
     */
    @Transactional
    public ApiResponse<UserResponse> register(
            String name,
            String phone,
            String gender,
            String birthDate,
            String idType,
            String idNumber,
            String password) {
        
        try {
            // 验证手机号是否已存在
            if (userRepository.existsByPhone(phone)) {
                return ApiResponse.conflict("手机号已被注册");
            }
            
            // 创建用户
            User user = new User();
            user.setUsername(phone); // 使用手机号作为用户名
            user.setPassword(passwordEncoder.encode(password));
            user.setName(name);
            user.setPhone(phone);
            user.setUserType("PATIENT"); // 默认为患者类型
            user.setDoctor(false);
            user.setPatient(true);
            user.setAdmin(false);
            
            // 保存用户
            user = userRepository.save(user);
            
            // 生成患者ID
            String userId = UUID.randomUUID().toString().replace("-", "");
            
            // 构建响应数据
            UserResponse userResponse = new UserResponse(
                userId,
                name,
                phone,
                gender,
                birthDate,
                idType,
                idNumber
            );
            
            return ApiResponse.success(userResponse, "注册成功", 201);
            
        } catch (Exception e) {
            return ApiResponse.serverError();
        }
    }
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * 根据手机号查找用户
     * @param phone 手机号
     * @return 用户对象
     */
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }
    
    /**
     * 检查用户名或手机号是否已存在
     * @param phone 手机号
     * @param username 用户名
     * @return 是否存在
     */
    public boolean existsByPhoneOrUsername(String phone, String username) {
        return userRepository.existsByPhoneOrUsername(phone, username);
    }
} 
# System 组架构设计

本文档描述了 System 组需要实现的 Controller 层和 Service 层代码结构，以连接 Data 组提供的 Repository 接口和 UI 组需要的 API 接口。

## 一、整体架构层次

系统采用分层架构，从上到下依次为：

1. 过滤器层（Filter）- 请求预处理，认证授权
2. 控制器层（Controller）- 处理 HTTP 请求，参数校验
3. 服务层（Service）- 实现业务逻辑
4. 数据访问层（Repository）- 由 Data 组提供实现
5. 安全配置（Security Config）- 配置安全相关策略

## 二、过滤器层设计

过滤器层负责请求的预处理和认证授权，拦截所有请求并验证其合法性。

### 1. JwtAuthenticationFilter

**文件路径**：`src/main/java/com/example/factorial/src/filter/JwtAuthenticationFilter.java`

**职责**：拦截所有需要认证的请求，验证请求中的 JWT 令牌，设置安全上下文。

**主要方法**：

```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // 从请求头中获取Token
    String token = extractToken(request);

    // 验证Token有效性
    if (token != null && jwtService.validateToken(token)) {
        // 设置安全上下文
        SecurityContext context = establishSecurityContext(token);
        SecurityContextHolder.setContext(context);
    }

    filterChain.doFilter(request, response);
}

private String extractToken(HttpServletRequest request) {
    // 从Authorization请求头中提取Bearer token
}

private SecurityContext establishSecurityContext(String token) {
    // 根据token创建安全上下文
}
```

## 三、Controller 层设计

Controller 层负责处理 HTTP 请求，进行参数验证，并返回格式化的响应。所有 Controller 都应位于 `com.example.factorial.src.controller`包下。

### 1. UserController

**文件路径**：`src/main/java/com/example/factorial/src/controller/UserController.java`

**职责**：处理用户注册和登录相关请求。

**API 端点**：

- POST `/api/session` - 用户登录
- POST `/api/users` - 用户注册

**主要方法**：

```java
@PostMapping("/session")
public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest);

@PostMapping
public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest registerRequest);
```

### 2. AdminController

**文件路径**：`src/main/java/com/example/factorial/src/controller/AdminController.java`

**职责**：处理管理员相关的请求，包括医生管理、患者管理和医患关系管理。

**API 端点**：

- GET `/api/admin/doctors` - 获取医生列表
- POST `/api/admin/doctors` - 注册新医生
- GET `/api/admin/patients` - 获取患者列表
- GET `/api/admin/relations` - 获取医生-患者关系列表
- POST `/api/admin/relations` - 添加医生-患者关系
- PUT `/api/admin/relations` - 更新医生-患者关系
- DELETE `/api/admin/relations` - 删除医生-患者关系

**主要方法**：

```java
@GetMapping("/doctors")
public ResponseEntity<Map<String, Object>> getAllDoctors();

@PostMapping("/doctors")
public ResponseEntity<Map<String, Object>> registerDoctor(@RequestBody DoctorRegisterRequest request);

@GetMapping("/patients")
public ResponseEntity<Map<String, Object>> getAllPatients();

@GetMapping("/relations")
public ResponseEntity<Map<String, Object>> getAllDoctorPatientRelations();

@PostMapping("/relations")
public ResponseEntity<Map<String, Object>> addDoctorPatientRelation(@RequestBody RelationRequest request);

@PutMapping("/relations")
public ResponseEntity<Map<String, Object>> updateDoctorPatientRelation(@RequestBody UpdateRelationRequest request);

@DeleteMapping("/relations")
public ResponseEntity<Map<String, Object>> deleteDoctorPatientRelation(@RequestBody RelationRequest request);
```

### 3. DoctorController

**文件路径**：`src/main/java/com/example/factorial/src/controller/DoctorController.java`

**职责**：处理医生查看患者列表的请求。

**API 端点**：

- GET `/api/doctor/{doctorId}/patients` - 获取某位医生的患者列表

**主要方法**：

```java
@GetMapping("/{doctorId}/patients")
public ResponseEntity<Map<String, Object>> getDoctorPatients(@PathVariable String doctorId);
```

### 4. PatientController

**文件路径**：`src/main/java/com/example/factorial/src/controller/PatientController.java`

**职责**：处理患者报告相关的请求。

**API 端点**：

- GET `/api/patient/{patientId}/reports` - 获取指定患者的步态报告列表

**主要方法**：

```java
@GetMapping("/{patientId}/reports")
public ResponseEntity<Map<String, Object>> getPatientReports(@PathVariable String patientId);
```

### 5. UploadController

**文件路径**：`src/main/java/com/example/factorial/src/controller/UploadController.java`

**职责**：处理 CSV 文件上传和报告管理的请求。

**API 端点**：

- POST `/api/upload/csv` - 上传 IMU 数据 CSV 文件
- PUT `/api/report/{reportId}` - 更新报告信息

**主要方法**：

```java
@PostMapping("/csv")
public ResponseEntity<Map<String, Object>> uploadCSV(@RequestParam("file") MultipartFile file, @RequestParam("patientId") String patientId);

@PutMapping("/{reportId}")
public ResponseEntity<Map<String, Object>> updateReport(@PathVariable String reportId, @RequestBody ReportUpdateRequest request);
```

### 6. HomeController

**文件路径**：`src/main/java/com/example/factorial/src/controller/HomeController.java`

**职责**：提供系统基本信息和健康检查。

**API 端点**：

- GET `/` - 首页或健康检查

**主要方法**：

```java
@GetMapping("/")
public ResponseEntity<String> home();
```

## 四、Service 层设计

Service 层实现业务逻辑，包括数据验证、权限检查和调用 Repository 层方法。所有 Service 应位于 `com.example.factorial.src.service`包下。

### 1. JwtService

**文件路径**：`src/main/java/com/example/factorial/src/service/JwtService.java`

**职责**：处理 JWT 令牌的生成、验证和解析。

**主要方法**：

```java
// 生成JWT令牌
public String generateToken(User user);

// 验证令牌有效性
public boolean validateToken(String token);

// 从令牌中提取用户信息
public String extractUsername(String token);

// 从令牌中提取到期时间
public Date extractExpiration(String token);

// 检查令牌是否过期
private boolean isTokenExpired(String token);
```

### 2. UserService

**文件路径**：`src/main/java/com/example/factorial/src/service/UserService.java`

**职责**：处理用户认证和注册的业务逻辑。

**主要方法**：

```java
// 用户登录
public Map<String, Object> login(String username, String password);

// 用户注册
public Map<String, Object> register(String name, String phone, String gender, String birthDate, String idType, String idNumber, String password);

// 用户查询
public User findUserByPhone(String phone);
public boolean existsByPhone(String phone);
```

### 3. DoctorService

**文件路径**：`src/main/java/com/example/factorial/src/service/DoctorService.java`

**职责**：处理医生相关的业务逻辑。

**主要方法**：

```java
// 获取所有医生
public List<Doctor> getAllDoctors();

// 注册新医生
public Doctor registerDoctor(String password, String name, String phone, String hospital, String department);

// 医生查询
public Doctor findDoctorByPhone(String phone);
public Doctor findDoctorById(String id);
public boolean existsByNameOrPhone(String name, String phone);
```

### 4. PatientService

**文件路径**：`src/main/java/com/example/factorial/src/service/PatientService.java`

**职责**：处理患者相关的业务逻辑。

**主要方法**：

```java
// 获取所有患者
public List<Patient> getAllPatients();

// 患者查询
public Patient findPatientById(String id);
public Patient findPatientByPhone(String phone);
public Patient findPatientByIdNumber(String idNumber);
```

### 5. DoctorPatientRelationService

**文件路径**：`src/main/java/com/example/factorial/src/service/DoctorPatientRelationService.java`

**职责**：处理医生-患者关系的业务逻辑。

**主要方法**：

```java
// 获取所有医患关系
public List<DoctorPatientRelationDTO> getAllRelations();

// 获取医生的所有患者
public List<Patient> getPatientsByDoctorId(String doctorId);

// 添加医患关系
public DoctorPatientRelation addRelation(String doctorId, String patientId);

// 更新医患关系
public DoctorPatientRelation updateRelation(String oldDoctorId, String oldPatientId, String newDoctorId, String newPatientId);

// 删除医患关系
public void deleteRelation(String doctorId, String patientId);

// 检查医患关系是否存在
public boolean relationExists(String doctorId, String patientId);
```

### 6. PatientReportService

**文件路径**：`src/main/java/com/example/factorial/src/service/PatientReportService.java`

**职责**：处理患者报告相关的业务逻辑。

**主要方法**：

```java
// 获取患者的所有报告
public List<PatientReport> getReportsByPatientId(String patientId);

// 获取单个报告
public PatientReport getReportById(String reportId);

// 更新报告信息
public PatientReport updateReportTypeAndSummary(String reportId, String type, String summary);

// 创建新报告
public PatientReport createReport(String patientId, String rawDataId, ReportData reportData);
```

### 7. CSVProcessingService

**文件路径**：`src/main/java/com/example/factorial/src/service/CSVProcessingService.java`

**职责**：处理 CSV 文件上传和解析的业务逻辑。

**主要方法**：

```java
// 处理CSV文件上传
public Map<String, Object> processCSVUpload(MultipartFile file, String patientId);

// 解析CSV文件内容
private ReportData parseCSVData(String csvContent);

// 保存CSV原始数据
private CSVData saveCSVData(String patientId, String fileName, String fileContent, long fileSize);
```

### 8. AuthenticationService

**文件路径**：`src/main/java/com/example/factorial/src/service/AuthenticationService.java`

**职责**：处理用户认证和权限验证的业务逻辑。

**主要方法**：

```java
// 验证用户认证
public User authenticateUser(String token);

// 检查用户是否为管理员
public boolean isAdmin(User user);

// 检查用户是否为医生
public boolean isDoctor(User user);

// 检查用户是否为患者
public boolean isPatient(User user);

// 检查医生是否有权访问患者数据
public boolean doctorCanAccessPatient(String doctorId, String patientId);
```

## 五、安全配置

安全配置负责定义系统的安全策略和认证机制，包括 URL 访问控制、认证方式等。

### 1. SecurityConfig

**文件路径**：`src/main/java/com/example/factorial/src/config/SecurityConfig.java`

**职责**：配置 Spring Security，定义安全策略。

**主要方法**：

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
            .antMatchers("/api/session", "/api/users").permitAll()  // 登录和注册接口允许公开访问
            .antMatchers("/api/admin/**").hasRole("ADMIN")  // 管理员接口需要ADMIN角色
            .anyRequest().authenticated()  // 其他请求需要认证
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 无状态会话
        .and()
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // 添加JWT过滤器

    return http.build();
}

@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
}

@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

@Bean
public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtService, userDetailsService);
}
```

## 六、DTO 类设计

数据传输对象(DTO)用于封装请求参数和响应数据。

### 1. 请求 DTO

- `LoginRequest` - 登录请求参数
- `RegisterRequest` - 用户注册请求参数
- `DoctorRegisterRequest` - 医生注册请求参数
- `RelationRequest` - 医患关系请求参数
- `UpdateRelationRequest` - 更新医患关系请求参数
- `ReportUpdateRequest` - 更新报告请求参数

### 2. 响应 DTO

- `DoctorPatientRelationDTO` - 医患关系详情
- `ApiResponse<T>` - 通用 API 响应封装

## 七、数据验证和异常处理

每个 Controller 和 Service 方法应包含适当的参数验证和异常处理：

1. 请求参数验证
2. 业务规则验证（如身份证格式、电话号码格式）
3. 权限检查
4. 全局异常处理器捕获并统一处理异常

### 1. 全局异常处理器

**文件路径**：`src/main/java/com/example/factorial/src/exception/GlobalExceptionHandler.java`

**职责**：统一处理系统中抛出的各类异常，转换为标准 API 响应格式。

**主要方法**：

```java
@ExceptionHandler(AuthenticationException.class)
public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
    // 处理认证异常
}

@ExceptionHandler(AccessDeniedException.class)
public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
    // 处理授权异常
}

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    // 处理参数验证异常
}

@ExceptionHandler(Exception.class)
public ResponseEntity<Object> handleAllExceptions(Exception ex) {
    // 处理其他所有异常
}
```

## 八、响应格式

所有 API 响应应遵循统一的格式：

```json
{
  "status": 200,
  "data": { ... },
  "message": "success"
}
```

对于错误情况：

```json
{
  "status": 400,
  "data": null,
  "message": "错误信息"
}
```

## 九、安全性考虑

1. 使用 JWT 进行认证
   - 设置合理的令牌有效期
   - 考虑使用刷新令牌机制
   - 令牌加密和签名
2. 密码安全
   - 使用 BCrypt 等强哈希算法加密存储密码
   - 实施密码强度要求
3. CSRF 保护
   - 对于需要的接口，启用 CSRF 保护
   - 使用 CSRF 令牌验证
4. 输入验证和清理
   - 所有输入必须经过验证和清理
   - 防止 SQL 注入、XSS 等攻击
5. 权限检查
   - 基于角色的访问控制
   - 检查用户是否有权访问特定资源

## 十、依赖需求

项目需要以下关键依赖：

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT依赖 -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- Bean Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

## 十一、实现顺序建议

1. 基础认证框架

   - SecurityConfig 配置
   - JwtService 和过滤器实现
   - 全局异常处理

2. UserController 和 UserService - 实现基本的用户认证
3. AdminController 和相关 Service - 实现医生和患者管理
4. DoctorController 和 PatientController - 实现医患数据访问
5. UploadController 和 CSVProcessingService - 实现数据上传和处理

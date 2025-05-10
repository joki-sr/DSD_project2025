# Service 包说明文档

Service 包包含系统的所有业务逻辑处理服务，负责连接控制器层和数据访问层。

## 服务架构概述

在重构后，服务层采用了更清晰的职责划分和门面模式，以减少代码冗余并提高可维护性：

- **AuthService**：作为门面类，提供统一的认证和授权接口，内部调用专门的服务
- **JwtService**：专门处理 JWT 令牌的生成、解析和验证
- **UserService**：处理用户注册、登录和用户信息管理
- **AuthenticationService**：处理权限检查和当前用户信息获取
- **其他业务服务**：各自处理特定业务领域的逻辑

## AuthService

认证服务门面类，整合认证相关功能并提供统一接口。

### 主要方法

- `login(LoginRequest loginRequest)`: 处理用户登录，内部调用 UserService
- `register(RegisterRequest registerRequest)`: 处理用户注册，内部调用 UserService
- `getCurrentUser()`: 获取当前登录用户信息，内部调用 AuthenticationService
- `canAccessPatientData(String patientId)`: 检查用户是否有权访问患者数据，内部调用 AuthenticationService
- `generateToken(User user)`: 生成 JWT 令牌，内部调用 JwtService
- `validateToken(String token)`: 验证 JWT 令牌有效性，内部调用 JwtService
- `extractUsername(String token)`: 从令牌中提取用户名，内部调用 JwtService
- `extractRoles(String token)`: 从令牌中提取角色列表，内部调用 JwtService

## JwtService

JWT 服务，负责 JWT 令牌的生成、解析和验证。

### 主要方法

- `generateToken(User user)`: 生成包含用户信息的 JWT 令牌
- `generateToken(String username, Collection<? extends GrantedAuthority> authorities)`: 基于用户名和权限生成 JWT 令牌
- `validateToken(String token)`: 验证令牌有效性
- `extractUsername(String token)`: 从令牌中提取用户名
- `extractExpiration(String token)`: 从令牌中提取过期时间
- `extractRoles(String token)`: 从令牌中提取角色列表
- `extractClaim(String token, Function<Claims, T> claimsResolver)`: 从令牌中提取特定声明
- `extractAllClaims(String token)`: 从令牌中提取所有声明

## UserService

用户服务，处理用户注册、登录和用户信息管理。

### 主要方法

- `login(String username, String password)`: 处理用户登录，验证凭据并生成令牌
- `register(RegisterRequest registerRequest)`: 处理用户注册请求
- `register(String name, String phone, String gender, String birthDate, String idType, String idNumber, String password)`: 完整的用户注册方法
- `findByUsername(String username)`: 根据用户名查找用户
- `findByPhone(String phone)`: 根据手机号查找用户
- `existsByPhoneOrUsername(String phone, String username)`: 检查用户名或手机号是否已存在

## AuthenticationService

认证服务，处理权限检查和当前用户信息获取。

### 主要方法

- `getCurrentUser()`: 获取当前登录用户
- `canAccessPatientData(String patientId)`: 检查当前用户是否有权访问患者数据
- `canAccessDoctorData(String doctorId)`: 检查当前用户是否有权访问医生数据
- `relationExists(String doctorId, String patientId)`: 检查医患关系是否存在
- `isAdmin()`: 检查当前用户是否为管理员
- `isDoctor()`: 检查当前用户是否为医生
- `isPatient()`: 检查当前用户是否为患者

## CustomUserDetailsService

自定义用户详情服务，实现 Spring Security 的 UserDetailsService 接口，用于加载用户信息。

### 主要方法

- `loadUserByUsername(String username)`: 根据用户名加载用户详情，调用 UserService
- `buildUserAuthority(User user)`: 构建用户权限列表

## DoctorService

医生服务，处理医生相关业务逻辑。

### 主要方法

- `getAllDoctors()`: 获取所有医生
- `getDoctorById(String doctorId)`: 根据 ID 获取医生
- `getDoctorByUser(User user)`: 根据用户获取医生
- `createDoctor(Doctor doctor)`: 创建新医生
- `updateDoctor(Doctor doctor)`: 更新医生信息
- `deleteDoctor(String doctorId)`: 删除医生
- `getDoctorPatients(String doctorId)`: 获取医生的患者列表

## PatientService

患者服务，处理患者相关业务逻辑。

### 主要方法

- `getAllPatients()`: 获取所有患者
- `getPatientById(String patientId)`: 根据 ID 获取患者
- `getPatientByUser(User user)`: 根据用户获取患者
- `createPatient(Patient patient)`: 创建新患者
- `updatePatient(Patient patient)`: 更新患者信息
- `deletePatient(String patientId)`: 删除患者
- `getPatientDoctors(String patientId)`: 获取患者的医生列表

## DoctorPatientRelationService

医患关系服务，处理医生和患者之间的关系管理。

### 主要方法

- `assignDoctorToPatient(String doctorId, String patientId, String relationType)`: 建立医患关系
- `removeDoctorFromPatient(String doctorId, String patientId)`: 解除医患关系
- `getDoctorRelations(String doctorId)`: 获取医生的所有患者关系
- `getPatientRelations(String patientId)`: 获取患者的所有医生关系
- `getDoctorPatients(String doctorId)`: 获取医生的患者列表
- `getPatientDoctors(String patientId)`: 获取患者的医生列表
- `relationExists(String doctorId, String patientId)`: 检查医患关系是否存在

## PatientReportService

患者报告服务，处理患者医疗报告的管理。

### 主要方法

- `createReport(PatientReport report)`: 创建新报告
- `getReportById(String reportId)`: 根据 ID 获取报告
- `getPatientReports(String patientId)`: 获取患者的所有报告
- `getDoctorReports(String doctorId)`: 获取医生创建的所有报告
- `updateReport(PatientReport report)`: 更新报告信息
- `deleteReport(String reportId)`: 删除报告
- `getPatientReportsByType(String patientId, String reportType)`: 获取患者指定类型的报告
- `getPatientReportsByDateRange(String patientId, LocalDateTime startDate, LocalDateTime endDate)`: 获取患者在指定日期范围内的报告

## CSVProcessingService

CSV 处理服务，用于处理 CSV 文件的上传、解析和导出。

### 主要方法

- `processCSVFile(MultipartFile file)`: 处理上传的 CSV 文件
- `parseCSVToReports(InputStream inputStream, String patientId, String doctorId)`: 将 CSV 解析为报告对象
- `exportReportsToCSV(List<PatientReport> reports)`: 将报告导出为 CSV
- `validateCSVHeaders(CSVRecord headerRecord)`: 验证 CSV 文件的标题行

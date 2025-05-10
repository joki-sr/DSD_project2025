# System 组与 Data 组协作 API 文档

## 1. 项目概述

本项目是一个医患管理系统，包含用户认证、医生患者关系管理、报告生成与分析等功能。System 组负责实现业务逻辑和 API 接口，Data 组负责数据持久化和访问层实现。本文档旨在明确 System 组对 Data 组的接口需求，以便双方高效协作。

## 2. 系统架构

项目采用分层架构：

- **Controller 层**：处理 HTTP 请求，参数验证，响应封装（System 组负责）
- **Service 层**：实现业务逻辑，权限验证，事务管理（System 组负责）
- **Repository 层**：定义数据访问方法（System 组定义，Data 组实现）
- **Entity 层**：具体数据库操作实现（Data 组负责）

## 3. Repository 接口需求

### 3.1 UserRepository

用于用户信息的增删改查和认证。

```java
public interface UserRepository {
    User findById(Long id);
    List<User> findAll();
    User findByPhone(String phone);        // 根据手机号精确查询
    User save(User user);
    void deleteByPhone(String phone);
    boolean existsByPhone(String phone);
}
```

### 3.2 DoctorRepository

用于医生信息管理。

```java
public interface DoctorRepository {
    List<Doctor> findAll();
    Doctor findByPhone(String phone);
    Doctor save(Doctor doctor);
    void deleteById(String id);
    boolean existsByNameOrPhone(String name, String phone);
}
```

### 3.3 PatientRepository

用于患者信息管理。

```java
public interface PatientRepository {
    Patient findByUserId(Long userId); // 根据关联的User表ID查询患者
    Patient findByIdNumber(String idNumber); // 根据身份证号/护照号查询
    List<Patient> findAll();
    Patient findByName(String name);
    Patient findByPhone(String phone);
    Patient save(Patient patient);
    void deleteById(String id);
    boolean existsByIdNumber(String idNumber);
}
```

### 3.4 DoctorPatientRelationRepository

用于管理医生与患者关系。

```java
public interface DoctorPatientRelationRepository {
    List<DoctorPatientRelation> findAll();

    // 查询医生关联的所有患者
    List<Patient> findPatientsByDoctorId(String doctorId);

    // 获取医患关系并包含详细信息的方法（避免N+1查询）
    List<DoctorPatientRelationDTO> findAllRelationsWithDetails();

    boolean existsByDoctorIdAndPatientId(String doctorId, String patientId);
    DoctorPatientRelation save(DoctorPatientRelation relation);
    void deleteByDoctorIdAndPatientId(String doctorId, String patientId);

    // 更新关系
    // 实现说明：这可能需要事务支持，涉及删除旧关系和创建新关系
    boolean updateRelation(String oldDoctorId, String oldPatientId,
                          String newDoctorId, String newPatientId);
}
```

### 3.5 PatientReportRepository

用于患者报告管理。

```java
public interface PatientReportRepository {
    List<PatientReport> findByPatientIdOrderByDateDesc(String patientId);
    PatientReport findById(String reportId);
    PatientReport save(PatientReport report);
    void deleteById(String reportId);

    // 更新报告类型和概要
    boolean updateReportTypeAndSummary(String reportId, String type, String summary);
}
```

### 3.6 CSVDataRepository

用于 CSV 数据文件的保存和处理。

```java
public interface CSVDataRepository {
    CSVData save(CSVData csvData);
    CSVData findByPatientIdAndFileName(String patientId, String fileName);
    List<CSVData> findByPatientId(String patientId);
    CSVData findById(String id);
}
```

## 4. 实体类设计建议

### 4.1 User

```java
public class User {
    private Long id;                // 自增主键
    private String username;        // 用户名（可能是姓名）
    private String password;        // 存储加密后的密码
    private String name;            // 真实姓名
    private String phone;           // 电话号码
    private String userType;        // "ADMIN", "DOCTOR", "PATIENT"
    private boolean isAdmin;        // 是否是管理员，冗余字段方便权限判断
    private boolean isDoctor;       // 是否是医生，冗余字段方便权限判断
    private boolean isPatient;      // 是否是患者，冗余字段方便权限判断

    // 构造函数、getter和setter方法
}
```

### 4.2 Doctor

```java
public class Doctor {
    private String id;              // 系统生成的唯一标识，如UUID
    private Long userId;            // 关联的User表ID，形成一对一关系
    private String name;            // 医生姓名（可从User表同步）
    private String phone;           // 电话号码（可从User表同步）
    private String hospital;        // 所在医院
    private String department;      // 科室

    // 构造函数、getter和setter方法
}
```

### 4.3 Patient

```java
public class Patient {
    private String id;              // 系统生成的唯一标识，如UUID
    private Long userId;            // 关联的User表ID，形成一对一关系
    private String name;            // 患者姓名（可从User表同步）
    private String phone;           // 电话号码（可从User表同步）
    private String gender;          // 性别（"男"/"女"）
    private Date birthDate;         // 出生日期
    private String idType;          // "身份证"或"护照"
    private String idNumber;        // 证件号码（唯一）
    private String address;         // 地址（可选）

    // 构造函数、getter和setter方法
}
```

### 4.4 DoctorPatientRelation

```java
public class DoctorPatientRelation {
    private String id;              // 可选，若使用复合主键可不需要
    private String doctorId;        // 医生ID，对应Doctor表的id
    private String patientId;       // 患者ID，对应Patient表的id
    private Date createdAt;         // 关系创建时间

    // 构造函数、getter和setter方法
}
```

### 4.5 PatientReport

```java
public class PatientReport {
    private String id;              // 系统生成的唯一标识，如UUID
    private String patientId;       // 患者ID
    private Date reportDate;        // 报告日期
    private String type;            // 报告类型
    private String summary;         // 报告摘要
    private ReportData reportData;  // 报告数据对象
    private String rawDataId;       // 关联的原始CSV数据ID

    // 构造函数、getter和setter方法
}
```

### 4.6 ReportData

```java
public class ReportData {
    private List<Double> standardRange;  // 标准幅度，12项指标
    private List<Double> motionRange;    // 运动幅度，12项指标
    private List<Double> difference;     // 差值，12项指标

    // 构造函数、getter和setter方法
}
```

### 4.7 CSVData

```java
public class CSVData {
    private String id;              // 系统生成的唯一标识，如UUID
    private String patientId;       // 关联的患者ID
    private String fileName;        // 上传的文件名
    private Date uploadTime;        // 上传时间
    private String fileContent;     // CSV文件内容或存储路径
    private long fileSize;          // 文件大小（字节）

    // 构造函数、getter和setter方法
}
```

### 4.8 用于数据传输的 DTO 对象

```java
public class DoctorPatientRelationDTO {
    private String doctorId;
    private String doctorName;
    private String patientId;
    private String patientName;

    // 构造函数、getter和setter方法
}
```

## 5. 用户与医生/患者关联机制

用户、医生和患者实体之间的关系设计如下：

1. **一对一关系模型**：

   - 一个 Doctor 实体通过 `userId` 字段与一个 User 实体关联
   - 一个 Patient 实体通过 `userId` 字段与一个 User 实体关联
   - User 实体通过 `userType` 字段标识用户类型（ADMIN/DOCTOR/PATIENT）

2. **新用户注册和关联流程**：

   - 当通过 API 注册新医生/患者时，System 组需要：
     1. 创建 User 实体（包含登录凭证）
     2. 创建 Doctor/Patient 实体（包含专业/个人信息）
     3. 设置正确的关联关系
   - Data 组需要确保：
     1. 支持在同一事务中创建关联实体
     2. 提供通过 `userId` 查询关联实体的方法
     3. 确保数据一致性（例如，删除 User 时级联删除关联的 Doctor/Patient）

3. **用户认证与权限判断**：

   - 用户登录时通过 UserRepository 获取基本信息和类型
   - 如需获取详细信息，可通过 DoctorRepository 或 PatientRepository 查询

## 6. ID 生成策略与一致性

1. **各实体 ID 策略**：

   - User：使用自增 Long 类型 ID
   - Doctor/Patient/PatientReport/CSVData：使用 String 类型 ID，建议采用 UUID
   - DoctorPatientRelation：可使用复合主键（doctorId 和 patientId）或独立 ID

2. **ID 生成方式**：

   - 自增 ID 由数据库自动生成
   - UUID 可在应用层生成（推荐）或由数据库生成
   - Data 组需负责保证 ID 生成的唯一性和一致性

3. **注意事项**：

   - Patient 实体的 id 与 idNumber（身份证/护照号）是不同的字段
   - API 使用的 ID 应保持稳定，不建议直接使用可能变更的业务字段作为主键
   - 为便于 API 使用，String 类型 ID 应避免特殊字符，推荐 UUID 去除连字符

## 7. Repository 方法的具体行为说明

1. **用户查询方法细化**：

   - `UserRepository.findByUsername(String username)`：仅查询用户名字段
   - `UserRepository.findByPhone(String phone)`：仅查询手机号字段
   - `UserRepository.findByUsernameOrPhone(String usernameOrPhone)`：同时匹配用户名或手机号字段

2. **关系管理方法说明**：

   - `DoctorPatientRelationRepository.updateRelation(...)`：可能需要在一个事务中：
     1. 检查新关系是否已存在
     2. 删除旧关系
     3. 创建新关系

3. **复杂查询支持**：

   - `DoctorPatientRelationRepository.findAllRelationsWithDetails()`：
     返回包含医生名称和患者名称的 DTO 对象列表，避免 Service 层出现 N+1 查询问题

4. **批量操作**：

   - 对于需要高性能的场景，可以考虑添加批量操作方法，如 `saveAll()`

## 8. CSV 数据处理与报告生成的职责边界

1. **职责划分**：

   - **System 组职责**：
     - CSV 文件内容的业务解析和验证
     - 从原始数据计算标准幅度、运动幅度、差值等指标
     - 生成 PatientReport 对象
   - **Data 组职责**：
     - 保存原始 CSV 数据
     - 保存处理后的报告数据
     - 提供高效的数据检索方法

2. **流程说明**：

   - 当上传 CSV 文件时：
     1. System 组解析文件内容，提取有效数据
     2. System 组计算必要的指标
     3. System 组调用 CSVDataRepository 保存原始数据
     4. System 组构造 PatientReport 对象并调用 PatientReportRepository 保存
     5. System 组返回处理结果给前端

3. **数据关联**：

   - PatientReport 实体包含 rawDataId 字段，关联到原始 CSV 数据
   - 这允许在需要时重新分析或审计原始数据

## 9. 权限管理需求

System 组需要 Data 组提供以下权限相关功能：

1. **用户角色验证**：

   - User 实体中的 userType 和 is\*字段用于判断用户类型
   - Repository 方法应支持按用户类型检索

2. **医患关系验证**：

   - `DoctorPatientRelationRepository.existsByDoctorIdAndPatientId()`方法用于验证医生是否关联了特定患者
   - `DoctorPatientRelationRepository.findPatientsByDoctorId()`方法用于获取医生关联的所有患者

3. **数据访问控制**：

   - 管理员可访问所有数据
   - 医生只能访问其关联患者的数据
   - 患者只能访问自己的数据
   - Data 组需提供能够支持这些控制规则的查询方法

## 10. API 响应数据的组装

1. **数据组装需求**：

   - 某些 API 响应需要组合多个实体的数据
   - 例如："获取医患关系列表"需要显示医生和患者的姓名

2. **优化策略**：

   - 使用 DTO 对象（如 DoctorPatientRelationDTO）直接从 Repository 层获取组合数据
   - 减少 Service 层的多次查询，避免 N+1 查询问题
   - Data 组可以使用 JOIN 查询或自定义实现

3. **按需加载**：

   - 对于复杂但不常用的查询，可保持简单接口，由 Service 层组装
   - 对于频繁使用且性能敏感的接口，考虑在 Repository 层优化

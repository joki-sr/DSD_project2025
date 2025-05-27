# System 组与 Data 组协作 API 文档

## 1. 项目概述

本项目是一个医患管理系统，包含用户认证、医生患者关系管理、报告生成与分析等功能。System 组负责实现业务逻辑和 API 接口，Data 组负责数据持久化和访问层实现。本文档旨在明确 System 组对 Data 组的接口需求，以便双方高效协作。

## 2. 系统架构

项目采用分层架构：

- **Controller 层**：处理 HTTP 请求，参数验证，响应封装（System 组负责）
- **Service 层**：实现业务逻辑，权限验证，事务管理（System 组负责）
- **Repository 层**：定义数据访问方法（System 组定义，Data 组实现）
- **Entity 层**：具体数据库操作实现（Data 组负责）

## 3.数据清洗

### DataManager

单例懒加载的数据清洗类，调用python脚本进行时间对齐。由data team组内的RecordService调用。

```java
public class DataManager {
    private final Integer frequency;
    private final String pythonEdition;
    private final String pythonFilePath;

    // 私有构造函数，防止外部直接 new
    private DataManager() {
        // 可以在此初始化 frequency
        frequency = 2;
        pythonEdition = "python";
//        pythonFilePath = "clean_script.py";
        pythonFilePath = "D:\\development\\DSD\\DSD_project2025\\src\\main\\java\\com\\example\\factorial\\src\\dataProcess\\clean_script.py";
    }

    private static volatile DataManager instance;    // 使用 volatile 确保多线程下的可见性与禁止指令重排序

    public static DataManager getInstance();

    //对指定路径的csv原始数据文件清洗并存到新文件里，返回新文件路径
    public String rawToCleaned(String  rawPath) throws IOException, InterruptedException;
```



## 4. 实体类设计建议

### 4.1 User

![](./png/user.png)



#### User entity

```java
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;

    /** 联系电话（允许国际区号，可根据需要调整正则） */
    @Column(name = "phonenumber", length = 45)
    @Pattern(regexp = "^\\+?\\d{1,45}$", message = "电话号码格式不正确")
    private String phonenumber;

    /** 角色类型（DOCTOR/PATIENT） */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "ENUM('DOCTOR','PATIENT','ADMIN')")
    private User.RoleType roletype;
    
    /* ---------- 枚举 ---------- */
    public enum RoleType {
        @JsonProperty("DOCTOR")
        DOCTOR,
        @JsonProperty("PATIENT")
        PATIENT,
        @JsonProperty("ADMIN")
        ADMIN,
    }
    
    // 构造函数、getter和setter方法
}
```

对于每个字段，都有getter和setter方法。

#### UserRepository

用于用户信息的增删改查和认证。

```java
public interface UserRepository {
    //C
    User(String username, String password, User.RoleType roletype, String phonenumber)
	//R
    User findById(Long id);
    List<User> findAll();
    User findByUsername(String username);  // 根据用户名精确查询
    User findByPhoneNumber(String phone);        // 根据手机号精确查询
    User findByUsernameOrPhoneNumber(String usernameOrPhone); // 同时查询用户名和手机号字段
    List<User> findByUserType(String userType);  // 例如：ADMIN, DOCTOR, PATIENT
    //**boolean existsByUsername(String username); //不需要，使用findByUsername()替代
    //**boolean existsByPhone(String phone);	//不需要，findByPhone()替代

    //U
    User save(User user);
    //D
    void deleteById(Long id);
    void deleteByUsername(Long id);
}
```

##### 接口用法示例

##### //Create

User(String username, String password, User.RoleType roletype)

创建新User

##### //Read

User findByUsername(String username);

根据用户名精确查询，若找不到则返回NULL

```java
        try {
            User notExistUser = userRepository.findByUsername("notExistUser");
            System.out.println("findByUsername():" + notExistUser.toString());
        }catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
//: Cannot invoke "com.example.factorial.src.entity.User.toString()" because "notExistUser" is null

```

List<User> findByRole(String userType); 

查找user表里的所有{Role}，例如ADMIN, DOCTOR, PATIENT

boolean existsByUsername(String username);

直接使用findByUsername()即可。

java. util. Optional<T> findById(Long id);

查询根据主键（id），若找不到则返回异常

```java
        // findById 异常
        Long notExistId = 999L;
        try {
            User notExistUser = userRepository.findById(notExistId)
                    .orElseThrow(() -> new RuntimeException("User with id "+notExistId+ "not found"));
            System.out.println("findById(): " + admin.toString());
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
//Error: User with id 999not found
```

List<User> findAll();

查询所有用户，若找不到则返回NULL

```java
        //findAll
        // 查询所有用户
        System.out.println("findByAll(): ");
        List<User> all = userRepository.findAll();
        all.forEach(System.out::println);
```

##### //Update

**User save(User user);**

保存user

**void setPassword/Roletype/username(String/User.RoleType/String)**

修改User信息（但是没有存到数据库，需要使用save() 才能存到数据库中）

##### //Delete

**void deleteById(Long id);**

根据主键（id）删除用户

**void deleteByUsername(String username);**

根据username删除用户

```java
        // 删除用户
        if (userRepository.findByUsername(updatedUser.getUsername()) != null) {
            userRepository.deleteByUsername(updatedUser.getUsername());//根据username删除
//              userRepository.deleteById(updatedUser.getId()); //根据id删除
            System.out.println("deleteByUsername: " + updatedUser.getUsername());
        }else{
            System.out.println(updatedUser.getUsername() + "not found");
        }
```





### 4.2 Doctor

![](./png/doctor.png)

#### Doctor entity

```java
public class Doctor {

    /** 主键：登录用户名 */
    @Id
    @Column(name = "username", nullable = false, length = 255)
    @NotBlank(message = "用户名不能为空")
    @Size(max = 255, message = "用户名长度不能超过 255 字符")
    private String username;

    /** 医生姓名 */
    @Column(name = "docname", length = 45)
    @Size(max = 45, message = "医生姓名长度不能超过 45 字符")
    private String docname;

    /** 所属医院 */
    @Column(name = "hospital", length = 45)
    @Size(max = 45, message = "医院名称长度不能超过 45 字符")
    private String hospital;

    /** 科室 */
    @Column(name = "Department", length = 45)
    @Size(max = 45, message = "科室名称长度不能超过 45 字符")
    private String department;

    /** 联系电话（允许国际区号，可根据需要调整正则） */
    @Column(name = "phonenumber", length = 45)
    @Pattern(regexp = "^\\+?\\d{1,45}$", message = "电话号码格式不正确")
    private String phonenumber;

    /** 业务构造器（可按需扩展） */
    public Doctor(String username,
                  String docname,
                  String hospital,
                  String department,
                  String phonenumber) {
        this.username   = username;
        this.docname    = docname;
        this.hospital   = hospital;
        this.department = department;
        this.phonenumber= phonenumber;
    }
}
```
构造示例：

```
Doctor doctor = new Doctor(
        "dr001",
        "张三",
        "协和医院",
        "神经内科",
        "+8613800000000"
);
```

#### DoctorRepository

用于医生信息管理。

```java
public interface DoctorRepository {
    Optional<T> findById(String id);//根据主键（这里的主键是username，如"DOC001")查询医生
    Doctor findByUsername(String id);//根据username(如;"DOC001")查询医生
    Doctor findByDocname(String name);//根据姓名（如“张三”）查询医生
    Doctor findByPhonenumber(String phone);
    
    //Doctor findByName(String name);//实现为findByDocname()和findByUsername(),如上
    List<Doctor> findAll();
	List<Doctor> findByDocnameOrPhonenumber(@Size(max = 45, message = "医生姓名长度不能超过 45 字符") String docname, @Pattern(regexp = "^\\+?\\d{1,45}$", message = "电话号码格式不正确") String phonenumber);
    List<Doctor> findByHospital(String hospital);
    List<Doctor> findByDepartment(@Size(max = 45, message = "科室名称长度不能超过 45 字符") String department);

    Doctor save(Doctor doctor);
    
    void deleteById(String id);//根据主键（username，如"DOC001")删除医生
	void deleteByUsername(@NotBlank(message = "用户名不能为空") @Size(max = 255, message = "用户名长度不能超过 255 字符") String username);
}
```



### 4.3 Patient

![](./png/patient.png)

#### Patient entity

```java
public class Patient {

    /** 用户名 —— 主键 */
    @Id
    @Column(name = "username", length = 255, nullable = false)
    @Size(max = 255, message = "用户名长度不能超过 255 字符")
    private String username;

    /** 证件类型（passport / idCard） */
    @Enumerated(EnumType.STRING)
    @Column(name = "id_type", columnDefinition = "ENUM('passport','idCard')")
    private IdType idtype;

    /** 证件号码 */
    @Column(name = "id_number", length = 32)
    @Size(max = 32, message = "真实姓名长度不能超过 32 字符")
    private String idnumber;


    /** 真实姓名 */
    @Column(name = "realname", length = 45)
    @Size(max = 45, message = "真实姓名长度不能超过 45 字符")
    private String realname;

//    /** 出生年份（建议 YYYY） */
//    @Column(name = "birthdate", length = 4)
//    @Pattern(regexp = "^(19|20)\\d{2}$",
//             message = "出生年份必须是 1900–2099 之间的 4 位数字")
//    private String birthyear;
    /** 出生日期（格式 YYYY-MM-DD） */
    @Column(name = "birthdate")
    @Pattern(
            regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$",
            message = "出生日期必须是格式为 YYYY-MM-DD 的合法日期"
    )
    private String birthdate;



    /** 性别（male / female） */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "ENUM('male','female')")
    private Gender gender;

    /** 联系电话（允许 +86-12345678901 或 11 位手机号） */
    @Column(name = "phonenumber", length = 45)
//    @Pattern(
//        regexp = "^(\\+?\\d{1,4}[- ]?)?\\d{5,20}$",
//        message = "电话号码格式不正确"
//    )
    private String phonenumber;

    /** 对应就诊医生（可为空，45 字符以内） */
    @Column(name = "doc", length = 45)
    @Size(max = 45, message = "医生字段长度不能超过 45 字符")
    private String doc;

    /* ---------- 枚举 ---------- */

    public enum IdType {
        @JsonProperty("passport")
        passport,
        @JsonProperty("idCard")
        idCard
    }

    public enum Gender {
        male, female
    }

    /* ---------- 业务构造器 ---------- */
    public Patient(String username,
                   IdType idtype,
                   String realname,
                   String birthdate,
                   Gender gender,
                   String phonenumber,
                   String doc) {
        this.username    = username;
        this.idtype      = idtype;
        this.realname    = realname;
        this.birthdate   = birthdate;
        this.gender      = gender;
        this.phonenumber = phonenumber;
        this.doc         = doc;
    }

    public Patient(String username,
                   IdType idtype,
                   String realname,
                   String birthdate,
                   Gender gender,
                   String phonenumber) {
        this.username    = username;
        this.idtype      = idtype;
        this.realname    = realname;
        this.birthdate   = birthdate;
        this.gender      = gender;
        this.phonenumber = phonenumber;
    }
    
    // 构造函数、getter和setter方法
}
```

构造示例

```
Patient patient = new Patient(
        "test_user_002",
        Patient.IdType.idCard,
        "李三",
        "2000-11-22",
        Patient.Gender.male,
        "+8613712345678"
);
```

#### PatientRepository

用于患者信息管理。

```java
public interface PatientRepository {
    //R
    Patient findById(String id);//根据主键（这里的主键是username，如"pat001")查询患者
    Patient findByUsername(String username);
    Patient findByIdNumber(String idNumber); // 根据身份证号/护照号查询
    Patient findByPhonenumber(String phonenumber)
//    Patient findByName(String name);//实现为findByUsername()，如上
//    Patient findByPhone(String phone);//实现为findByPhonenumber()，如上
    List<Patient> findAll();
    List<Patient> findByIdType(Patient.IdType idType);//根据护照/身份证查找
    List<Patient> findByRealname(@Size(max = 45, message = "真实姓名长度不能超过 45 字符") String realname);//根据真实姓名（如"韩非"）查找
    List<Patient> findByRealnameContaining(String realname);//根据真实姓名（如"三"）模糊查找
    List<Patient> findByGender(Patient.Gender gender);//根据性别查找
    List<Patient> findByBirthdate(String year);//根据出生年份查找
    List<Patient> findByDoc(String doc);    /** 根据就诊医生查询患者列表 */

    //U
    Patient save(Patient patient);
    
    //D
    void deleteById(String id);
//    boolean existsByIdNumber(String idNumber);//实现为findByIdNumber()
}
```

### 4.4 Record

![](./png/db-record1.png)

![](./png/db-record2.png)

#### record entity

```java
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;//YYYY-MM-DD (2025-04-17)

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date time;//YYYY-MM-DD hh:mm:ss (2025-04-16 22:17:43)

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "raw_file_path")
    private String rawFilePath;

    @Column(name = "raw_size_kb")
    private Integer rawSizeKb;//文件大小，以kb为单位

    @Lob
    @Column(name = "raw_file", columnDefinition = "LONGBLOB")
    private byte[] rawFile;//原始数据文件的内容

    // 其他字段保留但可为 null，无需初始化
    @Column(name = "format_file_path")
    private String formatFilePath;//时间对齐的数据文件的内容

    @Column(name = "format_size")
    private Integer formatSize;//文件大小，以kb为单位

    @Lob
    @Column(name = "format_file", columnDefinition = "LONGBLOB")
    private byte[] formatFile;

    @Column(name = "report_file_path")
    private String reportFilePath;

    @Column(name = "report_size")
    private Integer reportSize;//文件大小，以kb为单位

    @Lob
    @Column(name = "report_file", columnDefinition = "LONGBLOB")
    private byte[] reportFile;//报告文件的内容

    // Getter / Setter / 构造函数等略，可用 Lombok 简化

    public Record(Date date, Date time, String username,
                  String rawFilePath, Integer rawSizeKb, byte[] rawFile) {
        this.date = date;
        this.time = time;
        this.username = username;
        this.rawSizeKb = rawSizeKb;
        this.rawFile = rawFile;
        this.formatSize = 0;
        this.formatFile = new byte[0];
        this.reportSize = 0;
        this.reportFile = new byte[0];
    }
}
```

#### RecordRepository

用于数据文件的保存和处理。

```java
public interface RecordRepository {
    Record save(Record record);
    Optional<Record> findById(Long id);//
    List<Record> findAll();
    void deleteById(Long id);
    
// 根据用户名和精确时间查找
	List<Record> findByUsernameAndTime(String username, Date time);

// 根据用户名和日期查找
	List<Record> findByUsernameAndDate(String username, Date date);

// 查找某个用户的所有记录
	List<Record> findByUsername(String username);

// 查找某一天的所有记录
	List<Record> findByDate(Date date);
   
}
```

#### RecordService

```java
        List<Record> records = recordService.insertTwoCsvRecords(String username, String csvPath1, String csvPath2);//根据用户名，存入新采集到的两个csv文件，并进行数据清洗（时间对齐）
        List<Record> records = recordService.insertFourCsvRecords(String username, String csvPath1, String csvPath2, String csvPath3, String csvPath4);//根据用户名，存入新采集到的4个csv文件，并进行数据清洗（时间对齐）

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

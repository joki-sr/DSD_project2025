# Repository 包说明文档

Repository 包包含系统的所有数据访问接口，使用 Spring Data JPA 实现数据库操作。这些接口继承自 JpaRepository，提供基本的 CRUD 操作。

## UserRepository

用户数据访问接口，处理用户实体的持久化操作。

### 主要方法

- `findByUsername(String username)`: 根据用户名查找用户
- `findByPhone(String phone)`: 根据手机号查找用户
- `existsByPhone(String phone)`: 检查手机号是否已存在
- `existsByPhoneOrUsername(String phone, String username)`: 检查手机号或用户名是否已存在

## DoctorRepository

医生数据访问接口，处理医生实体的持久化操作。

### 主要方法

- `findByDoctorId(String doctorId)`: 根据医生 ID 查找医生
- `findByPhone(String phone)`: 根据手机号查找医生
- `findByUser(User user)`: 根据用户实体查找医生
- `existsByDoctorId(String doctorId)`: 检查医生 ID 是否已存在
- `existsByPhone(String phone)`: 检查手机号是否已存在
- `findByNameContainingIgnoreCase(String name)`: 模糊查询医生姓名
- `findByDepartment(String department)`: 根据科室查找医生
- `findByTitle(String title)`: 根据职称查找医生

## PatientRepository

患者数据访问接口，处理患者实体的持久化操作。

### 主要方法

- `findByPatientId(String patientId)`: 根据患者 ID 查找患者
- `findByIdNumber(String idNumber)`: 根据证件号码查找患者
- `findByPhone(String phone)`: 根据手机号查找患者
- `findByUser(User user)`: 根据用户实体查找患者
- `existsByPatientId(String patientId)`: 检查患者 ID 是否已存在
- `existsByIdNumber(String idNumber)`: 检查证件号码是否已存在
- `existsByPhone(String phone)`: 检查手机号是否已存在
- `findByNameContainingIgnoreCase(String name)`: 模糊查询患者姓名

## PatientReportRepository

患者报告数据访问接口，处理患者报告实体的持久化操作。

### 主要方法

- `findByReportId(String reportId)`: 根据报告 ID 查找报告
- `findByPatient(Patient patient)`: 查找患者的所有报告
- `findByDoctor(Doctor doctor)`: 查找医生创建的所有报告
- `findByPatientAndReportType(Patient patient, String reportType)`: 查找患者指定类型的报告
- `findByPatientAndReportDateBetween(Patient patient, LocalDateTime startDate, LocalDateTime endDate)`: 查找患者在指定日期范围内的报告
- `findByPatientAndDoctorAndReportDateBetween(Patient patient, Doctor doctor, LocalDateTime startDate, LocalDateTime endDate)`: 查找患者在指定医生创建的指定日期范围内的报告
- `existsByReportId(String reportId)`: 检查报告 ID 是否已存在

## DoctorPatientRelationRepository

医患关系数据访问接口，处理医患关系实体的持久化操作。

### 主要方法

- `findByDoctor(Doctor doctor)`: 查找医生的所有医患关系
- `findByPatient(Patient patient)`: 查找患者的所有医患关系
- `findByDoctorAndStatus(Doctor doctor, String status)`: 查找医生的特定状态的医患关系
- `findByPatientAndStatus(Patient patient, String status)`: 查找患者的特定状态的医患关系
- `findByDoctorAndPatient(Doctor doctor, Patient patient)`: 查找特定医生和患者之间的关系
- `findByDoctorAndPatientAndStatus(Doctor doctor, Patient patient, String status)`: 查找特定医生和患者之间特定状态的关系
- `existsByDoctorAndPatientAndStatus(Doctor doctor, Patient patient, String status)`: 检查特定医生和患者之间特定状态的关系是否存在

# Entity 包说明文档

Entity 包包含系统的所有实体类，代表数据库中的表结构，使用 JPA 注解进行对象关系映射。

## User

用户实体类，存储系统用户的基本信息和认证信息。

### 主要属性

- `id`: 主键 ID
- `username`: 用户名，唯一
- `password`: 加密后的密码
- `name`: 用户真实姓名
- `phone`: 手机号，唯一
- `userType`: 用户类型（ADMIN/DOCTOR/PATIENT）
- `isDoctor`: 是否医生
- `isPatient`: 是否患者
- `isAdmin`: 是否管理员
- `doctor`: 关联的医生实体（一对一）
- `patient`: 关联的患者实体（一对一）

## Doctor

医生实体类，存储医生的专业信息。

### 主要属性

- `id`: 主键 ID
- `doctorId`: 医生业务 ID，唯一
- `name`: 医生姓名
- `gender`: 性别
- `department`: 所属科室
- `title`: 职称
- `phone`: 联系电话
- `user`: 关联的用户实体（一对一）
- `patientRelations`: 医患关系列表（一对多）

## Patient

患者实体类，存储患者的个人信息。

### 主要属性

- `id`: 主键 ID
- `patientId`: 患者业务 ID，唯一
- `name`: 患者姓名
- `gender`: 性别
- `birthDate`: 出生日期
- `idType`: 证件类型
- `idNumber`: 证件号码，唯一
- `phone`: 联系电话
- `user`: 关联的用户实体（一对一）
- `reports`: 患者报告列表（一对多）
- `doctorRelations`: 医患关系列表（一对多）

## PatientReport

患者报告实体类，存储患者的医疗报告信息。

### 主要属性

- `id`: 主键 ID
- `reportId`: 报告业务 ID，唯一
- `reportType`: 报告类型
- `reportDate`: 报告日期
- `diagnosis`: 诊断结果
- `treatment`: 治疗方案
- `notes`: 备注信息
- `filePath`: 报告文件路径
- `patient`: 关联的患者（多对一）
- `doctor`: 关联的医生（多对一）
- `createdAt`: 创建时间
- `updatedAt`: 更新时间

## DoctorPatientRelation

医患关系实体类，建立医生和患者之间的多对多关系。

### 主要属性

- `id`: 主键 ID
- `doctor`: 关联的医生（多对一）
- `patient`: 关联的患者（多对一）
- `relationType`: 关系类型（主治医师、专家会诊等）
- `startDate`: 关系开始日期
- `endDate`: 关系结束日期
- `status`: 关系状态（active/inactive/pending）
- `createdAt`: 创建时间
- `updatedAt`: 更新时间

## 实体关系

### 一对一关系

- User - Doctor: 一个用户可以是一个医生，一个医生对应一个用户
- User - Patient: 一个用户可以是一个患者，一个患者对应一个用户

### 一对多关系

- Patient - PatientReport: 一个患者可以有多个报告
- Doctor - PatientReport: 一个医生可以创建多个报告

### 多对多关系（通过 DoctorPatientRelation 实现）

- Doctor - Patient: 一个医生可以有多个患者，一个患者可以有多个医生

### 级联操作

- User 和 Doctor/Patient 之间的级联：当删除用户时，关联的医生/患者也会被删除
- Patient 和 PatientReport 之间的级联：当删除患者时，关联的报告也会被删除
- Doctor/Patient 和 DoctorPatientRelation 之间的级联：当删除医生/患者时，关联的关系也会被删除

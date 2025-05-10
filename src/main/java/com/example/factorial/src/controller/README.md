# Controller 包说明文档

Controller 包包含系统的所有 API 控制器，负责处理 HTTP 请求并返回响应。每个控制器针对不同的业务实体提供相应的 API 接口。

## UserController

用户控制器，处理用户登录和注册相关功能。

### 主要方法

- `login(@RequestBody LoginRequest loginRequest)`：处理用户登录请求，验证用户凭据并返回 JWT 令牌
- `register(@RequestBody RegisterRequest registerRequest)`：处理用户注册请求，创建新用户并返回用户信息

## AdminController

管理员控制器，处理系统管理功能，包括医生管理、患者管理和医患关系管理。

### 主要方法

- `getDoctors()`：获取所有医生列表
- `getDoctor(String doctorId)`：获取指定医生详情
- `createDoctor(@RequestBody DoctorRequest request)`：创建新医生
- `updateDoctor(String doctorId, @RequestBody DoctorRequest request)`：更新医生信息
- `deleteDoctor(String doctorId)`：删除医生
- `getPatients()`：获取所有患者列表
- `getPatient(String patientId)`：获取指定患者详情
- `createPatient(@RequestBody PatientRequest request)`：创建新患者
- `updatePatient(String patientId, @RequestBody PatientRequest request)`：更新患者信息
- `deletePatient(String patientId)`：删除患者
- `assignDoctorToPatient(String doctorId, String patientId)`：建立医患关系
- `removeDoctorFromPatient(String doctorId, String patientId)`：解除医患关系

## DoctorController

医生控制器，处理医生查看患者列表、管理患者报告等功能。

### 主要方法

- `getPatients()`：医生获取自己的患者列表
- `getPatient(String patientId)`：医生获取指定患者详情
- `getPatientReports(String patientId)`：医生获取指定患者的报告列表
- `createPatientReport(String patientId, @RequestBody ReportRequest request)`：创建患者报告

## PatientController

患者控制器，处理患者查看自己的报告等功能。

### 主要方法

- `getReports()`：患者获取自己的所有报告
- `getReport(String reportId)`：患者获取指定报告详情
- `getDoctors()`：患者获取自己的主治医生列表

## UploadController

文件上传控制器，处理 CSV 文件上传和报告更新。

### 主要方法

- `uploadFile(MultipartFile file)`：上传 CSV 文件并解析
- `uploadReport(MultipartFile file, String patientId)`：上传患者报告文件
- `downloadReport(String reportId)`：下载报告文件

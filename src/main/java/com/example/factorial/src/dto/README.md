# DTO 包说明文档

DTO (Data Transfer Object) 包含用于在不同层之间传输数据的对象。该包分为请求（request）和响应（response）两个子包。

## request 包

包含所有客户端发送到服务器的请求数据对象。

### LoginRequest

用户登录请求对象。

**属性：**

- `username`: 用户名或手机号
- `password`: 用户密码

### RegisterRequest

用户注册请求对象。

**属性：**

- `name`: 用户姓名
- `phone`: 用户手机号（用作用户名）
- `gender`: 用户性别
- `birthDate`: 出生日期
- `idType`: 证件类型
- `idNumber`: 证件号码
- `password`: 用户密码

## response 包

包含所有服务器发送给客户端的响应数据对象。

### ApiResponse\<T>

通用 API 响应对象，封装所有 API 响应的标准格式。

**属性：**

- `status`: HTTP 状态码
- `message`: 响应消息
- `data`: 响应数据，泛型 T

**主要方法：**

- `success(T data, String message, int status)`: 创建成功响应
- `success(T data, String message)`: 创建标准成功响应（状态码 200）
- `success(T data)`: 创建成功响应，默认消息
- `error(String message, int status)`: 创建失败响应
- `error(String message)`: 创建失败响应，默认状态码 400
- `unauthorized()`: 创建未授权响应（状态码 401）
- `forbidden()`: 创建禁止访问响应（状态码 403）
- `notFound(String resource)`: 创建资源未找到响应（状态码 404）
- `conflict(String message)`: 创建资源冲突响应（状态码 409）
- `serverError()`: 创建服务器错误响应（状态码 500）

### LoginResponse

用户登录响应对象。

**属性：**

- `id`: 用户 ID
- `isAdmin`: 是否管理员
- `isDoctor`: 是否医生
- `isPatient`: 是否患者
- `name`: 用户姓名
- `token`: JWT 令牌

### UserResponse

用户注册响应对象。

**属性：**

- `id`: 用户 ID
- `name`: 用户姓名
- `phone`: 用户手机号
- `gender`: 用户性别
- `birthDate`: 出生日期
- `idType`: 证件类型
- `idNumber`: 证件号码

### DoctorResponse

医生信息响应对象。

**属性：**

- `doctorId`: 医生 ID
- `name`: 医生姓名
- `gender`: 性别
- `department`: 所属科室
- `title`: 职称
- `phone`: 联系电话

### PatientResponse

患者信息响应对象。

**属性：**

- `patientId`: 患者 ID
- `name`: 患者姓名
- `gender`: 性别
- `birthDate`: 出生日期
- `idType`: 证件类型
- `idNumber`: 证件号码
- `phone`: 联系电话

### ReportResponse

患者报告响应对象。

**属性：**

- `reportId`: 报告 ID
- `reportType`: 报告类型
- `reportDate`: 报告日期
- `diagnosis`: 诊断结果
- `treatment`: 治疗方案
- `doctorName`: 医生姓名

### RelationResponse

医患关系响应对象。

**属性：**

- `doctorId`: 医生 ID
- `patientId`: 患者 ID
- `doctorName`: 医生姓名
- `patientName`: 患者姓名
- `relationType`: 关系类型
- `status`: 关系状态

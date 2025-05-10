# Config 包说明文档

Config 包包含系统的各种配置类，用于配置 Spring 应用程序的不同方面，如安全、跨域等。

## SecurityConfig

安全配置类，配置 JWT 认证、访问规则和密码加密器。

### 主要功能

- 配置 Spring Security 安全框架
- 定义 API 访问权限规则
- 配置 JWT 认证过滤器
- 设置无状态会话策略
- 配置密码加密器
- 配置认证管理器

### 主要配置内容

- 禁用 CSRF 保护（因为使用 JWT 令牌）
- 启用 CORS 支持
- 配置公开访问的接口（无需认证）：
  - `/api/session`：用户登录
  - `/api/users`：用户注册
- 配置基于角色的访问控制：
  - `/api/admin/**`：需要 ADMIN 角色
  - `/api/doctor/**`：需要 DOCTOR 角色
  - `/api/patient/**`：需要 PATIENT 角色
- 配置无状态会话管理
- 配置 JWT 认证过滤器
- 提供密码加密器（BCryptPasswordEncoder）
- 提供认证管理器

### 主要方法

- `securityFilterChain(HttpSecurity http)`: 配置 HTTP 安全过滤链
- `authenticationManager(AuthenticationConfiguration config)`: 提供认证管理器
- `passwordEncoder()`: 提供密码加密器

## CorsConfig

跨域资源共享配置类，配置 CORS 策略，允许前端应用与后端 API 进行安全通信。

### 主要功能

- 配置允许的来源域（Origins）
- 配置允许的 HTTP 方法
- 配置允许的请求头
- 配置允许的响应头
- 设置预检请求的缓存时间
- 配置是否允许发送 Cookie

### 主要方法

- `corsConfigurationSource()`: 提供 CORS 配置源
- `addCorsMappings(CorsRegistry registry)`: 配置 CORS 映射

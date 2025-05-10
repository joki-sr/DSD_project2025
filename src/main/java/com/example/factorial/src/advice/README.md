# Advice 包说明文档

Advice 包包含系统的全局异常处理器，用于统一处理应用程序中抛出的各种异常，并将其转换为友好的 API 响应。

## GlobalExceptionHandler

全局异常处理器，使用 Spring 的@ControllerAdvice 注解，拦截所有控制器中抛出的异常，并返回统一格式的错误响应。

### 主要功能

- 拦截并处理应用程序中的各种异常
- 将异常转换为统一的 API 响应格式
- 根据异常类型设置适当的 HTTP 状态码
- 提供友好的错误消息，隐藏敏感的技术细节
- 记录异常日志，便于问题排查

### 处理的异常类型

#### HTTP 请求相关异常

- `HttpRequestMethodNotSupportedException`: 请求方法不支持异常，返回 405 状态码
- `HttpMediaTypeNotSupportedException`: 媒体类型不支持异常，返回 415 状态码
- `MissingServletRequestParameterException`: 缺少请求参数异常，返回 400 状态码
- `MethodArgumentNotValidException`: 方法参数验证失败异常，返回 400 状态码
- `BindException`: 参数绑定异常，返回 400 状态码

#### 安全相关异常

- `AccessDeniedException`: 访问拒绝异常，返回 403 状态码
- `BadCredentialsException`: 凭据错误异常，返回 401 状态码
- `AuthenticationException`: 认证异常，返回 401 状态码

#### 业务相关异常

- `EntityNotFoundException`: 实体未找到异常，返回 404 状态码
- `DataIntegrityViolationException`: 数据完整性违反异常，返回 409 状态码
- `ResourceNotFoundException`: 资源未找到异常，返回 404 状态码

#### 系统异常

- `Exception`: 通用异常，返回 500 状态码
- `RuntimeException`: 运行时异常，返回 500 状态码

### 处理方法

- `handleHttpRequestMethodNotSupported`: 处理请求方法不支持异常
- `handleHttpMediaTypeNotSupported`: 处理媒体类型不支持异常
- `handleMissingServletRequestParameter`: 处理缺少请求参数异常
- `handleMethodArgumentNotValid`: 处理方法参数验证失败异常
- `handleBindException`: 处理参数绑定异常
- `handleAccessDeniedException`: 处理访问拒绝异常
- `handleAuthenticationException`: 处理认证异常
- `handleEntityNotFoundException`: 处理实体未找到异常
- `handleDataIntegrityViolationException`: 处理数据完整性违反异常
- `handleResourceNotFoundException`: 处理资源未找到异常
- `handleExceptionInternal`: 内部异常处理方法
- `handleAllExceptions`: 处理所有未被特定处理方法捕获的异常

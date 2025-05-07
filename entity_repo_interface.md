### entity

### user

| 方法名        | 参数                                                       | 返回类型         |
| ------------- | ---------------------------------------------------------- | ---------------- |
| `User`        | 无                                                         | `void`（构造器） |
| `getId`       | 无                                                         | `Long`           |
| `setId`       | `Long id`                                                  | `void`           |
| `getUsername` | 无                                                         | `String`         |
| `setUsername` | `String username`                                          | `void`           |
| `getPassword` | 无                                                         | `String`         |
| `setPassword` | `String password`                                          | `void`           |
| `getRoletype` | 无                                                         | `User.RoleType`  |
| `setRoletype` | `User.RoleType roletype`                                   | `void`           |
| `User`        | `String username, String password, User.RoleType roletype` | `void`（构造器） |
| `toString`    | 无                                                         | `String`         |

### doctor

| 方法签名                   | 返回类型          | 说明         |
| -------------------------- | ----------------- | ------------ |
| `Doctor()`                 | void              | 无参构造函数 |
| `Doctor(...)`              | void              | 有参构造函数 |
| `getXxx()` / `setXxx(...)` | 各字段类型 / void | 属性访问器   |
| `toString()`               | `String`          | 打印类信息   |

### patient

| 方法签名                       | 返回类型 | 说明         |
| ------------------------------ | -------- | ------------ |
| `Patient()`                    | void     | 无参构造器   |
| `Patient(String, IdType, ...)` | void     | 业务构造器   |
| `getUsername()`                | `String` | 获取用户名   |
| `setUsername(String)`          | `void`   | 设置用户名   |
| `getIdType()`                  | `IdType` | 获取证件类型 |
| `setIdType(IdType)`            | `void`   | 设置证件类型 |
| `getRealname()`                | `String` | 获取真实姓名 |
| `setRealname(String)`          | `void`   | 设置真实姓名 |
| `getBirthyear()`               | `String` | 获取出生年份 |
| `setBirthyear(String)`         | `void`   | 设置出生年份 |
| `getGender()`                  | `Gender` | 获取性别     |
| `setGender(Gender)`            | `void`   | 设置性别     |
| `getPhonenumber()`             | `String` | 获取电话     |
| `setPhonenumber(String)`       | `void`   | 设置电话     |
| `getDoc()`                     | `String` | 获取就诊医生 |
| `setDoc(String)`               | `void`   | 设置就诊医生 |
| `toString()`                   | `String` | 打印调试信息 |

### Record

| **方法签名**                                           | **说明**                             |
| ------------------------------------------------------ | ------------------------------------ |
| `public Long getId()`                                  | 获取记录的主键 ID。                  |
| `public void setId(Long id)`                           | 设置记录的主键 ID。                  |
| `public Date getDate()`                                | 获取记录的日期部分（无具体时间）。   |
| `public void setDate(Date date)`                       | 设置记录的日期。                     |
| `public Date getTime()`                                | 获取记录的时间戳（包含日期和时间）。 |
| `public void setTime(Date time)`                       | 设置记录的时间戳。                   |
| `public String getUsername()`                          | 获取记录所属的用户名。               |
| `public void setUsername(String username)`             | 设置记录所属的用户名。               |
| `public String getRawFilePath()`                       | 获取原始文件的存储路径。             |
| `public void setRawFilePath(String rawFilePath)`       | 设置原始文件的路径。                 |
| `public Integer getRawSizeKb()`                        | 获取原始文件的大小（单位：KB）。     |
| `public void setRawSizeKb(Integer rawSizeKb)`          | 设置原始文件的大小（单位：KB）。     |
| `public byte[] getRawFile()`                           | 获取原始文件的二进制内容。           |
| `public void setRawFile(byte[] rawFile)`               | 设置原始文件的二进制内容。           |
| `public String getFormatFilePath()`                    | 获取格式化文件的存储路径。           |
| `public void setFormatFilePath(String formatFilePath)` | 设置格式化文件路径。                 |
| `public Integer getFormatSize()`                       | 获取格式化文件的大小。               |
| `public void setFormatSize(Integer formatSize)`        | 设置格式化文件的大小。               |
| `public byte[] getFormatFile()`                        | 获取格式化文件的二进制内容。         |
| `public void setFormatFile(byte[] formatFile)`         | 设置格式化文件的二进制内容。         |
| `public String getReportFilePath()`                    | 获取报告文件的存储路径。             |
| `public void setReportFilePath(String reportFilePath)` | 设置报告文件路径。                   |
| `public Integer getReportSize()`                       | 获取报告文件的大小。                 |
| `public void setReportSize(Integer reportSize)`        | 设置报告文件的大小。                 |
| `public byte[] getReportFile()`                        | 获取报告文件的二进制内容。           |
| `public void setReportFile(byte[] reportFile)`         | 设置报告文件的二进制内容。           |





### UserRepository

| 方法签名                                                  | 说明                       |
| --------------------------------------------------------- | -------------------------- |
| User findByUsername(String username)                      | 根据用户名查找用户         |
| `List<User> findAll()`                                    | 查找所有用户               |
| `List<User> findAll(Sort sort)`                           | 查找所有用户并排序         |
| `Page<User> findAll(Pageable pageable)`                   | 分页查找所有用户           |
| `Optional<User> findById(Long id)`                        | 根据 ID 查找用户           |
| `boolean existsById(Long id)`                             | 检查某 ID 是否存在         |
| `long count()`                                            | 统计记录总数               |
| `void deleteById(Long id)`                                | 根据 ID 删除用户           |
| `void delete(User entity)`                                | 删除指定用户               |
| `void deleteAllById(Iterable<? extends Long> ids)`        | 根据多个 ID 删除用户       |
| `void deleteAll(Iterable<? extends User> entities)`       | 删除多个用户               |
| `void deleteAll()`                                        | 删除所有用户               |
| `<S extends User> S save(S entity)`                       | 保存或更新一个用户         |
| `<S extends User> List<S> saveAll(Iterable<S> entities)`  | 批量保存或更新             |
| `void flush()`                                            | 将所有更改立即刷新到数据库 |
| `<S extends User> S saveAndFlush(S entity)`               | 保存并刷新                 |
| `void deleteAllInBatch()`                                 | 批量删除（立即执行）       |
| `void deleteAllByIdInBatch(Iterable<Long> ids)`           | 批量根据 ID 删除           |
| `User getOne(Long id)`（⚠️ 已弃用）`User getById(Long id)` | 延迟加载用户（JPA Proxy）  |

### DoctorRepository

| 方法签名                                                   | 说明                      |
| ---------------------------------------------------------- | ------------------------- |
| Doctor findByUsername(String username);                    | 根据用户名查找医生        |
| `List<Doctor> findAll()`                                   | 查询所有医生              |
| `List<Doctor> findAll(Sort sort)`                          | 排序后查询                |
| `Page<Doctor> findAll(Pageable pageable)`                  | 分页查询                  |
| `Optional<Doctor> findById(String username)`               | 通过主键查找医生          |
| `boolean existsById(String username)`                      | 判断某主键是否存在        |
| `long count()`                                             | 统计记录数                |
| `void deleteById(String username)`                         | 根据主键删除              |
| `void delete(Doctor doctor)`                               | 删除一个实体              |
| `void deleteAllById(Iterable<? extends String> ids)`       | 批量删除（按主键）        |
| `void deleteAll(Iterable<? extends Doctor> entities)`      | 批量删除                  |
| `void deleteAll()`                                         | 删除所有记录              |
| `<S extends Doctor> S save(S entity)`                      | 保存或更新一个医生        |
| `<S extends Doctor> List<S> saveAll(Iterable<S> entities)` | 批量保存                  |
| `void flush()`                                             | 强制写入数据库            |
| `<S extends Doctor> S saveAndFlush(S entity)`              | 保存并强制写入            |
| `void deleteAllInBatch()`                                  | 批量立即删除              |
| `void deleteAllByIdInBatch(Iterable<String> ids)`          | 批量删除主键集合          |
| `Doctor getById(String id)`                                | 延迟加载实体（JPA proxy） |

### patientRepo

| 方法签名                                                  | 功能说明             |
| --------------------------------------------------------- | -------------------- |
| `List<Patient> findByRealnameContaining(String realname)` | 姓名模糊查询         |
| `List<Patient> findByDoc(String doc)`                     | 查询指定医生的患者   |
| `Patient findByUsername(String username)`                 | 通过用户名查找       |
| `Optional<Patient> findById(String id)`                   | 继承自 Jpa，主键查找 |
| `List<Patient> findAll()`                                 | 查询全部             |
| `Patient save(Patient patient)`                           | 新增或更新数据       |
| `void deleteById(String id)`                              | 根据主键删除         |
| `boolean existsById(String id)`                           | 是否存在主键         |
| `long count()`                                            | 总数统计             |

### RecordRepository

| **方法签名**                                                 | **说明**                                         |
| ------------------------------------------------------------ | ------------------------------------------------ |
| `List<Report> findByUsername(String username)`               | 根据用户名查询所有报告。                         |
| `List<Report> findByTimeBetween(LocalDateTime start, LocalDateTime end)` | 查询指定时间区间内的报告。                       |
| `List<Report> findByFileSizeBetween(Integer minBytes, Integer maxBytes)` | 按文件大小范围查询报告。                         |
| `S save(S entity)`                                           | 保存一个实体对象。如果实体对象已存在，则会更新。 |
| `Optional<T> findById(ID id)`                                | 根据 ID 查询实体，返回一个 Optional 对象。       |
| `boolean existsById(ID id)`                                  | 根据 ID 判断实体是否存在。                       |
| `List<T> findAll()`                                          | 查询所有实体。                                   |
| `List<T> findAllById(Iterable<ID> ids)`                      | 根据给定的 ID 列表查询实体。                     |
| `long count()`                                               | 查询实体的数量。                                 |
| `void deleteById(ID id)`                                     | 根据 ID 删除实体。                               |
| `void delete(T entity)`                                      | 删除指定的实体对象。                             |
| `void deleteAllById(Iterable<? extends ID> ids)`             | 根据 ID 列表删除实体。                           |
| `void deleteAll(Iterable<? extends T> entities)`             | 删除指定的实体列表。                             |
| `void deleteAll()`                                           | 删除所有实体。                                   |
## 运行后端

```shell
mvn spring-boot:run
```

## 测试
现在所有的实体类和测试类都已经更新为匹配数据库表结构。您可以运行测试来验证数据库功能：


### user
测试文件：`src/test/java/com/example/factorial/src/DatabaseTestUser.java`


```shell
mvn test -Dtest=DatabaseTestUser
```

### Record
测试文件：`src/test/java/com/example/factorial/src/DatabaseTestRecord.java`
测试文件：`src/test/java/com/example/factorial/src/DatabaseTestRecordService.java`
测试文件：`src/test/java/com/example/factorial/src/DatabaseTestRecordCRUD.java`

```shell
mvn test -Dtest=DatabaseTestRecord
mvn test -Dtest=DatabaseTestRecordService

```
---
## 运行python清洗脚本
```shell
# 默认频率为2Hz
python D:\development\DSD\DSD_project2025\src\main\java\com\example\factorial\src\dataProcess\clean_script.py D:\development\DSD\DSD_project2025\src\main\java\com\example\factorial\src\dataProcess\raw\2025-04-17-06-17-43.1.csv
# 也可以手动设置频率，比如10Hz
python D:\development\DSD\DSD_project2025\src\main\java\com\example\factorial\src\dataProcess\clean_script.py D:\development\DSD\DSD_project2025\src\main\java\com\example\factorial\src\dataProcess\raw\2025-04-17-06-17-43.1.csv 10 
```

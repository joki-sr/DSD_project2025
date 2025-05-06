## 运行后端

```shell
mvn spring-boot:run
```

## 测试

现在所有的实体类和测试类都已经更新为匹配数据库表结构。您可以运行测试来验证数据库功能：

```shell
mvn test -Dtest=DatabaseTest
```

如果您想要看到更详细的测试输出，我们可以使用以下命令：

```shell
> mvn test -Dtest=DatabaseTest -Dmaven.test.failure.ignore=true -Dorg.slf4j.simpleLogger.log.org.springframework=DEBUG

```

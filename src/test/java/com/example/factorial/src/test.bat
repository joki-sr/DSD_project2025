@echo off
setlocal

:: 创建 logs 文件夹
if not exist logs (
    mkdir logs
)

:: 获取当前时间并格式化为 年-月-日-时-分-秒
for /f "tokens=1-5 delims=/: " %%a in ("%date% %time%") do (
    set year=%%a
    set month=%%b
    set day=%%c
    set hour=%%d
    set minute=%%e
)

:: 获取秒（保留整数部分）
set second=%time:~6,2%

:: 处理时间格式中的前导0（可选）
if "%month:~0,1%"=="0" set month=%month:~1%
if "%day:~0,1%"=="0" set day=%day:~1%
if "%hour:~0,1%"=="0" set hour=0%hour%
if "%minute:~0,1%"=="0" set minute=0%minute%
if "%second:~0,1%"=="0" set second=0%second%

:: 拼接时间字符串
set timestamp=%year%-%month%-%day%-%hour%-%minute%-%second%

:: 设置输出文件路径
set outputTar=logs\%timestamp%.txt

:: 执行测试并保存输出到日志文件
echo Running test and saving output to %outputTar%...
mvn test -Dtest=DatabaseTestRecordService > "%outputTar%" 2>&1

echo "测试输出已保存至: $outputTar"
end

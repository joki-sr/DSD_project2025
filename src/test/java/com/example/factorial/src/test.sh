#!/bin/bash

# 设置日志输出目录
outputdir="./logs"

# 获取当前时间（格式：年-月-日-时-分-秒）
time=$(date +"%Y-%m-%d-%H-%M-%S")

# 日志输出文件路径
outputTar="$outputdir/$time.txt"

# 创建 logs 文件夹（如果不存在）
mkdir -p "$outputdir"

# 执行测试并将输出保存到日志文件中
mvn test -Dtest=DatabaseTestRecordService > "$outputTar" 2>&1

# 可选：提示输出文件位置
echo "测试输出已保存至: $outputTar"

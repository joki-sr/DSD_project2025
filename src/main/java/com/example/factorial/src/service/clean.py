import pandas as pd
import numpy as np
from sqlalchemy import create_engine, inspect
from sqlalchemy.exc import SQLAlchemyError
import urllib.parse
from datetime import datetime, timedelta
import os

csv_file = './imu_data_2025-04-18-08-39-33.csv'  # 替换为实际文件路径
file_name = os.path.basename(csv_file)  # 获取文件名

schema_name = 'dsd'
table_name = f"{file_name}"  # 拼接字符串


# 数据库配置
db_config = {
    'host': 'localhost',
    'port': 3306,
    'user': 'root',
    'password': 'msMS123@_@',  # 替换为实际密码
    'database': 'dsd'
}
# 可调整的频率参数（单位：Hz）
frequency = 2  # 可以修改为需要的频率
################################################################################


def clean_and_store_sensor_data(csv_file, db_config, frequency=10):
    """
    读取CSV文件，清洗数据（最近邻插值），并存储到数据库

    参数:
        csv_file: CSV文件路径
        db_config: 数据库连接配置字典
        frequency: 采样频率(Hz)，默认为10
    """
    try:
        # 1. 读取CSV文件
        df = pd.read_csv(csv_file)

        # 2. 数据预处理
        # 确保时间戳是datetime类型
        df['时间戳'] = pd.to_datetime(df['时间戳'])

        # 检查并处理重复的时间戳（针对同一设备ID）
        # 对于同一设备ID的重复时间戳，保留第一条记录
        df = df.drop_duplicates(subset=['时间戳', '设备ID'], keep='first')

        # 3. 对每个设备ID分别处理
        cleaned_data = []

        for device_id, group in df.groupby('设备ID'):
            # 按设备ID分组
            device_df = group.sort_values('时间戳').copy()

            # 计算时间间隔（秒）
            min_time = device_df['时间戳'].min()
            max_time = device_df['时间戳'].max()

            # 生成目标时间序列（根据frequency）
            time_delta = timedelta(seconds=1 / frequency)
            target_times = pd.date_range(start=min_time, end=max_time, freq=time_delta)

            # 设置时间戳为索引以便重采样
            device_df.set_index('时间戳', inplace=True)

            try:
                # 使用最近邻插值
                resampled_df = device_df.reindex(target_times, method='nearest')
            except ValueError as e:
                print(f"设备 {device_id} 处理时出错: {e}")
                continue

            # 重置索引并将设备ID添加回数据
            resampled_df.reset_index(inplace=True)
            resampled_df.rename(columns={'index': '时间戳'}, inplace=True)
            resampled_df['设备ID'] = device_id

            cleaned_data.append(resampled_df)

        # 合并所有设备的数据
        if cleaned_data:  # 检查是否有成功处理的数据
            final_df = pd.concat(cleaned_data, ignore_index=True)
        else:
            print("没有有效数据可以处理")
            return

        # 4. 创建数据库连接
        # 对密码进行URL编码
        encoded_password = urllib.parse.quote_plus(db_config['password'])

        # 创建连接字符串
        db_url = f"mysql+pymysql://{db_config['user']}:{encoded_password}@{db_config['host']}:{db_config['port']}/{db_config['database']}"

        engine = create_engine(db_url)

        # # 检查表是否存在，如果不存在则创建
        # inspector = inspect(engine)
        # if not inspector.has_table(table_name):
        #     print("警告: 表 table_name 不存在，请先创建表")
        #     return
        # 使用SQLAlchemy的inspector来检查表是否存在
        inspector = inspect(engine)
        if table_name not in inspector.get_table_names(schema=schema_name):
            try:
                print(f"表 {table_name} 不存在，正在创建表...")
                # 自动创建表
                df.head(0).to_sql(
                    name=table_name,
                    con=engine,
                    schema=schema_name,
                    if_exists='replace',  # 创建表
                    index=False
                )
                print(f"表 {table_name} 创建成功!")
            except Exception as e:
                print(f"创建表失败: {e}")
                exit(1)
        else:
            print(f"表 {table_name} 已存在，准备插入数据...")

        # 存储数据
        final_df.to_sql(table_name, engine, if_exists='append', index=False)

        print(f"数据清洗完成，共处理{len(final_df)}条记录，频率为{frequency}Hz")

    except SQLAlchemyError as e:
        print(f"数据库错误: {e}")
    except Exception as e:
        print(f"发生错误: {e}")


# 使用示例
if __name__ == "__main__":
    # 执行清洗和存储
    clean_and_store_sensor_data(csv_file, db_config, frequency)
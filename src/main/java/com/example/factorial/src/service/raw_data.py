import pandas as pd
from altair.vega import exprString
from sqlalchemy import create_engine, inspect, MetaData, Table, Column, Integer, String, text
from sqlalchemy.exc import SQLAlchemyError
import urllib.parse
import os

from sqlalchemy.sql.coercions import expect

# 配置数据库连接
db_config = {
    'host': 'localhost',
    'port': 3306,
    'user': 'root',
    'password': 'msMS123@_@',  # 替换为实际密码
    'database': 'dsd'
}
csv_file = './imu_data_2025-04-18-08-39-33.csv'  # 替换为实际文件路径
file_name = os.path.basename(csv_file)  # 获取文件名
# 导入数据到dsd schema中的表
schema_name = 'dsd'
table_name = f"{file_name}_raw"  # 拼接字符串


################################################################################

# 对密码进行URL编码
db_config['password'] = urllib.parse.quote_plus(db_config['password'])

# 读取CSV文件
try:
    print(f"正在读取CSV文件: {csv_file}")
    df = pd.read_csv(csv_file)
    print("CSV文件读取成功!")
except Exception as e:
    print(f"读取CSV文件失败: {e}")
    exit(1)

# 如果时间戳列存在，进行格式化
if '时间戳' in df.columns:
    try:
        print("正在转换时间戳列格式...")
        df['时间戳'] = pd.to_datetime(df['时间戳'], errors='coerce')  # 如果格式不正确，设置为 NaT
        print("时间戳转换成功!")
    except Exception as e:
        print(f"时间戳转换失败: {e}")
        exit(1)

# 创建数据库连接字符串
connection_string = f"mysql+pymysql://{db_config['user']}:{db_config['password']}@{db_config['host']}:{db_config['port']}/{db_config['database']}"
print(f"连接字符串: {connection_string}")

# 连接数据库并创建引擎
try:
    print(f"正在连接数据库: {db_config['database']}...")
    engine = create_engine(connection_string)
    connection = engine.connect()
    print("数据库连接成功!")
except SQLAlchemyError as e:
    print(f"数据库连接失败: {e}")
    exit(1)


# 使用SQLAlchemy的inspector来检查表是否存在
inspector = inspect(engine)

# #######################################################################
# history table
# id    |  name
# ##################
history_table_name = "history"
if history_table_name not in inspector.get_table_names(schema=schema_name):
    try:
        print(f"表 {history_table_name} 不存在，正在创建表...")
        metadata = MetaData()
        history_table = Table(history_table_name, metadata,
                              Column('history_id', Integer, primary_key=True, autoincrement=True),
                              Column('name', String(255), nullable=False),
                              schema=schema_name)

        metadata.create_all(engine)
        print(f"表 {history_table_name} 创建成功！")
    except Exception as e:
        print(f"创建表失败: {e}")
        exit(1)
else:
    print(f"表 {history_table_name} 已存在，准备插入历史记录...")

# 导入历史数据
try:
    # 打印调试信息
    print(f"准备插入数据到 {schema_name}.{history_table_name}, 表名: {table_name}")

    # 使用新的连接确保事务提交
    with engine.begin() as connection:  # 这会自动提交事务
        # 获取表结构
        metadata = MetaData()
        history_table = Table(history_table_name, metadata,
                              autoload_with=engine,
                              schema=schema_name)

        # 打印表结构确认
        print(f"表结构: {[c.name for c in history_table.columns]}")

        # 插入数据
        insert_stmt = history_table.insert().values(name=table_name)
        result = connection.execute(insert_stmt)

        # 打印插入结果
        print(f"插入结果: {result.rowcount} 行受影响")
        if result.rowcount == 1:
            print(f"成功插入数据: id={result.inserted_primary_key[0]}, name={table_name}")
        else:
            print("警告: 没有插入任何数据")

    # 验证数据是否真的插入
    with engine.connect() as conn:
        count = conn.execute(text(f"SELECT COUNT(*) FROM {schema_name}.{history_table_name}")).scalar()
        print(f"当前表 {history_table_name} 中共有 {count} 条记录")

    print(f"数据已成功导入到 {schema_name}.{history_table_name}")
except SQLAlchemyError as e:
    print(f"数据导入失败: {e}")
    exit(1)

# #######################################################################
# 导入数据表
# #######################################################################

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



try:
    df.to_sql(
        name=table_name,
        con=engine,
        schema=schema_name,
        if_exists='append',  # 如果表存在，追加数据
        index=False,
        chunksize=1000  # 分批插入提高大文件处理效率
    )
    print(f"数据已成功导入到 {schema_name}.{table_name}")
except SQLAlchemyError as e:
    print(f"数据导入失败: {e}")
    exit(1)

# 关闭数据库连接
finally:
    connection.close()
    print("数据库连接已关闭.")

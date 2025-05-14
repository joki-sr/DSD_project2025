import pandas as pd
import numpy as np
import sys
import os
from datetime import timedelta

def clean_sensor_data(csv_file, frequency=10):
    df = pd.read_csv(csv_file)
    df['时间戳'] = pd.to_datetime(df['时间戳'])
    df = df.drop_duplicates(subset=['时间戳', '设备ID'], keep='first')

    cleaned_data = []

    for device_id, group in df.groupby('设备ID'):
        device_df = group.sort_values('时间戳').copy()
        min_time = device_df['时间戳'].min()
        max_time = device_df['时间戳'].max()
        time_delta = timedelta(seconds=1 / frequency)
        target_times = pd.date_range(start=min_time, end=max_time, freq=time_delta)

        device_df.set_index('时间戳', inplace=True)

        try:
            resampled_df = device_df.reindex(target_times, method='nearest')
        except ValueError as e:
            print(f"设备 {device_id} 处理时出错: {e}", file=sys.stderr)
            continue

        resampled_df.reset_index(inplace=True)
        resampled_df.rename(columns={'index': '时间戳'}, inplace=True)
        resampled_df['设备ID'] = device_id

        cleaned_data.append(resampled_df)

    if cleaned_data:
        final_df = pd.concat(cleaned_data, ignore_index=True)

        # 使用当前目录的 "cleaned" 文件夹
        # 获取脚本所在目录
        script_dir = os.path.dirname(os.path.abspath(__file__))

        # 构建 output 目录：脚本目录下的 "cleaned"
        output_dir = os.path.join(script_dir, "cleaned")
        os.makedirs(output_dir, exist_ok=True)

        # 输出文件完整路径
        output_file = os.path.join(output_dir, f"cleaned_{os.path.basename(csv_file)}")
        final_df.to_csv(output_file, index=False, encoding='utf-8-sig')
#         print(f"CLEANED_FILE:{output_file}")

        os.makedirs(output_dir, exist_ok=True)
        output_file = os.path.join(output_dir, f"cleaned_{os.path.basename(csv_file)}")
        final_df.to_csv(output_file, index=False, encoding='utf-8-sig')
        print(f"CLEANED_FILE:{output_file}")
    else:
        print("没有有效数据", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    print("exec python code...")
    if len(sys.argv) < 2:
        print("用法: python clean.py path/to/file.csv [frequency]", file=sys.stderr)
        sys.exit(1)
    csv_path = sys.argv[1]
    # 若不传入Hz，则设置为2
    freq = int(sys.argv[2]) if len(sys.argv) > 2 else 2
    print(f"freq={freq}")
    clean_sensor_data(csv_path, freq)

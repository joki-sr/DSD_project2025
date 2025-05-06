-- 创建患者报告表
CREATE TABLE IF NOT EXISTS dsd.patient_reports (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(50) NOT NULL,
    report_date DATE NOT NULL,
    report_type VARCHAR(50) NOT NULL,
    summary VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
    motion_range JSON,  -- 存储运动幅度数据
    score JSON,         -- 存储得分数据
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES dsd.patient(id_card)
) CHARACTER SET utf8 COLLATE utf8_unicode_ci;

-- 插入示例数据
INSERT INTO dsd.patient_reports 
(patient_id, report_date, report_type, summary, motion_range, score) VALUES
('123456', '2023-01-15', '步态分析', '正常步态', 
 '[75, 82, 90, 88, 79, 85, 78, 81, 87, 84, 80, 83]', 
 '[8, 9, 10, 9, 8, 9, 8, 9, 10, 9, 8, 9]'),
 
('123456', '2023-03-20', '步态分析', '略有改善', 
 '[78, 85, 92, 90, 82, 88, 80, 84, 89, 86, 83, 85]', 
 '[8, 9, 10, 10, 9, 9, 8, 9, 10, 9, 9, 9]'),
 
('654321', '2023-02-10', '步态分析', '轻度异常', 
 '[70, 75, 82, 80, 72, 78, 74, 76, 81, 79, 73, 77]', 
 '[7, 8, 9, 8, 7, 8, 7, 8, 9, 8, 7, 8]'),
 
('789012', '2023-04-05', '步态分析', '显著进步', 
 '[82, 88, 95, 92, 86, 90, 84, 87, 93, 89, 85, 88]', 
 '[9, 10, 10, 10, 9, 10, 9, 9, 10, 10, 9, 9]');

-- 创建CSV文件的模拟内容 (实际应该是真实文件)
CREATE TABLE IF NOT EXISTS dsd.sentiment_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date VARCHAR(20),
    type VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
    summary VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci,
    motion_range TEXT,  -- 存储为字符串，前端会解析为JSON
    score TEXT          -- 存储为字符串，前端会解析为JSON
) CHARACTER SET utf8 COLLATE utf8_unicode_ci;

-- 插入示例数据
INSERT INTO dsd.sentiment_data
(date, type, summary, motion_range, score) VALUES
('2023-01-15', '步态分析', '正常步态', '[75, 82, 90, 88, 79, 85, 78, 81, 87, 84, 80, 83]', '[8, 9, 10, 9, 8, 9, 8, 9, 10, 9, 8, 9]'),
('2023-02-10', '步态分析', '轻度异常', '[70, 75, 82, 80, 72, 78, 74, 76, 81, 79, 73, 77]', '[7, 8, 9, 8, 7, 8, 7, 8, 9, 8, 7, 8]'),
('2023-03-20', '步态分析', '略有改善', '[78, 85, 92, 90, 82, 88, 80, 84, 89, 86, 83, 85]', '[8, 9, 10, 10, 9, 9, 8, 9, 10, 9, 9, 9]'),
('2023-04-05', '步态分析', '显著进步', '[82, 88, 95, 92, 86, 90, 84, 87, 93, 89, 85, 88]', '[9, 10, 10, 10, 9, 10, 9, 9, 10, 10, 9, 9]'); 
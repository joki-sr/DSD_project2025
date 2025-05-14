package com.example.factorial.src.dataProcess;

import java.io.*;

public class DataManager {
    private final Integer frequency;
    private final String pythonEdition;
    private final String pythonFilePath;

    // 私有构造函数，防止外部直接 new
    private DataManager() {
        // 可以在此初始化 frequency
        frequency = 2;
        pythonEdition = "python";
//        pythonFilePath = "clean_script.py";
        pythonFilePath = "D:\\development\\DSD\\DSD_project2025\\src\\main\\java\\com\\example\\factorial\\src\\dataProcess\\clean_script.py";
    }

    // 使用 volatile 确保多线程下的可见性与禁止指令重排序
    private static volatile DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) { // 第一次检查
            synchronized (DataManager.class) {
                if (instance == null) { // 第二次检查
                    instance = new DataManager();
                }
            }
        }
        return instance;
    }

    public String rawToCleaned(String  rawPath) throws IOException, InterruptedException {
        System.out.println("xxx");
        // 1. 调用 Python 脚本
        ProcessBuilder pb = new ProcessBuilder(
                pythonEdition,
                pythonFilePath,             // 替换为实际脚本路径
                rawPath,
                String.valueOf(frequency)
        );
        System.out.println("python exec args:" + pythonEdition + pythonFilePath + rawPath);
        pb.redirectErrorStream(true);
//        Process process = pb.start();
//        process.waitFor();
        try {
            System.out.println("x111");
            Process process = pb.start();
            System.out.println("x222");

            int exitCode = process.waitFor();  // 等待进程结束
            System.out.println("x333");
            System.out.println("Process exited with code: " + exitCode);
            System.out.println("x444");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            // 可根据需要决定是否中断线程
            Thread.currentThread().interrupt();
        }



        System.out.println("yyy");

        // 替换路径中的 raw 为 cleaned
        String cleanedDir = rawPath.replace("\\raw\\", "\\cleaned\\");
        // 获取文件名
        String fileName = new File(rawPath).getName();
        // 构造最终路径
        String cleanedPath = cleanedDir.replace(fileName, "cleaned_" + fileName);

        System.out.println("rawPath："+rawPath);
        System.out.println("cleanedPath:"+cleanedPath);

        System.out.println("zzz");

        return cleanedPath;
    }


//        public void cleanCsvData(byte[] rawData, byte[] cleanedData) {
//        try {
//            // 1. 写入临时CSV文件
//            File rawFile = File.createTempFile("raw_", ".csv");
//            try (FileOutputStream fos = new FileOutputStream(rawFile)) {
//                fos.write(rawData);
//            }
//
//            // 2. 调用 Python 脚本
//            ProcessBuilder pb = new ProcessBuilder(
//                    pythonEdition,
//                    pythonFilePath,             // 替换为实际脚本路径
//                    rawFile.getAbsolutePath(),
//                    String.valueOf(frequency)
//            );
//            pb.redirectErrorStream(true);
//            Process process = pb.start();
//
//            // 3. 读取 Python 脚本输出
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(process.getInputStream()));
//            String line;
//            String cleanedCsvPath = null;
//            while ((line = reader.readLine()) != null) {
//                System.out.println("[PYTHON] " + line);
//                if (line.startsWith("CLEANED_FILE:")) {
//                    cleanedCsvPath = line.substring("CLEANED_FILE:".length());
//                }
//            }
//
//            int exitCode = process.waitFor();
//            if (exitCode != 0 || cleanedCsvPath == null) {
//                throw new RuntimeException("Python 脚本运行失败");
//            }
//
//            // 4. 读取清洗后的文件内容
//            File resultFile = new File(cleanedCsvPath.trim());
//            byte[] cleanedBytes = java.nio.file.Files.readAllBytes(resultFile.toPath());
//            System.arraycopy(cleanedBytes, 0, cleanedData, 0, cleanedBytes.length);  // 注意长度
//
//            // 可选：删除临时文件
////            rawFile.delete();
////            resultFile.delete();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("清洗失败: " + e.getMessage());
//        }
//    }
}

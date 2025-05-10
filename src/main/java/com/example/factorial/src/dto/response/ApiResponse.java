package com.example.factorial.src.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标准化API响应体
 * @param <T> 响应数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    
    /**
     * 创建成功响应
     * @param data 响应数据
     * @param message 响应消息
     * @param status 状态码
     * @return 响应对象
     */
    public static <T> ApiResponse<T> success(T data, String message, int status) {
        return new ApiResponse<>(status, message, data);
    }
    
    /**
     * 创建标准成功响应 (状态码200)
     * @param data 响应数据
     * @param message 响应消息
     * @return 响应对象
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return success(data, message, 200);
    }
    
    /**
     * 创建成功响应，默认消息 (状态码200)
     * @param data 响应数据
     * @return 响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "操作成功", 200);
    }
    
    /**
     * 创建空数据成功响应 (状态码200)
     * @param message 响应消息
     * @return 响应对象
     */
    public static <T> ApiResponse<T> success(String message) {
        return success(null, message, 200);
    }
    
    /**
     * 创建失败响应
     * @param message 错误消息
     * @param status 状态码
     * @return 响应对象
     */
    public static <T> ApiResponse<T> error(String message, int status) {
        return new ApiResponse<>(status, message, null);
    }
    
    /**
     * 创建失败响应，默认状态码400
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ApiResponse<T> error(String message) {
        return error(message, 400);
    }
    
    /**
     * 创建未授权响应 (状态码401)
     * @return 响应对象
     */
    public static <T> ApiResponse<T> unauthorized() {
        return error("未授权访问", 401);
    }
    
    /**
     * 创建禁止访问响应 (状态码403)
     * @return 响应对象
     */
    public static <T> ApiResponse<T> forbidden() {
        return error("权限不足", 403);
    }
    
    /**
     * 创建资源未找到响应 (状态码404)
     * @param resource 资源描述
     * @return 响应对象
     */
    public static <T> ApiResponse<T> notFound(String resource) {
        return error("未找到指定的" + resource, 404);
    }
    
    /**
     * 创建资源冲突响应 (状态码409)
     * @param message 冲突描述
     * @return 响应对象
     */
    public static <T> ApiResponse<T> conflict(String message) {
        return error(message, 409);
    }
    
    /**
     * 创建服务器错误响应 (状态码500)
     * @return 响应对象
     */
    public static <T> ApiResponse<T> serverError() {
        return error("服务器内部错误", 500);
    }
} 
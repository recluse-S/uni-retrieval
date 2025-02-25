package com.retrieval.pojo;

public class R<T> {
    private int code;          // 状态码
    private String message;    // 提示信息
    private T data;            // 数据

    // 构造函数
    public R() {}

    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Getter 和 Setter 方法
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // 工具方法：成功的结果
    public static <T> R<T> success(T data) {
        return new R<>(200, "Operation successful", data);
    }

    // 工具方法：失败的结果
    public static <T> R<T> failure(String message) {
        return new R<>(500, message, null);
    }
}


package com.ceit.desktop.utils;

/**
 * @ClassName ResultJson
 * @Description TODO
 * @Author hello world
 * @DATE 2021/4/6  20:26
 **/
public class testJson {

    //流水号
    public String data;
    //返回状态码
    public String status;
    //返回信息
    public String message;

    public testJson() {
    }

    public testJson(String data, String status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "testJson{" +
                "data='" + data + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

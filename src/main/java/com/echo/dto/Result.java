package com.echo.dto;

//封装json对象给前端使用
public class Result<T> {
    private boolean status;
    private String errorMsg;
    private Integer errCode;
    private T data;

    //成功时候的构造函数
    public Result(T data, Boolean status) {
        this.data = data;
        this.status = status;
    }

    //错误时候的构造函数
    public Result(Boolean status, String errorMsg) {
        this.status = status;
        this.errCode = errCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

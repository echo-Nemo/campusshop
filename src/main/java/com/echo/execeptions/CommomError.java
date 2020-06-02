package com.echo.execeptions;

public interface CommomError {
    public int getErrCode();

    public String getMsg();

    //自定义错误信息
    public CommomError setMsg(String msg);
}

package com.echo.execeptions;

public class BusinessException extends Exception implements CommomError {
    private CommomError commomError;

    public BusinessException(CommomError commomError) {
        //继承异常类中的信息
        super();
        this.commomError = commomError;
    }

    //自定义的msg
    public BusinessException(CommomError commomError, String msg) {
        this.commomError = commomError;
        commomError.setMsg(msg);
    }


    @Override
    public int getErrCode() {
        return commomError.getErrCode();
    }

    @Override
    public String getMsg() {
        return commomError.getMsg();
    }

    @Override
    public CommomError setMsg(String msg) {
        commomError.setMsg(msg);
        return commomError;
    }
}

package com.echo.dto;

import com.echo.dataobject.WechatAuthDO;

import java.util.List;

public class WechatAuExecution {
    //结果状态
    private int state;

    //状态标识符
    private String stateInfo;

    private int count;
    private WechatAuthDO wechatAuth;

    private List<WechatAuthDO> wechatAuthList;

    public WechatAuExecution(WechatAuthDO wechatAuth, String stateInfo) {
        this.wechatAuth = wechatAuth;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public WechatAuthDO getWechatAuth() {
        return wechatAuth;
    }

    public void setWechatAuth(WechatAuthDO wechatAuth) {
        this.wechatAuth = wechatAuth;
    }

    public List<WechatAuthDO> getWechatAuthList() {
        return wechatAuthList;
    }

    public void setWechatAuthList(List<WechatAuthDO> wechatAuthList) {
        this.wechatAuthList = wechatAuthList;
    }
}

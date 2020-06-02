package com.echo.service;

import com.echo.dataobject.WechatAuthDO;
import com.echo.dto.WechatAuExecution;
import com.echo.execeptions.BusinessException;

public interface WechatAuthService {

    WechatAuthDO getWechatAuthByopenId(String openId);

    //微信账号的注册
    WechatAuExecution register(WechatAuthDO wechatAuth) throws BusinessException;

}

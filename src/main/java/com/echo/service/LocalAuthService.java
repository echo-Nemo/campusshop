package com.echo.service;

import com.echo.dataobject.LocalAuthDO;
import com.echo.execeptions.BusinessException;
import com.echo.util.execution.LocalAuthExecution;

public interface LocalAuthService {

    //注册
    LocalAuthExecution register(LocalAuthDO localAuthDO) throws BusinessException;

    //根据username和password查询
    LocalAuthDO queryByNamePwd(String username, String password);

    //根据userid查询
    LocalAuthDO queryByUserId(Integer userId);

    //修改密码
    LocalAuthExecution modifypwd(Integer userId, String userName, String password, String newPassword) throws BusinessException;

    //绑定微信账号生成平台的专属账号
    LocalAuthExecution localBindWechat(LocalAuthDO localAuthDO) throws BusinessException;


}

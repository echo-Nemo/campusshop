package com.echo.service.Impl;

import com.echo.dao.LocalAuthDOMapper;
import com.echo.dao.OwnerDOMapper;
import com.echo.dataobject.LocalAuthDO;
import com.echo.dataobject.OwnerDO;
import com.echo.execeptions.BusinessException;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.LocalAuthService;
import com.echo.util.execution.LocalAuthExecution;
import com.echo.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    @Autowired
    LocalAuthDOMapper localAuthDOMapper;

    @Autowired
    OwnerDOMapper ownerDOMapper;

    //注册
    @Transactional
    @Override
    public LocalAuthExecution register(LocalAuthDO localAuthDO) throws BusinessException {

        if (localAuthDO == null) {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }

        try {
            localAuthDO.setCreateTime(new Date());
            localAuthDO.setLastEditTime(new Date());
            //对用户的密码进行md5的加密
            localAuthDO.setPassword(Md5Util.getMd5(localAuthDO.getPassword()));

            //先插入personInfo 获取到userId
            if (localAuthDO.getPersonInfo() != null) {
                localAuthDO.getPersonInfo().setCreateTime(new Date());
                localAuthDO.getPersonInfo().setLastEditTime(new Date());
                localAuthDO.getPersonInfo().setEnableStatus(1);

                OwnerDO personInfo = localAuthDO.getPersonInfo();
                int effect = ownerDOMapper.insert(personInfo);
                if (effect < 0) {
                    throw new RuntimeException("personinfo insert failure");
                }
            }

            //进行localauth的注册

            int effectNum = localAuthDOMapper.insert(localAuthDO);

            if (effectNum < 0) {
                throw new RuntimeException("register failure");
            } else {
                return new LocalAuthExecution("success", localAuthDO);
            }
        } catch (Exception e) {
            throw new RuntimeException("register failure" + e.getMessage());
        }
    }


    //通过username和password查询
    @Override
    public LocalAuthDO queryByNamePwd(String username, String password) {
        return localAuthDOMapper.selectByNamePwd(username, password);
    }

    //通过userid查询
    @Override
    public LocalAuthDO queryByUserId(Integer userId) {
        return localAuthDOMapper.selectByUserId(userId);
    }

    //密码的修改
    @Override
    public LocalAuthExecution modifypwd(Integer userId, String userName, String password, String newPassword) throws BusinessException {
        if (userId != null && userName != null && password != null && newPassword != null
                && !newPassword.equals(password)) {
            int result = localAuthDOMapper.updateBypwd(userName, userId, Md5Util.getMd5(password), Md5Util.getMd5(newPassword), new Date());
            if (result < 0) {
                throw new RuntimeException("修改密码失败");
            } else {
                return new LocalAuthExecution("success");
            }
        } else {
            throw new BusinessException(EmBusinessError.LOCAL_AUTH_NULL);
        }
    }

    /*
    和微信账号的绑定
    wechat中userid是唯一的,根据userid插入，
    判断改微信号是否已关联改账号
     */
    @Transactional
    @Override
    public LocalAuthExecution localBindWechat(LocalAuthDO localAuthDO) throws BusinessException {

        //localAuth是根据前端的数据生成的值
        if (localAuthDO == null || localAuthDO.getUsername() == null || localAuthDO.getPassword() == null
                || localAuthDO.getPersonInfo() == null || localAuthDO.getPersonInfo().getUserId() == null) {
            throw new BusinessException(EmBusinessError.LOCAL_AUTH_NULL);
        }
        //获取临时的LocalAuth对象
        LocalAuthDO temLocalAuth = localAuthDOMapper.selectByUserId(localAuthDO.getPersonInfo().getUserId());

        //已绑定改微信号
        if (temLocalAuth != null) {
            return new LocalAuthExecution("the bindwechat only one account");
        }

        //没有绑定微信号
        try {
            localAuthDO.setCreateTime(new Date());
            localAuthDO.setLastEditTime(new Date());
            //userId的设置
            localAuthDO.setUserId(localAuthDO.getPersonInfo().getUserId());
            localAuthDO.setPassword(Md5Util.getMd5(localAuthDO.getPassword()));

            int result = localAuthDOMapper.insertSelective(localAuthDO);

            if (result < 0) {
                throw new RuntimeException("账号绑定失败");
            } else {
                return new LocalAuthExecution("success");
            }
        } catch (Exception e) {
            throw new RuntimeException("insert error" + " " + e.getMessage());
        }
    }

}


package com.echo.service.Impl;

import com.echo.dao.OwnerDOMapper;
import com.echo.dao.WechatAuthDOMapper;
import com.echo.dataobject.OwnerDO;
import com.echo.dataobject.WechatAuthDO;
import com.echo.dto.WechatAuExecution;
import com.echo.execeptions.BusinessException;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.WechatAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {
    @Autowired
    WechatAuthDOMapper wechatAuthDOMapper;

    @Autowired
    OwnerDOMapper ownerDOMapper;

    @Override
    public WechatAuthDO getWechatAuthByopenId(String openId) {
        return wechatAuthDOMapper.selectByOpenid(openId);
    }

    //微信用户的注册
    /*
    wechatAu表和personInfo表是关联的
    首先主表personInfo中的主键先设置值，从表wechatAu再设置值，不然会报错
     */
    @Transactional
    @Override
    public WechatAuExecution register(WechatAuthDO wechatAuth) throws BusinessException {
        if (wechatAuth == null || wechatAuth.getOpenId() == null) {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }

        try {
            wechatAuth.setCreateTime(new Date());

            if (wechatAuth.getPersonInfo() != null && wechatAuth.getPersonInfo().getUserId() == null) {


                try {
                    wechatAuth.getPersonInfo().setCreateTime(new Date());
                    wechatAuth.getPersonInfo().setLastEditTime(new Date());
                    wechatAuth.getPersonInfo().setEnableStatus(1);
                    //这里先把email设置为硬编码
                    wechatAuth.getPersonInfo().setEmail("134234433@qq.com");

                    OwnerDO personInfo = wechatAuth.getPersonInfo();

                    int effectedNum = ownerDOMapper.insertSelective(personInfo);

                    wechatAuth.setUserId(personInfo.getUserId());

                    if (effectedNum <= 0) {
                        throw new RuntimeException("添加用户信息失败");
                    }
                } catch (Exception e) {
                    //log.debug("insertPersonInfo error:" + e.toString());
                    throw new RuntimeException("insertPersonInfo error: "
                            + e.getMessage());
                }
            }
            int effectedNum = wechatAuthDOMapper.insertSelective(wechatAuth);
            if (effectedNum <= 0) {
                throw new RuntimeException("帐号创建失败");
            } else {
                return new WechatAuExecution(
                        wechatAuth, "success");
            }
        } catch (Exception e) {
            //log.debug("insertWechatAuth error:" + e.toString());
            throw new RuntimeException("insertWechatAuth error: "
                    + e.getMessage());
        }
    }
}
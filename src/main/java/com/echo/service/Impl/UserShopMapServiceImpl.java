package com.echo.service.Impl;


import com.echo.dao.UserShopMapDOMapper;
import com.echo.dataobject.UserShopMapDO;
import com.echo.execeptions.BusinessException;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.UserShopMapService;
import com.echo.util.PageCalculate;
import com.echo.util.execution.UserShopExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserShopMapServiceImpl implements UserShopMapService {

    @Autowired
    UserShopMapDOMapper userShopMapDOMapper;

    @Override
    public UserShopExecution getUserShopListByShop(UserShopMapDO userShopMapDO, Integer pageIndex, Integer pageSize) throws BusinessException {
        if (userShopMapDO != null && pageIndex != null && pageSize != null) {
            //beginIndex
            Integer beginIndex = PageCalculate.calculateRowIndex(pageIndex, pageSize);

            List<UserShopMapDO> userShopMapList = userShopMapDOMapper.queryUserShopMapList(userShopMapDO, beginIndex, pageSize);
            Integer count = userShopMapDOMapper.queryUserShopCount(userShopMapDO);

            UserShopExecution userShopExecution = new UserShopExecution();
            userShopExecution.setCount(count);
            userShopExecution.setUserShopMapList(userShopMapList);
            return userShopExecution;
        } else {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
    }

    @Override
    public UserShopMapDO getUserShopMap(Integer userId, Integer shopId) {
        return userShopMapDOMapper.queryUserShopMap(userId, shopId);
    }
}

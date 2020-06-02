package com.echo.service;

import com.echo.dataobject.UserShopMapDO;
import com.echo.execeptions.BusinessException;
import com.echo.util.execution.UserShopExecution;

public interface UserShopMapService {
    /*
    根据条件分页查询用户积分列表
     */
    UserShopExecution getUserShopListByShop(UserShopMapDO userShopMapDO, Integer pageIndex, Integer pageSize) throws BusinessException;

    /*
    根据用户Id 和店铺Id返回某个店铺的积分
     */

    UserShopMapDO getUserShopMap(Integer userId, Integer shopId);
}

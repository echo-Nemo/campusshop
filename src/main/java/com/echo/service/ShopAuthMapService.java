package com.echo.service;

import com.echo.dataobject.ShopAuthMapDO;
import com.echo.execeptions.BusinessException;
import com.echo.util.execution.ShopAuthMapExecution;


public interface ShopAuthMapService {
    //根据shopId分页显示店铺的授权信息
    ShopAuthMapExecution getShopAuthMapListByShopId(Integer shopId, Integer pageIndex, Integer pageSize) throws BusinessException;

    //根据shopId返回信息
    ShopAuthMapDO getShopAuthMapById(Integer AuthShopId);

    //添加授权信息
    ShopAuthMapExecution addShopAuthMap(ShopAuthMapDO shopAuthMap) throws BusinessException;

    //更新授权信息 包阔职位，状态等
    ShopAuthMapExecution modifyShopAuthMap(ShopAuthMapDO shopAuthMapDO);


}

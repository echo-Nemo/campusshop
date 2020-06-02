package com.echo.service;

import com.echo.dataobject.ShopCategoryDO;
import com.echo.execeptions.BusinessException;

import java.util.List;

public interface ShopcategoryService {
    public List<ShopCategoryDO> queryShopCategory(ShopCategoryDO shopCategoryDO) throws BusinessException;

    //根据shopCategory获取实例
    ShopCategoryDO getShopCategory(Integer shopCategoryId);
}

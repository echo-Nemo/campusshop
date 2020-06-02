package com.echo.service.Impl;

import com.echo.dao.ShopCategoryDOMapper;
import com.echo.dataobject.ShopCategoryDO;
import com.echo.execeptions.BusinessException;
import com.echo.service.ShopcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopcategoryService {
    @Autowired
    ShopCategoryDOMapper shopCategoryDOMapper;


    @Override
    public List<ShopCategoryDO> queryShopCategory(ShopCategoryDO shopCategoryDO) throws BusinessException {
        return shopCategoryDOMapper.queryShopcategory(shopCategoryDO);
    }

    @Override
    public ShopCategoryDO getShopCategory(Integer shopCategoryId) {
        return shopCategoryDOMapper.selectByPrimaryKey(shopCategoryId);
    }
}

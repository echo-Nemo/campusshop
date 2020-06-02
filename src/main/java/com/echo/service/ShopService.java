package com.echo.service;

import com.echo.dataobject.ShopDO;
import com.echo.dto.ImageHolder;
import com.echo.execeptions.BusinessException;
import com.echo.util.execution.ShopExecution;

import java.util.List;

public interface ShopService {

    int addShop(ShopDO shop, ImageHolder shopImg) throws BusinessException;

    ShopDO getShopById(Integer shopId) throws BusinessException;

    //更新shop
    ShopDO modifyShop(ShopDO shop, ImageHolder thumbnail) throws BusinessException;

    /*
    店铺列表
   前端只能显示页面数，数据库中只能显示从那条数据开始显示,需要将pageIndex和rowIndex进行转化
    pageIndex:页码数
    pageSize:每页显示的条数
     */
    ShopExecution shopList(ShopDO shop, int pageIndex, int pageSize);

    List<ShopDO> queryShopByShopcategory(Integer shopCategoryId) throws BusinessException;

    List<ShopDO> getShopList(Integer pageIndex, Integer pageSize);

    ShopDO modifyShopStatus(ShopDO shopDO);

}

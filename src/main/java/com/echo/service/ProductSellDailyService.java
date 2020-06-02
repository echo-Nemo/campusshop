package com.echo.service;

import com.echo.dataobject.ProductSellDailyDO;

import java.util.Date;
import java.util.List;

public interface ProductSellDailyService {

    void dailyCalculate();

    //商品的日销售
    List<ProductSellDailyDO> getProductSellDailyList(ProductSellDailyDO productSellDailyDO,
                                                     Date beginTime, Date endTime);

}

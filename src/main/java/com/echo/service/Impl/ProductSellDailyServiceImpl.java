package com.echo.service.Impl;

import com.echo.dao.ProductSellDailyDOMapper;
import com.echo.dataobject.ProductSellDailyDO;
import com.echo.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {

    private static final Logger log = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);

    @Autowired
    ProductSellDailyDOMapper productSellDailyDOMapper;

    @Override
    public void dailyCalculate() {
        //使用quarz
        log.info("QUARZ Running");
        //统计每个店铺的日销售量
        productSellDailyDOMapper.insertProductSellDaily(new ProductSellDailyDO());
        //统计每个店铺 日销售量为0 将它们销量全部置为0，为了迎合echarts的数据请求
        productSellDailyDOMapper.insertDefaultProductSellDaily(new ProductSellDailyDO());
    }

    @Override
    public List<ProductSellDailyDO> getProductSellDailyList(ProductSellDailyDO productSellDailyDO, Date beginTime, Date endTime) {
        //return productSellDailyDOMapper.queryProductSellList(productSellDailyDO, beginTime, endTime);
        return productSellDailyDOMapper.queryProductSellDailyList(productSellDailyDO, beginTime, endTime);
    }
}

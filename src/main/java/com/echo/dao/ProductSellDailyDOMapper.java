package com.echo.dao;

import com.echo.dataobject.ProductSellDailyDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author cheng
 * 2018/4/20 14:39
 */
@Repository
public interface ProductSellDailyDOMapper {

    /**
     * 统计平台所有商品的日销售量
     *
     * @return
     */
    int insertProductSellDaily(ProductSellDailyDO productSellDailyDO);

    /**
     * 统计平台当天没乡销量的商品，补全信息，将销量置为0
     *
     * @return
     */
    int insertDefaultProductSellDaily(ProductSellDailyDO productSellDailyDO);

    /**
     * 根据查询条件返回商品日销售的统计列表
     *
     * @param productSellDailyCondition
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductSellDailyDO> queryProductSellDailyList(
            @Param("productSellDailyCondition") ProductSellDailyDO productSellDailyCondition,
            @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);


}



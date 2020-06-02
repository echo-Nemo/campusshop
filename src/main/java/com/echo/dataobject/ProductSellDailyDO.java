package com.echo.dataobject;

import java.util.Date;

/**
 * 顾客消费的商品映射
 *
 * @author cheng
 * 2018/4/19 11:23
 */
public class ProductSellDailyDO {

    /**
     * 主键id
     */
    private Integer productSellDailyId;

    /**
     * 哪天的销量，精确到天
     */
    private Date createTime;
    /**
     * 销量
     */
    private Integer total;
    /**
     * 商品信息实体类
     */
    private ProductDO product;
    /**
     * 店铺信息实体类
     */
    private ShopDO shop;

    public Integer getProductSellDailyId() {
        return productSellDailyId;
    }

    public void setProductSellDailyId(Integer productSellDailyId) {
        this.productSellDailyId = productSellDailyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public ProductDO getProduct() {
        return product;
    }

    public void setProduct(ProductDO product) {
        this.product = product;
    }

    public ShopDO getShop() {
        return shop;
    }

    public void setShop(ShopDO shop) {
        this.shop = shop;
    }
}

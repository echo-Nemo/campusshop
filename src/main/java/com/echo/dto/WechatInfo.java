package com.echo.dto;

import java.util.Date;

/*
用于接收二维码的信息
 */
public class WechatInfo {
    private Integer customerId;
    private Integer productId;
    private Integer userAwardId;
    private Integer shopId;
    //二维码的创建时间
    private Long createTime;
    private Integer flage;

    public Integer getFlage() {
        return flage;
    }

    public void setFlage(Integer flage) {
        this.flage = flage;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserAwardId() {
        return userAwardId;
    }

    public void setUserAwardId(Integer userAwardId) {
        this.userAwardId = userAwardId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}

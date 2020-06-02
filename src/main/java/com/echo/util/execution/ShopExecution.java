package com.echo.util.execution;

import com.echo.dataobject.ShopDO;

import java.util.List;

public class ShopExecution {
    private List<ShopDO> shopList;
    //店铺的数量
    private int count;
    //结果状态
    private int status;

    public List<ShopDO> getShopList() {
        return shopList;
    }

    public void setShopList(List<ShopDO> shopList) {
        this.shopList = shopList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

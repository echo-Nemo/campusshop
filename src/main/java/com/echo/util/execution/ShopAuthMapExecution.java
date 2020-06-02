package com.echo.util.execution;

import com.echo.dataobject.ShopAuthMapDO;

import java.util.List;

public class ShopAuthMapExecution {
    ShopAuthMapDO shopAuthMap;
    List<ShopAuthMapDO> shopAuthMapList;
    String states;
    Integer count;

    public ShopAuthMapExecution() {
    }

    public ShopAuthMapExecution(ShopAuthMapDO shopAuthMap, String states) {
        this.shopAuthMap = shopAuthMap;
        this.states = states;
    }

    public ShopAuthMapExecution(List<ShopAuthMapDO> shopAuthMapList, String states) {
        this.shopAuthMapList = shopAuthMapList;
        this.states = states;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ShopAuthMapDO getShopAuthMap() {
        return shopAuthMap;
    }

    public void setShopAuthMap(ShopAuthMapDO shopAuthMap) {
        this.shopAuthMap = shopAuthMap;
    }

    public List<ShopAuthMapDO> getShopAuthMapList() {
        return shopAuthMapList;
    }

    public void setShopAuthMapList(List<ShopAuthMapDO> shopAuthMapList) {
        this.shopAuthMapList = shopAuthMapList;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }
}

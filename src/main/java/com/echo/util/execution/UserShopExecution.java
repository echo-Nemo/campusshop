package com.echo.util.execution;

import com.echo.dataobject.UserShopMapDO;

import java.util.List;

public class UserShopExecution {
    private UserShopMapDO userShopMapDO;
    private Integer count;
    private List<UserShopMapDO> userShopMapList;
    private String status;

    public UserShopMapDO getUserShopMapDO() {
        return userShopMapDO;
    }

    public void setUserShopMapDO(UserShopMapDO userShopMapDO) {
        this.userShopMapDO = userShopMapDO;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<UserShopMapDO> getUserShopMapList() {
        return userShopMapList;
    }

    public void setUserShopMapList(List<UserShopMapDO> userShopMapList) {
        this.userShopMapList = userShopMapList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

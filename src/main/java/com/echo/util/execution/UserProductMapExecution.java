package com.echo.util.execution;

import com.echo.dataobject.UserProductMapDO;

import java.util.List;

public class UserProductMapExecution {
    private UserProductMapDO userProductMapDO;
    private Integer count;
    private String status;
    private List<UserProductMapDO> userProductMapList;

    public UserProductMapExecution(String status) {
        this.status = status;
    }

    public  UserProductMapExecution(){

    }

    public UserProductMapExecution(UserProductMapDO userProductMapDO, String status) {
        this.userProductMapDO = userProductMapDO;
        this.status = status;
    }

    public UserProductMapExecution(List<UserProductMapDO> userProductMapList, String status) {
        this.userProductMapList = userProductMapList;
        this.status = status;
    }

    public UserProductMapDO getUserProductMapDO() {
        return userProductMapDO;
    }

    public void setUserProductMapDO(UserProductMapDO userProductMapDO) {
        this.userProductMapDO = userProductMapDO;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UserProductMapDO> getUserProductMapList() {
        return userProductMapList;
    }

    public void setUserProductMapList(List<UserProductMapDO> userProductMapList) {
        this.userProductMapList = userProductMapList;
    }
}

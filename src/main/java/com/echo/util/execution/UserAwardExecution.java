package com.echo.util.execution;

import com.echo.dataobject.UserAwardMapDO;

import java.util.List;

public class UserAwardExecution {
    private UserAwardMapDO userAwardMapDO;
    private Integer count;
    private List<UserAwardMapDO> userAwardMapDOList;
    private String status;

    public UserAwardMapDO getUserAwardMapDO() {
        return userAwardMapDO;
    }

    public void setUserAwardMapDO(UserAwardMapDO userAwardMapDO) {
        this.userAwardMapDO = userAwardMapDO;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<UserAwardMapDO> getUserAwardMapDOList() {
        return userAwardMapDOList;
    }

    public void setUserAwardMapDOList(List<UserAwardMapDO> userAwardMapDOList) {
        this.userAwardMapDOList = userAwardMapDOList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

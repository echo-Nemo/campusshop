package com.echo.util.execution;

import com.echo.dataobject.LocalAuthDO;

public class LocalAuthExecution {
    private String states;
    private LocalAuthDO localAuthDO;
    private int count;

    public LocalAuthExecution(String states, LocalAuthDO localAuthDO) {
        this.states = states;
        this.localAuthDO = localAuthDO;
    }

    public LocalAuthExecution(String states) {
        this.states = states;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public LocalAuthDO getLocalAuthDO() {
        return localAuthDO;
    }

    public void setLocalAuthDO(LocalAuthDO localAuthDO) {
        this.localAuthDO = localAuthDO;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

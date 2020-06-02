package com.echo.util.execution;

import com.echo.dataobject.AwardDO;

import java.util.List;

public class AwardExecution {
    private Integer count;
    private AwardDO awardDO;
    private List<AwardDO> awardList;
    private String status;


    public AwardExecution() {

    }

    public AwardExecution(AwardDO awardDO, String status) {
        this.awardDO = awardDO;
        this.status = status;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public AwardDO getAwardDO() {
        return awardDO;
    }

    public void setAwardDO(AwardDO awardDO) {
        this.awardDO = awardDO;
    }

    public List<AwardDO> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<AwardDO> awardList) {
        this.awardList = awardList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

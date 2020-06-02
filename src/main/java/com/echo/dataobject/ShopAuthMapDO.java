package com.echo.dataobject;

import java.util.Date;

//店铺授权
public class ShopAuthMapDO {
    //主键
    private Integer shopAuthId;

    //职称名
    private String title;

    //职称符号(用于权限控制)  0(店家本身不能再修改)表示无效，1表示有效
    private Integer titleFlag;

    //授权的有效状态
    private Integer enableStatus;

    private Date createTime;

    private Date lastEditTime;

    //员工实体类
    private OwnerDO employee;
    //店铺实体类
    private ShopDO shop;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public Integer getShopAuthId() {
        return shopAuthId;
    }

    public void setShopAuthId(Integer shopAuthId) {
        this.shopAuthId = shopAuthId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTitleFlag() {
        return titleFlag;
    }

    public void setTitleFlag(Integer titleFlag) {
        this.titleFlag = titleFlag;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }


    public OwnerDO getEmployee() {
        return employee;
    }

    public void setEmployee(OwnerDO employee) {
        this.employee = employee;
    }

    public ShopDO getShop() {
        return shop;
    }

    public void setShop(ShopDO shop) {
        this.shop = shop;
    }

    @Override
    public String toString() {
        return "ShopAuthMapDO{" +
                "shopAuthId=" + shopAuthId +
                ", title='" + title + '\'' +
                ", titleFlag=" + titleFlag +
                ", enableStatus=" + enableStatus +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                ", employee=" + employee +
                ", shop=" + shop +
                '}';
    }
}

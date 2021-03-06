package com.echo.dataobject;

import java.util.Date;

public class ShopDO {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.shop_id
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private Integer shopId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.owner_id
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private Integer ownerId;


    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.area_id
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    //private Integer areaId;
    private AreaDO area;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.shop_category_id
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    // private Integer shopCategoryId;
    private ShopCategoryDO shopCategory;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.parent_category_id
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private Integer parentCategoryId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.shop_name
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private String shopName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.shop_desc
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private String shopDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.shop_addr
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private String shopAddr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.phone
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private String phone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.shop_img
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private String shopImg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.priority
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private Integer priority;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.create_time
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.last_edit_time
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private Date lastEditTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.enable_status
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private Integer enableStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_shop.advice
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    private String advice;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.shop_id
     *
     * @return the value of tb_shop.shop_id
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */


    public Integer getShopId() {
        return shopId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.shop_id
     *
     * @param shopId the value for tb_shop.shop_id
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.owner_id
     *
     * @return the value of tb_shop.owner_id
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public Integer getOwnerId() {
        return ownerId;
    }

    //设置店铺授权登入
    private ShopAuthMapDO shopAuthMap;

    public ShopAuthMapDO getShopAuthMap() {
        return shopAuthMap;
    }

    public void setShopAuthMap(ShopAuthMapDO shopAuthMap) {
        this.shopAuthMap = shopAuthMap;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.owner_id
     *
     * @param ownerId the value for tb_shop.owner_id
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public AreaDO getArea() {
        return area;
    }

    public void setArea(AreaDO area) {
        this.area = area;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.area_id
     *
     * @return the value of tb_shop.area_id
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
//    public Integer getAreaId() {
//        return areaId;
//    }
    public ShopCategoryDO getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(ShopCategoryDO shopCategory) {
        this.shopCategory = shopCategory;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.area_id
     *
     * @param areaId the value for tb_shop.area_id
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
//    public void setAreaId(Integer areaId) {
//        this.areaId = areaId;
//    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.shop_category_id
     *
     * @return the value of tb_shop.shop_category_id
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
//    public Integer getShopCategoryId() {
//        return shopCategoryId;
//    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.shop_category_id
     *
     * @param shopCategoryId the value for tb_shop.shop_category_id
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
//    public void setShopCategoryId(Integer shopCategoryId) {
//        this.shopCategoryId = shopCategoryId;
//    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.parent_category_id
     *
     * @return the value of tb_shop.parent_category_id
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.parent_category_id
     *
     * @param parentCategoryId the value for tb_shop.parent_category_id
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.shop_name
     *
     * @return the value of tb_shop.shop_name
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.shop_name
     *
     * @param shopName the value for tb_shop.shop_name
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.shop_desc
     *
     * @return the value of tb_shop.shop_desc
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public String getShopDesc() {
        return shopDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.shop_desc
     *
     * @param shopDesc the value for tb_shop.shop_desc
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc == null ? null : shopDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.shop_addr
     *
     * @return the value of tb_shop.shop_addr
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public String getShopAddr() {
        return shopAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.shop_addr
     *
     * @param shopAddr the value for tb_shop.shop_addr
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setShopAddr(String shopAddr) {
        this.shopAddr = shopAddr == null ? null : shopAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.phone
     *
     * @return the value of tb_shop.phone
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.phone
     *
     * @param phone the value for tb_shop.phone
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.shop_img
     *
     * @return the value of tb_shop.shop_img
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public String getShopImg() {
        return shopImg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.shop_img
     *
     * @param shopImg the value for tb_shop.shop_img
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setShopImg(String shopImg) {
        this.shopImg = shopImg == null ? null : shopImg.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.priority
     *
     * @return the value of tb_shop.priority
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.priority
     *
     * @param priority the value for tb_shop.priority
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.create_time
     *
     * @return the value of tb_shop.create_time
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.create_time
     *
     * @param createTime the value for tb_shop.create_time
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.last_edit_time
     *
     * @return the value of tb_shop.last_edit_time
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public Date getLastEditTime() {
        return lastEditTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.last_edit_time
     *
     * @param lastEditTime the value for tb_shop.last_edit_time
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.enable_status
     *
     * @return the value of tb_shop.enable_status
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public Integer getEnableStatus() {
        return enableStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.enable_status
     *
     * @param enableStatus the value for tb_shop.enable_status
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_shop.advice
     *
     * @return the value of tb_shop.advice
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public String getAdvice() {
        return advice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_shop.advice
     *
     * @param advice the value for tb_shop.advice
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    public void setAdvice(String advice) {
        this.advice = advice == null ? null : advice.trim();
    }

    @Override
    public String toString() {
        return "ShopDO{" +
                "shopId=" + shopId +
                ", ownerId=" + ownerId +
                ", area=" + area +
                ", shopCategory=" + shopCategory +
                ", parentCategoryId=" + parentCategoryId +
                ", shopName='" + shopName + '\'' +
                ", shopDesc='" + shopDesc + '\'' +
                ", shopAddr='" + shopAddr + '\'' +
                ", phone='" + phone + '\'' +
                ", shopImg='" + shopImg + '\'' +
                ", priority=" + priority +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                ", enableStatus=" + enableStatus +
                ", advice='" + advice + '\'' +
                ", shopAuthMap=" + shopAuthMap +
                '}';
    }
}
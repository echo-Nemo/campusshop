package com.echo.execeptions;

public enum EmBusinessError implements CommomError {

    CHECK(0, "审核中"),
    OFF_LINE(-1, "非法商铺"),
    SUCCESS(1, "注册成功"),
    PASS(2, "通过认证"),
    INNER_ERROE(-1001, "操作失败"),
    NULL_SHOPID(-1002, "ShopId为空"),
    NULL_IMAGE(-1003, "该图片不存在"),
    NULL_SHOP_INFO(-1004, "店铺不存在"),
    NULL_ERROR(-1005, "该值不存在"),
    PRODUCT_NULL_ERROR(-2001, "商品不存在"),
    PRODUCT_UPDATE_FAIL(-2002, "商品类别更新失败"),
    WECHAT_REGISTER_FAIL(-3001, "微信用户注册失败"),
    LOCAL_AUTH_NULL(-4001, "属性值为NULL"),
    SHOP_AUTH_NULL(-5001, "属性值为空"),
    SHOP_AUTH_FAILURE(-5002, "店铺授权失败"),
    USER_AWARD_NULL(-6001, "在该店铺的没有积分"),
    USER_AWARD_NOT_ENOUGH(-6002, "积分不足");

    private int errorCode;
    private String errorMsg;

    EmBusinessError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public int getErrCode() {
        return this.errorCode;
    }

    @Override
    public String getMsg() {
        return this.errorMsg;
    }

    @Override
    public CommomError setMsg(String msg) {
        this.errorMsg = msg;
        return this;
    }
}

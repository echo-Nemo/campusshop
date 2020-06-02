package com.echo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccessToken {
    //获取到凭证
    @JsonProperty("access_token")
    private String accessToken;

    //凭证的有效时间
    @JsonProperty("expires_in")
    private String expiresin;

    //更新令牌，获取下一次的访问令牌
    @JsonProperty("refresh_token")
    private String refreshToken;

    //公众号下的身份标识
    @JsonProperty("openid")
    private String openId;

    //权限范围
    @JsonProperty("scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresin() {
        return expiresin;
    }

    public void setExpiresin(String expiresin) {
        this.expiresin = expiresin;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "accessToken:" + this.getAccessToken() + ",openId:" + this.getOpenId();
    }
}

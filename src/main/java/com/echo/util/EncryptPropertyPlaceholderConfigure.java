package com.echo.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigure extends PropertyPlaceholderConfigurer {

    //要加密的值
    private String[] encryptPropNames = {"jdbc.username", "jdbc.password"};

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            String descryptValue = DESUtils.getDescryptString(propertyName);
            return descryptValue;
        } else {
            return propertyValue;
        }
    }

    //判断要加密的对象
    private boolean isEncryptProp(String propertyName) {

        for (String encryptName : encryptPropNames) {
            if (encryptName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
}

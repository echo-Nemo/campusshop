package com.echo.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
    /*
    验证码的判断
     */
    public static Boolean checkVerfyCode(HttpServletRequest request) {
        //期望生成的验证码
        String verifyCode = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String verfyCodeActual = HttpServletUtils.getString(request, "verifyCodeActual");
        if (verfyCodeActual == null || !verfyCodeActual.equals(verifyCode)) {
            return false;
        }
        return true;
    }
}

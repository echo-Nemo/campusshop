package com.echo.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletUtils {
    /*
    对HttpServlet中的参数进行转化
     */
    public static Integer getInteger(HttpServletRequest request, String key) {
        try {
            return Integer.decode(request.getParameter(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public static Double getDouble(HttpServletRequest request, String key) {
        try {
            return Double.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1d;
        }
    }

    public static Long getLong(HttpServletRequest request, String key) {
        try {
            return Long.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return -1L;
        }
    }

    public static Boolean getBoolean(HttpServletRequest request, String key) {
        try {
            return Boolean.valueOf(request.getParameter(key));
        } catch (Exception e) {
            return false;
        }
    }

    //去掉字符串两端的空格
    public static String getString(HttpServletRequest request, String key) {
        try {
            String result = request.getParameter(key);

            if (request != null) {
                result = result.trim();
            }

            if ("".equals(result)) {
                result = null;
            }
            return result;

        } catch (Exception e) {
            return null;
        }
    }


}

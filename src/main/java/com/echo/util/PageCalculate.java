package com.echo.util;

public class PageCalculate {
    //从那条数据开始显示
    public static int calculateRowIndex(int pageIndex, int pageSize) {
        return (pageIndex > 0) ? (pageIndex - 1) * pageSize : 0;
    }
}

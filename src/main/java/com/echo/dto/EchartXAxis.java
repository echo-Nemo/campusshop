package com.echo.dto;

import java.util.HashSet;
import java.util.TreeSet;

public class EchartXAxis {
    /*
    echart表中X轴中的数据 日期(周一到周五 去重)
     */
    private String type = "category";

    //去重且有序
    TreeSet<String> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TreeSet<String> getData() {
        return data;
    }

    public void setData(TreeSet<String> data) {
        this.data = data;
    }
}

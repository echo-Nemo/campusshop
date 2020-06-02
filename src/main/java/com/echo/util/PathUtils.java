package com.echo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtils {
    //获取文件的分隔符
    private static String seperator = System.getProperty("file.separator");
    private static String winPath;
    private static String linuxPath;
    private static String shopPath;

    @Value("${win.base.path}")
    public void setWinPath(String winPath) {
        PathUtils.winPath = winPath;
    }

    @Value("${linux.base.path}")
    public void setLinuxPath(String linuxPath) {
        PathUtils.linuxPath = linuxPath;
    }

    @Value("${shop.relevant.path}")
    public void setShopPath(String shopPath) {
        PathUtils.shopPath = shopPath;
    }

    //返回项目的根路径
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {
            basePath = winPath;
        } else {
            basePath = linuxPath;
        }
        basePath = basePath.replace("/", seperator);
        return basePath;
    }

    //图片的存放路径
    public static String getShopImgPath(Integer shopId) {
        System.out.println("=======" + linuxPath + "====");
        String imagePath = shopPath + shopId + seperator;
        return imagePath.replace("/", seperator);
    }

// ssm中这样设置路径
//    //项目中图片的根路径
//    public static String getImgBasePath() {
//
//        //获取系统的名字
//        String os = System.getProperty("os.name");
//        String basePath = "";
//
//        //windows
//        if (os.toLowerCase().startsWith("win")) {
//            basePath = "D:/javaEE/ShopImgs";
//        } else {
//            //linux 或其它的系统
//            basePath = "/home/ShopImg/Imgs";
//        }
//        //将'/'换成相应系统的分隔符
//        basePath = basePath.replace("/", seperator);
//        return basePath;
//    }
//
//    //项目中图片的子路径
//    public static String getShopImgPath(int shopId) {
//        String imgPath = "/upload/item/shop" + shopId + "/";
//        return imgPath.replace("/", seperator);
//    }
}

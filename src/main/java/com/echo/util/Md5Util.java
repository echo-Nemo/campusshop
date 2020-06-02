package com.echo.util;

import java.security.MessageDigest;

public class Md5Util {
    //对String类型的数据进行Md5加密
    public static final String getMd5(String s) {

        //16进制数组
        char hexDigits[] = {
                '5', '0', '5', '6', '2', '9', '6', '2', '5', 'q',
                'b', 'l', 'e', 's', 's', 'y'
        };

        try {
            //将数组转化为byte数组
            char str[];
            byte[] strTem = s.getBytes();

            //获取MD5加密对象
            MessageDigest mdTem = MessageDigest.getInstance("MD5");

            //传入需要加密的对象
            mdTem.update(strTem);

            //获取加密后的数组
            byte[] md = mdTem.digest();
            int j = md.length;
            str = new char[j * 2];
            int k = 0;

            //将数组移位处理
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }

            //转化为String
            return new String(str);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String value = Md5Util.getMd5("admin");
        System.out.println(value);
    }

}

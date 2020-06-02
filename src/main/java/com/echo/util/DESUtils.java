package com.echo.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

/*
用于将关键数据进行加密
 */
public class DESUtils {
    private static Key key;
    private static String KER_STR = "myKey";
    private static String CHARSETNAME = "UTF-8";
    private static String ALGORITHM = "DES";

    static {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(KER_STR.getBytes());
            generator.init(secureRandom);
            key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //加密
    public static String getEncryptString(String str) {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {
            byte[] bytes = str.getBytes(CHARSETNAME);
            //算法的实例对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(bytes);
            return base64Encoder.encode(doFinal);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //解密
    public static String getDescryptString(String str) {
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] bytes = base64Decoder.decodeBuffer(str);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(bytes);
            return new String(doFinal, CHARSETNAME);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) {
//        System.out.println(getEncryptString("root"));
//        System.out.println(getEncryptString("123456"));
        System.out.println(getDescryptString("WnplV/ietfQ="));
        System.out.println(getDescryptString("QAHlVoUc49w="));
    }

}

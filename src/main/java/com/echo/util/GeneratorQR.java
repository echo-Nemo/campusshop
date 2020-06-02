package com.echo.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/*
二维码的生成
 */
public class GeneratorQR {
    /*
    content:url 地址
     */

    public static BitMatrix generatorQRCodeStream(String content, HttpServletResponse resp) {
        //给响应添加头部信息，告诉浏览器返回的是图片流
        resp.setHeader("Cache-Control", "no-store"); // 不要存储
        resp.setHeader("Pragma", "no-cache"); // 不要缓存
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/png");
        //设置图片的文字编码以及内边眶距
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix;
        try {
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);

        } catch (Exception e) {
            e.getMessage();
            return null;
        }
        return bitMatrix;
    }
}

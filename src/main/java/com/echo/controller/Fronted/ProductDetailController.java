package com.echo.controller.Fronted;


import com.echo.dataobject.OwnerDO;
import com.echo.dataobject.ProductDO;
import com.echo.service.ProductService;
import com.echo.util.GeneratorQR;
import com.echo.util.HttpServletUtils;
import com.echo.util.wechat.ShortNetAddress;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "frontend", method = RequestMethod.GET)
public class ProductDetailController {

    @Autowired
    ProductService productService;

    @RequestMapping(value = "getproductdetail", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getProductdetail(HttpServletRequest request) {
        Map<String, Object> modelmap = new HashMap<>();
        Integer productId = HttpServletUtils.getInteger(request, "productId");

        if (productId < 0) {
            modelmap.put("status", false);
            modelmap.put("errMsg", "传入的值不合法");
            return modelmap;
        } else {
            ProductDO product = productService.searchProduct(productId);

            //根据用户是否存在进行二维码的扫描
            OwnerDO user = (OwnerDO) request.getSession().getAttribute("user");
            if (user != null) {
                modelmap.put("needQRCode", true);
            } else {
                modelmap.put("needQRCode", false);
            }
            modelmap.put("status", true);
            modelmap.put("product", product);
            return modelmap;
        }
    }

    //二维码的生成
    //获取微信用户信息api的前缀
    private static String urlPrefix;
    //获取微信用户信息api的中间部分
    private static String urlMiddle;
    //微信用户信息api的后缀部分
    private static String urlSuffix;

    //用户和店铺的信息
    private static String productMapUrl;

    @Value("${wechat.prefix}")

    public void setUrlPrefix(String urlPrefix) {
        ProductDetailController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ProductDetailController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ProductDetailController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.productmap.url}")
    public void setProductMapUrl(String productMapUrl) {
        ProductDetailController.productMapUrl = productMapUrl;
    }


    /**
     * 生成二维码图片流并返回给前端   userProductMapUrl
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/generateqrcode4product", method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4Product(HttpServletRequest request, HttpServletResponse response) {
        Logger logger = LoggerFactory.getLogger(ProductDetailController.class);

        Integer productId = HttpServletUtils.getInteger(request, "productId");

        OwnerDO user = (OwnerDO) request.getSession().getAttribute("user");

        if (productId != -1 && user != null && user.getUserId() != null) {
            //获取时间戳用于有效性验证
            long timeStamp = System.currentTimeMillis();
            //设置二维码内容
            //冗余aaa为了后期替换
            String content = "{aaaproductIdaaa:" + productId + ",aaacustomerIdaaa:"
                    + user.getUserId() + ",aaacreateTimeaaa:" + timeStamp + "}";
            // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
            try {
                String longUrl = urlPrefix + productMapUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;

                //获取短链接 设置时间为长期有效
                //String shortUrl = ShortNetAddress.createShortUrl(longUrl, "long-term");
                String shortUrl = "https://dwz.cn/3y2YVhGe";

                BitMatrix bitMatrix = GeneratorQR.generatorQRCodeStream(longUrl, response);

                //将二维码发送到前端

                MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
            } catch (Exception e) {
                logger.error("二维码创建失败");
            }
        }
    }

}

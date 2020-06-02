package com.echo.controller.ShopAdmin;


import com.echo.dataobject.*;
import com.echo.dto.EchartXAxis;
import com.echo.dto.UserAccessToken;
import com.echo.dto.WechatInfo;
import com.echo.execeptions.BusinessException;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.*;
import com.echo.util.HttpServletUtils;
import com.echo.util.RedisConfig.RedisDao;
import com.echo.util.execution.ShopAuthMapExecution;
import com.echo.util.execution.UserProductMapExecution;
import com.echo.util.wechat.WechatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "shopadmin")
public class UserProductManageController {

    @Autowired
    ProductService productService;

    @Autowired
    ShopAuthMapService shopAuthMapService;

    @Autowired
    WechatAuthService wechatAuthService;

    @Autowired
    UserProductMapService userProductMapService;

    @Autowired
    OwnerService ownerService;

    @Autowired
    RedisDao redisDao;

    /*
    店员通过给顾客扫描二位码 进行商品的卖出
     */

    Logger logger = LoggerFactory.getLogger(UserProductManageController.class);


    @RequestMapping(value = "adduserproductmap", method = RequestMethod.GET)
    public String adduserproductmap(HttpServletRequest request) throws Exception {
        Map<String, Object> modelMap = new HashMap<>();

        //获取微信用户的信息
        WechatAuthDO wechatAuth = getEmployeeInfo(request);

        if (null != wechatAuth) {
            //获取操作员的信息
            OwnerDO operator = wechatAuth.getPersonInfo();
            request.getSession().setAttribute("user", operator);

            //解析微信回传的二维码  并将contend的内容进行解码
            String qrCodeinfo = null;
            try {
                qrCodeinfo = new String(URLDecoder.decode(HttpServletUtils.getString(request, "state"), "UTF-8"));
            } catch (Exception e) {
                return "InfoMap/operationfail";
            }

            ObjectMapper mapper = new ObjectMapper();
            //微信操作的类进行封装
            WechatInfo wechatInfo = null;

            try {
                //获取二维码中的信息
                wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
            } catch (Exception e) {
                return "InfoMap/operationfail";
            }

            //判断二维码是否过期
            if (checkQRCodeInfo(wechatInfo)) {
                return "InfoMap/operationfail";
            }

            Integer productId = wechatInfo.getProductId();
            Integer customerId = wechatInfo.getCustomerId();

            UserProductMapDO userProductMapDO = compactUserProduct(customerId, productId, wechatAuth.getPersonInfo());

            if (productId != null && customerId != null) {
                //检查操作员是否有权限
                if (!checkShopAuth(operator.getUserId(), userProductMapDO)) {
                    return "InfoMap/operationfail";
                }
                //新增userProduct信息
                UserProductMapExecution userProductMapExecution = userProductMapService.addUserProductMap(userProductMapDO);
                if ("success".equals(userProductMapExecution.getStatus())) {
                    return "InfoMap/operationsuccess";
                } else {
                    return "InfoMap/operationfail";
                }
            } else {
                return "InfoMap/operationfail";
            }
        } else {
            return "InfoMap/operationfail";
        }
    }

/*
    判断微信二维码是否失效
     */

    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        if (wechatInfo != null && wechatInfo.getShopId() != null && wechatInfo.getCreateTime() != null) {
            //获取当前时间
            Long nowTime = System.currentTimeMillis();

            //设置二维码的有效时间为10分钟
//            if ((nowTime - wechatInfo.getCreateTime()) <= 600000) {
//                return true;
//            } else {
//                return false;
//            }
            return true;
        } else {
            return false;
        }
    }

    /*
    根据微信回传回来的code 获取用户信息
     */
    private WechatAuthDO getEmployeeInfo(HttpServletRequest request) {
        String keys = "CODE_KEY";
        String code = "";

        WechatUtil wechatUtil = new WechatUtil();

        //String code = request.getParameter("code");

        //将code放在缓存中
        String codeStr = redisDao.getValue(keys);

        if (codeStr != null) {
            code = codeStr;
        } else {
            code = request.getParameter("code");
            redisDao.setKey(keys, code);
        }


        WechatAuthDO wechatAuth = null;

        try {
            if (code != null) {
                UserAccessToken token;
                token = wechatUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId", openId);
                wechatAuth = wechatAuthService.getWechatAuthByopenId(openId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wechatAuth;
    }


    //检查操作员的资格 判断 该店员是合格
    public Boolean checkShopAuth(Integer userId, UserProductMapDO userProductMap) throws BusinessException {
        try {
            //获取某个店铺下的所有操作员
            ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.getShopAuthMapListByShopId(userProductMap.getShop().getShopId(), 0, 999);

            for (ShopAuthMapDO shopAuthMapDO : shopAuthMapExecution.getShopAuthMapList()) {
                if (userId.equals(shopAuthMapDO.getEmployee().getUserId())) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.INNER_ERROE);
        }
        return false;
    }


    //创建  userProduct信息
    public UserProductMapDO compactUserProduct(Integer customerId, Integer productId, OwnerDO operator) {

        UserProductMapDO userProductMap = new UserProductMapDO();

        if (customerId != null && productId != null && operator != null) {

            userProductMap.setOperator(operator);
            ProductDO product = new ProductDO();

            //根据productId获取 product对象  获取该product的积分
            product = productService.searchProduct(productId);
            userProductMap.setProduct(product);
            userProductMap.setPoint(product.getPoint());

            //根据product获取shop信息
            userProductMap.setShop(product.getShop());

            OwnerDO customer = new OwnerDO();
            //customer.setUserId(customerId);
            customer = ownerService.selectOwner(customerId);
            userProductMap.setUser(customer);
            userProductMap.setCreateTime(new Date());
            userProductMap.setOperator(operator);
            return userProductMap;
        } else {
            return null;
        }
    }

}

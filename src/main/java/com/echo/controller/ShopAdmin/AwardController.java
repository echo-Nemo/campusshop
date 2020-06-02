package com.echo.controller.ShopAdmin;

import com.echo.dataobject.OwnerDO;
import com.echo.dataobject.ShopAuthMapDO;
import com.echo.dataobject.UserAwardMapDO;
import com.echo.dataobject.WechatAuthDO;
import com.echo.dto.UserAccessToken;
import com.echo.dto.WechatInfo;
import com.echo.service.ShopAuthMapService;
import com.echo.service.UserAwardService;
import com.echo.service.WechatAuthService;
import com.echo.util.HttpServletUtils;
import com.echo.util.RedisConfig.RedisDao;
import com.echo.util.execution.ShopAuthMapExecution;
import com.echo.util.execution.UserAwardExecution;
import com.echo.util.wechat.WechatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

@Controller
@RequestMapping(value = "shopadmin")
public class AwardController {

    @Autowired
    UserAwardService userAwardService;

    @Autowired
    RedisDao redisDao;

    @Autowired
    WechatAuthService wechatAuthService;

    @Autowired
    ShopAuthMapService shopAuthMapService;


    /*
    该二维码的生成在 frontendShopAwardController中
     */
    //前端的奖品的兑换
    @RequestMapping(value = "exchangeaward", method = RequestMethod.GET)
    public String exchageAward(HttpServletRequest request) {
        WechatAuthDO wechatAuth = null;
        WechatInfo wechatInfo = null;
        OwnerDO operator = null;
        String qrCodeinfo = "";

        try {
            //获取微信用户的信息
            wechatAuth = getEmployeeInfo(request);
            if (wechatAuth != null) {
                operator = wechatAuth.getPersonInfo();
                //获取到操作员信息
                request.getSession().setAttribute("user", operator);
                qrCodeinfo = new String(URLDecoder.decode(HttpServletUtils.getString(request, "state"), "UTF-8"));
            }
        } catch (Exception e) {
            return "InfoMap/operationfail";
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
        } catch (Exception e) {
            return "InfoMap/operationfail";
        }


        Integer userAwardId = wechatInfo.getUserAwardId();
        Integer customerId = wechatInfo.getCustomerId();
        UserAwardMapDO userAwardMap = compactUserAwardMap(customerId, userAwardId, operator);

        if (userAwardId != null && customerId != null) {
            try {
                //权限的校验
                if (!checkOperator(operator.getUserId(), userAwardMap)) {
                    return "InfoMap/operationfail";
                }

                UserAwardExecution userAwardExecution = userAwardService.modifyUserAwardMap(userAwardMap);

                if ("success".equals(userAwardExecution.getStatus())) {
                    return "InfoMap/operationsuccess";
                } else {
                    return "InfoMap/operationfail";
                }
            } catch (Exception e) {
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

//            //设置二维码的有效时间为10分钟
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
        WechatUtil wechatUtil = new WechatUtil();

        String keys = "CODE_KEY";
        String code = "";

        //将code放在缓存中
        String codeStr = redisDao.getValue(keys);

        if ("".equals(codeStr)) {
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


    private UserAwardMapDO compactUserAwardMap(Integer customerId, Integer userAwardId, OwnerDO operator) {
        UserAwardMapDO userAwardMapDO = null;
        if (customerId != null && userAwardId != null) {
            userAwardMapDO = userAwardService.getUserAwardMapById(userAwardId);

            userAwardMapDO.setUsedStatus(1);

            userAwardMapDO.setOperator(operator);

            OwnerDO customer = new OwnerDO();
            customer.setUserId(customerId);
            userAwardMapDO.setUser(customer);
            return userAwardMapDO;
        }
        return null;
    }

    //检查操作员的权限
    public Boolean checkOperator(Integer userId, UserAwardMapDO userAwardMap) throws Exception {
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.getShopAuthMapListByShopId(userAwardMap.getShop().getShopId(), 0, 999);

        if (shopAuthMapExecution != null) {
            for (ShopAuthMapDO shopAuthMapDO : shopAuthMapExecution.getShopAuthMapList()) {
                if (userId.equals(shopAuthMapDO.getEmployee().getUserId())) {
                    return true;
                }
            }
        }
        return false;
    }


}

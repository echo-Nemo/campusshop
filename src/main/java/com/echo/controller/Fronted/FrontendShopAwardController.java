package com.echo.controller.Fronted;

import com.echo.dataobject.*;
import com.echo.service.AwardService;
import com.echo.service.ShopService;
import com.echo.service.UserAwardService;
import com.echo.service.UserShopMapService;
import com.echo.util.GeneratorQR;
import com.echo.util.HttpServletUtils;
import com.echo.util.execution.AwardExecution;
import com.echo.util.execution.UserAwardExecution;
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
@RequestMapping(value = "frontend")
public class FrontendShopAwardController {
    @Autowired
    UserAwardService userAwardService;

    @Autowired
    AwardService awardService;

    @Autowired
    UserShopMapService userShopMapService;

    @Autowired
    ShopService shopService;


    //shop中award列表的展示
    @RequestMapping(value = "listawardsbyshop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopAwardList(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>();

        Integer pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        Integer pageSize = HttpServletUtils.getInteger(request, "pageSize");
        //店铺id
        Integer shopId = HttpServletUtils.getInteger(request, "shopId");

        //分页获取award信息
        if (pageIndex > 0 && pageSize > 0 && shopId > 0) {
            String awardName = HttpServletUtils.getString(request, "awardName");
            AwardDO award = compactAwardCondition(shopId, awardName);
            try {
                AwardExecution awardExecution = awardService.listAward(award, pageIndex, pageSize);
                modelMap.put("awardList", awardExecution.getAwardList());
                modelMap.put("count", awardExecution.getCount());
                modelMap.put("status", true);
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }

            //获取用户的积分
            OwnerDO personInfo = (OwnerDO) request.getSession().getAttribute("user");
            if (personInfo != null && personInfo.getUserId() != null) {
                //判断该用户是否存在该店铺中
                UserShopMapDO userShopMap = userShopMapService.getUserShopMap(personInfo.getUserId(), shopId);
                if (userShopMap != null) {
                    modelMap.put("totalPoint", userShopMap.getPoint());
                } else {
                    modelMap.put("totalPoint", 0);
                }

            }

        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "empty info");
        }
        return modelMap;
    }


    //用户进行奖品的领取
    @RequestMapping(value = "reciveuseraward", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> reciveUserAward(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取用户和它的awardid
        OwnerDO personInfo = (OwnerDO) request.getSession().getAttribute("user");
        Integer awardId = HttpServletUtils.getInteger(request, "awardId");
        Integer shopId = HttpServletUtils.getInteger(request, "shopId");

        UserAwardMapDO userAwardMap = compactUserAward(personInfo, awardId);

        ShopDO shopCondition = null;
        try {
            shopCondition = shopService.getShopById(shopId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userAwardMap.setShop(shopCondition);


        if (userAwardMap != null) {
            try {
                UserAwardExecution userAwardExecution = userAwardService.addUserAwardMap(userAwardMap);
                if ("success".equals(userAwardExecution.getStatus())) {
                    modelMap.put("status", true);
                } else {
                    modelMap.put("status", false);
                }
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "传入的参数不合法");
        }
        return modelMap;
    }

    /*
    后端预览前端的award 根据awardId进行查询
     */
    @RequestMapping(value = "awarddetail", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAwardById(HttpServletRequest request) {
        Map<String, Object> modepMap = new HashMap<>();
        //从前端传来的awardId
        Integer awardId = HttpServletUtils.getInteger(request, "awardId");
        if (awardId > 0) {
            AwardDO awardDO = awardService.getAwardId(awardId);
            modepMap.put("status", true);
            modepMap.put("award", awardDO);
        } else {
            modepMap.put("status", false);
            modepMap.put("errMsg", "award is empty");
        }
        return modepMap;
    }

    @RequestMapping(value = "/getawardbyuserawardid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getAwardbyId(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 获取前端传递过来的userAwardId
        Integer userAwardId = HttpServletUtils.getInteger(request, "userAwardId");
        // 空值判断
        if (userAwardId > -1) {
            // 根据Id获取顾客奖品的映射信息，进而获取奖品Id
            UserAwardMapDO userAwardMap = userAwardService.getUserAwardMapById(userAwardId);

            // 根据奖品Id获取奖品信息
            AwardDO award = awardService.getAwardId(userAwardMap.getAward().getAwardId());
            // 将奖品信息和领取状态返回给前端
            modelMap.put("award", award);
            modelMap.put("usedStatus", userAwardMap.getUsedStatus());
            modelMap.put("userAwardMap", userAwardMap);
            modelMap.put("status", true);
        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "获取信息失败");
        }
        return modelMap;
    }


    //user和awardId进行组合
    public UserAwardMapDO compactUserAward(OwnerDO personInfo, Integer awardId) {
        UserAwardMapDO userAwardMap = new UserAwardMapDO();

        if (personInfo != null && personInfo.getUserId() != null) {
            //设置用户的信息
            userAwardMap.setUser(personInfo);
            //设置操作员为自己
            userAwardMap.setOperator(personInfo);
        }

        if (awardId != null && personInfo != null) {
            userAwardMap.setUserAwardId(awardId);

            //通过awarId获取奖品所需的积分
            AwardDO awardCondition = awardService.getAwardId(awardId);
            ShopDO shopCondition = new ShopDO();

            if (awardCondition != null) {
                userAwardMap.setAward(awardCondition);
                userAwardMap.setPoint(awardCondition.getPoint());

                try {
                    shopCondition = shopService.getShopById(awardCondition.getShopId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                userAwardMap.setShop(shopCondition);
            }
            return userAwardMap;
        } else {
            return null;
        }
    }

    //封装商品查询的条件
    public static AwardDO compactAwardCondition(Integer shopId, String awardName) {
        AwardDO awardDO = new AwardDO();
        awardDO.setShopId(shopId);

        //按奖品名进行模糊查询
        if (awardName != null) {
            awardDO.setAwardName(awardName);
        }
        return awardDO;
    }

    /*
    userAwardManageController中exchangeAward中二维码的存放
     */

    //二维码的创建
    //二维码的生成
    //获取微信用户信息api的前缀
    private static String urlPrefix;
    //获取微信用户信息api的中间部分
    private static String urlMiddle;
    //微信用户信息api的后缀部分
    private static String urlSuffix;

    //用户和店铺的信息
    private static String exchangeUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        FrontendShopAwardController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        FrontendShopAwardController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        FrontendShopAwardController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.exchange.url}")
    public void setExchangeUrl(String exchangeUrl) {
        FrontendShopAwardController.exchangeUrl = exchangeUrl;
    }

    /**
     * 生成二维码图片流并返回给前端
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/generateqrcode4award", method = RequestMethod.GET)
    @ResponseBody
    private void generateqrcode4Award(HttpServletRequest request, HttpServletResponse response) {
        Logger logger = LoggerFactory.getLogger(ProductDetailController.class);

        Integer userAwardId = HttpServletUtils.getInteger(request, "userAwardId");
        //确保用户处于登入状态
        OwnerDO user = (OwnerDO) request.getSession().getAttribute("user");

        if (userAwardId != -1 && user != null && user.getUserId() != null) {
            //获取时间戳用于有效性验证
            long timeStamp = System.currentTimeMillis();

            //设置二维码内容
            //冗余aaa为了后期替换
            String content = "{aaauserAwardIdaaa:" + userAwardId + ",aaacustomerIdaaa:"
                    + user.getUserId() + ",aaacreateTimeaaa:" + timeStamp + "}";

            // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
            try {
                String longUrl = urlPrefix + exchangeUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;

                //获取短链接 设置时间为长期有效
                // String shortUrl = ShortNetAddress.createShortUrl(longUrl,"long-term");
                //https://dwz.cn/qw7lzzpm
                String shortUrl = "https://dwz.cn/qw7lzzpm";

                BitMatrix bitMatrix = GeneratorQR.generatorQRCodeStream(longUrl, response);

                //将二维码发送到前端

                MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
            } catch (Exception e) {
                logger.error("二维码创建失败");
            }
        }
    }

}

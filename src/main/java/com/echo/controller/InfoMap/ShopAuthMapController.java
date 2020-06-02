package com.echo.controller.InfoMap;

import com.echo.dataobject.OwnerDO;
import com.echo.dataobject.ShopAuthMapDO;
import com.echo.dataobject.ShopDO;
import com.echo.dataobject.WechatAuthDO;
import com.echo.dto.UserAccessToken;
import com.echo.dto.WechatInfo;
import com.echo.service.OwnerService;
import com.echo.service.ShopAuthMapService;
import com.echo.service.ShopService;
import com.echo.service.WechatAuthService;
import com.echo.util.GeneratorQR;
import com.echo.util.HttpServletUtils;
import com.echo.util.RedisConfig.RedisDao;
import com.echo.util.execution.ShopAuthMapExecution;
import com.echo.util.wechat.ShortNetAddress;
import com.echo.util.wechat.WechatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopmap")
public class ShopAuthMapController {

    @Autowired
    ShopAuthMapService shopAuthMapService;

    @Autowired
    WechatAuthService wechatAuthService;

    @Autowired
    OwnerService ownerService;

    @Autowired
    ShopService shopService;

    @Autowired
    RedisDao redisDao;

    Logger logger = LoggerFactory.getLogger(ShopAuthMapController.class);

    //返回List列别
    @RequestMapping(value = "listshopauthmap", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listShopAuthMap(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>();

        // 取出分页信息
        int pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        int pageSize = HttpServletUtils.getInteger(request, "pageSize");

        // 从session中获取店铺信息
        ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");

        if (pageIndex < 0 || pageSize < 0 || currentShop.getShopId() == null) {
            modelMap.put("status", false);
            modelMap.put("errMsg", "输入的参数不合法");
        } else {
            try {
                ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.getShopAuthMapListByShopId(currentShop.getShopId(), pageIndex, pageSize);
                modelMap.put("status", true);
                modelMap.put("shopAuthMapList", shopAuthMapExecution.getShopAuthMapList());
                modelMap.put("count", shopAuthMapExecution.getCount());
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        return modelMap;
    }

    //通过ShopAuthId获取信息
    @RequestMapping(value = "getshopauthbyid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopAuthById(@RequestParam Integer shopAuthId) {
        Map<String, Object> modelMap = new HashMap<>();
        if (shopAuthId != null && shopAuthId > -1) {
            //根据前台传来的shopId获取信息
            ShopAuthMapDO shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
            modelMap.put("status", true);
            modelMap.put("shopAuthMap", shopAuthMap);
        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "传入的参数不合法");
        }
        return modelMap;
    }

    //修改授权登入的信息
    @RequestMapping(value = "modifyshopauth", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyShopAuthMap(String shopAuthMapStr, HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>();

        /*
        判断是否要输入验证码,授权编辑时需要输入验证码
         */
        boolean statusChange = HttpServletUtils.getBoolean(request, "statusChange");

        if (statusChange && !statusChange) {
            modelMap.put("status", false);
            modelMap.put("errMsg", "验证码输入错误");
            return modelMap;
        }

        ObjectMapper mapper = new ObjectMapper();
        ShopAuthMapDO shopAuthMap = null;

        try {
            //将前端传来的json字符串进行解析
            shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMapDO.class);
        } catch (Exception e) {
            modelMap.put("status", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        //进行店铺信息的修改
        if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
            //判断是否时店家自己
            Boolean flag = havePermission(shopAuthMap.getShopAuthId());

            if (!flag) {
                modelMap.put("status", false);
                modelMap.put("errMsg", "店家自己不能修改");
                return modelMap;
            } else {
                ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.modifyShopAuthMap(shopAuthMap);

                if (shopAuthMapExecution.getStates().equals("success")) {
                    modelMap.put("status", true);
                } else {
                    modelMap.put("status", false);
                    modelMap.put("errMsg", "修改信息失败");
                }
            }
        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "请输入授权信息");
            return modelMap;
        }
        return modelMap;
    }


    //判断是否有权限修改职位
    public boolean havePermission(Integer shopAuthId) {
        //根据shopAuthId获得该对象
        ShopAuthMapDO shopAuthMapDO = shopAuthMapService.getShopAuthMapById(shopAuthId);
        if (shopAuthMapDO.getTitleFlag() == 0) {
            //店家自己
            return false;
        } else {
            return true;
        }
    }


    /*
    微信二维码的生成
     */
    // 微信获取用户信息的api前缀
    private static String urlPrefix;
    // 微信获取用户信息的api中间部分
    private static String urlMiddle;
    // 微信获取用户信息的api后缀
    private static String urlSuffix;
    // 微信回传给的响应添加授权信息的url
    private static String authUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ShopAuthMapController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ShopAuthMapController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ShopAuthMapController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.auth.url}")
    public void setAuthUrl(String authUrl) {
        ShopAuthMapController.authUrl = authUrl;
    }

    /**
     * 生成带有URL的二维码，微信扫一扫就能链接到对应的URL里面
     *
     * @param request
     * @param response
     */


    @RequestMapping(value = "/generateqrcode4shopauth", method = RequestMethod.GET)
    private void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response) {


        // 从session里获取当前shop的信息
        ShopDO shop = (ShopDO) request.getSession().getAttribute("currentShop");

        if (shop != null && shop.getShopId() != null) {
            // 获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
            long timpStamp = System.currentTimeMillis();

            // 将店铺id和timestamp传入content，赋值到state中，这样微信获取到这些信息后会回传到授权信息的添加方法里
            // 加上aaa是为了一会的在添加信息的方法里替换这些信息使用
            String content = "{aaashopIdaaa:" + shop.getShopId() + ",aaacreateTimeaaa:" + timpStamp + "}";

            try {
                // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL

                String longUrl = urlPrefix + authUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;

                // 将目标URL转换成短的URL
                // String shortUrl = ShortNetAddress.createShortUrl(longUrl,"long-term");
                //String shortUrl = "https://dwz.cn/Pyd2wCaS";

                // 调用二维码生成的工具类方法，传入短的URL，生成二维码
                BitMatrix qRcodeImg = GeneratorQR.generatorQRCodeStream(longUrl, response);

                // 将二维码以图片流的形式输出到前端
                MatrixToImageWriter.writeToStream(qRcodeImg, "png", response.getOutputStream());

            } catch (IOException e) {
                logger.info(e.getMessage());

            }
        }
    }


    /*
    微信扫描添加用户信息
     */
    @RequestMapping(value = "addshopatuh", method = RequestMethod.GET)
    public String addShopAuthByWechat(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //从request中获取微信用户的信息
        WechatAuthDO auth = getEmployeeInfo(request);

        if (auth != null) {
            //根据userId获取用户信息
            OwnerDO user = ownerService.selectOwner(auth.getUserId());
            //将用户信息添加进user里面
            request.getSession().setAttribute("user", user);
            //解析微信回传过来的自定义参数state,因为之前已经进行了编码，所以这里需要解码
            String qrCodeInfo = new String(URLDecoder.decode(HttpServletUtils.getString(request, "state"), "UTF-8"));
            ObjectMapper mapper = new ObjectMapper();
            WechatInfo weChatInfo = null;
            try {
                //转换成WechatInfo实体类
                weChatInfo = mapper.readValue(qrCodeInfo.replace("aaa", "\""), WechatInfo.class);
            } catch (Exception e) {
                return "InfoMap/operationfail";
            }
            //校验二维码是否过期
            if (!checkQRCodeInfo(weChatInfo)) {
                return "InfoMap/operationfail";
            }
            // 去重校验
            // 获取该店铺下所有的授权信息
            // 避免重复扫描，重复添加数据库
            ShopAuthMapExecution allMapList = shopAuthMapService.getShopAuthMapListByShopId(weChatInfo.getShopId(), 1, 999);
            List<ShopAuthMapDO> shopAuthList = allMapList.getShopAuthMapList();
            for (ShopAuthMapDO sm : shopAuthList) {
                if (sm.getEmployee().getUserId().equals(user.getUserId())) {
                    return "shop/operationfail";
                }
            }
            try {
                //根据微信获取到的内容，添加店铺授权信息
                ShopAuthMapDO shopAuthMap = new ShopAuthMapDO();
                ShopDO shop = new ShopDO();
                shop.setShopId(weChatInfo.getShopId());
                shopAuthMap.setShop(shop);
                shopAuthMap.setEmployee(user);
                shopAuthMap.setTitle("员工");
                shopAuthMap.setTitleFlag(1);
                ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);
                if ("success".equals(se.getStates())) {
                    return "InfoMap/operationsuccess";
                } else {
                    return "InfoMap/operationfail";
                }
            } catch (RuntimeException e) {
                return "InfoMap/operationfail";
            }
        }
        return "InfoMap/operationfail";
    }

    /*
    判断微信二维码是否失效
     */

    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        if (wechatInfo != null && wechatInfo.getShopId() != null && wechatInfo.getCreateTime() != null) {
            //获取当前时间
            Long nowTime = System.currentTimeMillis();

//            //设置二维码的有效时间为10分钟
//            if ((nowTime - wechatInfo.getCreateTime()) <= 6000000) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
            return true;
        }
        return false;
    }

    /*
    根据微信回传回来的code 获取用户信息
     */
    private WechatAuthDO getEmployeeInfo(HttpServletRequest request) {
        WechatUtil wechatUtil = new WechatUtil();

        /*
        通过缓存获取code
         */

        //String code = request.getParameter("code");
        String keys = "SHOP_AUTH";
        String code = "";
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


}

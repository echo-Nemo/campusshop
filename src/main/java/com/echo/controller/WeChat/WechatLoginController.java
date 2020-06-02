package com.echo.controller.WeChat;

import com.echo.controller.local.LocalAuthController;
import com.echo.dataobject.LocalAuthDO;
import com.echo.dataobject.OwnerDO;
import com.echo.dataobject.WechatAuthDO;
import com.echo.dto.UserAccessToken;
import com.echo.dto.WechatAuExecution;
import com.echo.dto.WechatInfo;
import com.echo.dto.WechatUser;
import com.echo.service.LocalAuthService;
import com.echo.service.OwnerService;
import com.echo.service.WechatAuthService;
import com.echo.util.CodeUtil;
import com.echo.util.GeneratorQR;
import com.echo.util.HttpServletUtils;
import com.echo.util.RedisConfig.RedisDao;
import com.echo.util.execution.LocalAuthExecution;
import com.echo.util.wechat.WechatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("wechatlogin")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class WechatLoginController {

    @Autowired
    WechatAuthService wechatAuthService;

    @Autowired
    OwnerService ownerService;

    @Autowired
    RedisDao redisDao;

    @Autowired
    LocalAuthService localAuthService;

    private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);

    private OwnerDO person = null;

    @Transactional
    @RequestMapping(value = "userlogin", method = RequestMethod.GET)
    public String wechatadminLogin(HttpServletRequest request) {

        WechatUtil wechatUtil = new WechatUtil();
        WechatInfo wechatInfo = null;
        String qrCodeinfo = "";
        Integer flag;
        String code = "";
        String keys = "FLAG";
        String codeStr = redisDao.getValue(keys);

        //获取二维码的信息
        try {
            qrCodeinfo = new String(URLDecoder.decode(HttpServletUtils.getString(request, "state"), "UTF-8"));
        } catch (Exception e) {
            log.info(e.getMessage());
            return "InfoMap/operationfail";
        }

        //判断二维码是否有效
        ObjectMapper mapper = new ObjectMapper();

        try {
            wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
            flag = wechatInfo.getFlage();
        } catch (Exception e) {
            return "InfoMap/operationfail";
        }

        //检验验证码是否有效
        if (!checkQRCodeInfo(wechatInfo)) {
            return "InfoMap/operationfail";
        }

        if (codeStr != null) {
            code = codeStr;
        } else {
            code = request.getParameter("code");
            redisDao.setKey(keys, code);
        }

        log.debug("weixin login code:" + code);

        WechatUser chatuser = null;
        String openId = null;

        if (null != code) {
            UserAccessToken token;
            try {
                //通过code获取token
                token = wechatUtil.getUserAccessToken(code);
            } catch (Exception e) {
                e.printStackTrace();
                log.info(e.getMessage());
                return "InfoMap/operationfail";
            }

            log.debug("weixin login token" + token.toString());

            //通过token获取accessToken
            String accessToken = token.getAccessToken();

            //通过token获取openId
            openId = token.getOpenId();

            //通过openId和accessToken获取wechat对象全部信息
            chatuser = wechatUtil.getUserInfo(accessToken, openId);

            /*
            转化为wechat对象信息
             */
            if (chatuser == null) {
                return "InfoMap/operationfail";
            }

            /**
             * 根据openid判断对象是否存在
             */
            WechatAuthDO wechatAuthCondition = wechatAuthService.getWechatAuthByopenId(openId);

            if (wechatAuthCondition != null) {
                return "InfoMap/wechatexit";
            } else {
                //用户还没注册 获取微信用户信息
                OwnerDO personInfo = wechatUtil.getPersonInfoByWechatUser(chatuser);
                //微信用户的信息

                if (flag.equals(1)) {
                    personInfo.setUserType(1);
                } else {
                    personInfo.setUserType(2);
                }

                //微信账号信息
                WechatAuthDO wechatAuth = new WechatAuthDO();
                wechatAuth.setOpenId(openId);
                wechatAuth.setPersonInfo(personInfo);


                try {
                    WechatAuExecution wechatAuExecution = wechatAuthService.register(wechatAuth);

                    if (!("success".equals(wechatAuExecution.getStateInfo()))) {
                        return "InfoMap/operationfail";
                    } else {
                        person = ownerService.selectOwner(wechatAuth.getUserId());

                        //request.getSession().setAttribute("person", person);

                        System.out.println("======== userlogin " + request.getSession().getId() + "=========" + person + "=========");
                        log.info(person + "");
                        return "InfoMap/operationsuccess";
                    }
                } catch (Exception e) {
                    return "InfoMap/operationfail";
                }
            }
        }
        return "InfoMap/operationfail";
    }

    /*
 微信用户进行扫码登入
  */
    //二维码的创建
    //获取微信用户信息api的前缀
    private static String urlPrefix;
    //获取微信用户信息api的中间部分
    private static String urlMiddle;
    //微信用户信息api的后缀部分
    private static String urlSuffix;
    //商家的注册
    private static String loginUrl;


    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        WechatLoginController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        WechatLoginController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        WechatLoginController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.login.url}")
    public void setLoginUrl(String loginUrl) {
        WechatLoginController.loginUrl = loginUrl;
    }


    //普通用户的二维码开发
    @RequestMapping(value = "/generateqrcode4adminer", method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4AdmLogin(HttpServletRequest request, HttpServletResponse response) {
        Logger logger = LoggerFactory.getLogger(LocalAuthController.class);

        //获取时间戳用于有效性验证
        long timeStamp = System.currentTimeMillis();
        Integer flage = 2;

        //设置二维码内容
        //冗余aaa为了后期替换
        String content = "{aaacreateTimeaaa:" + timeStamp + ",aaaflageaaa:" + flage + "}";

        // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
        try {
            String longUrl = urlPrefix + loginUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;

            //获取短链接 设置时间为长期有效
            String shortUrl = "https://dwz.cn/xMp0M60a";


            BitMatrix bitMatrix = GeneratorQR.generatorQRCodeStream(longUrl, response);

            //将二维码发送到前端
            MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
        } catch (Exception e) {
            logger.error("二维码创建失败");
        }
    }

    /**
     * 生成二维码图片流并返回给前端
     * 微信扫码进行登入判断 该用户是否以注册 wechatAuth 根据openId进行判断
     *
     * @param request
     * @param response
     */
    //普通用户的二维码开发
    @RequestMapping(value = "/generateqrcode4customer", method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4CusLogin(HttpServletRequest request, HttpServletResponse response) {
        Logger logger = LoggerFactory.getLogger(LocalAuthController.class);

        //获取时间戳用于有效性验证
        long timeStamp = System.currentTimeMillis();
        Integer flage = 1;

        //设置二维码内容
        //冗余aaa为了后期替换
        String content = "{aaacreateTimeaaa:" + timeStamp + ",aaaflageaaa:" + flage + "}";

        // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标URL
        try {
            String longUrl = urlPrefix + loginUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;

            BitMatrix bitMatrix = GeneratorQR.generatorQRCodeStream(longUrl, response);

            //将二维码发送到前端

            MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
        } catch (Exception e) {
            logger.error("二维码创建失败");
        }
    }


    //微信账号的绑定
    @RequestMapping(value = "bindauth", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> bindWechatAuth(HttpServletRequest request, HttpSession httpSession) {
        Map<String, Object> modelMap = new HashMap<>();


        if (!CodeUtil.checkVerfyCode(request)) {
            modelMap.put("status", false);
            modelMap.put("errMsg", "验证码输入错误");
            return modelMap;
        }
        //获取前端的数据
        String userName = HttpServletUtils.getString(request, "userName");
        String password = HttpServletUtils.getString(request, "password");

        System.out.println("===== bindurl " + request.getSession().getId() + "=======");
        //OwnerDO personInfo = (OwnerDO) request.getSession().getAttribute("person");

        try {
            if (userName != null && password != null && person != null && person.getUserId() != null) {

                LocalAuthDO localAuth = new LocalAuthDO();

                localAuth.setPassword(password);
                localAuth.setUsername(userName);
                localAuth.setPersonInfo(person);

                LocalAuthExecution localAuthExecution = localAuthService.localBindWechat(localAuth);

                String flag = "success";
                //账户是否绑定成功
                if (flag.equals(localAuthExecution.getStates())) {
                    modelMap.put("status", true);
                } else {
                    modelMap.put("status", false);
                    modelMap.put("errMsg", "该账号已绑定或绑定失败");
                }
            } else {
                modelMap.put("status", false);
                modelMap.put("errMsg", "用户名和密码不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("status", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        return modelMap;
    }


    /*
  判断微信二维码是否失效
   */
    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        if (wechatInfo != null && wechatInfo.getCreateTime() != null) {
            //获取当前时间
            Long nowTime = System.currentTimeMillis();

//            //设置二维码的有效时间为10分钟
//            if ((nowTime - wechatInfo.getCreateTime()) <= 6000000) {
//                return true;
//            } else {
//                return false;
//            }
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping(value = "getvalue", method = RequestMethod.GET)
    @ResponseBody
    public String getValue(HttpSession httpSession) {
        OwnerDO person = (OwnerDO) httpSession.getAttribute("Person");
        return "hello" + person;
    }

}

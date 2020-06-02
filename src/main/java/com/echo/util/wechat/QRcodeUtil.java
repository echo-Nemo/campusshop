//package com.echo.util.wechat;
//
//import com.echo.dataobject.WechatAuthDO;
//import com.echo.dto.UserAccessToken;
//import com.echo.dto.WechatInfo;
//import com.echo.execeptions.BusinessException;
//import com.echo.execeptions.EmBusinessError;
//import com.echo.service.WechatAuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.servlet.http.HttpServletRequest;
//
//public class QRcodeUtil {
//
//    @Autowired
//    WechatAuthService wechatAuthService;
//
//
//    //设置二位码的有效时间
//    public Boolean checkQRCodeVaild(WechatInfo wechatInfo) {
//
//        if (wechatInfo != null && wechatInfo.getCreateTime() != null && wechatInfo.getProductId() != null
//                && wechatInfo.getCustomerId() != null) {
//            long nowTime = System.currentTimeMillis();
//            //用户扫描二维码的有效时间位10分钟
//            if (nowTime - wechatInfo.getCreateTime() <= 600000) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        return false;
//    }
//
//
//    //获取微信二维码二维码扫描后用户的信息
//    public WechatAuthDO getOperatorInfo(HttpServletRequest request) throws BusinessException {
//        WechatUtil wechatUtil = new WechatUtil();
//
//        String code = request.getParameter("code");
//
//        WechatAuthDO wechatAuth = null;
//
//        if (null != code) {
//            UserAccessToken token = null;
//            try {
//                token = wechatUtil.getUserAccessToken(code);
//                String openId = token.getOpenId();
//                request.getSession().setAttribute("openId", openId);
//                wechatAuth = wechatAuthService.getWechatAuthByopenId(openId);
//            } catch (Exception e) {
//                throw new BusinessException(EmBusinessError.NULL_ERROR);
//            }
//        }
//        return wechatAuth;
//    }
//
//}

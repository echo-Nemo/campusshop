package com.echo.controller.WeChat;

import com.echo.util.wechat.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Controller
@RequestMapping("wechat")

public class WechatController {

    private static Logger log = LoggerFactory.getLogger(WechatController.class);

    //servlet中的Get 请求
    @RequestMapping(value = "connect", method = RequestMethod.GET)
    public void weChatConnect(HttpServletRequest request, HttpServletResponse response) {
        log.debug("weixin get....");

        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        //通过检验signature对请求进行校验
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                log.debug("weixin get success...");
                out.print(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}

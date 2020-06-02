package com.echo.util.wechat;

import com.echo.dataobject.OwnerDO;
import com.echo.dto.UserAccessToken;
import com.echo.dto.WechatUser;
import com.echo.service.Impl.AreaServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;


public class WechatUtil {

    @Autowired
    AreaServiceImpl areaService;


    private static Logger log = LoggerFactory.getLogger(WechatUtil.class);

    /**
     * 获取UserAccessToken实体类
     *
     * @param code
     * @return
     * @throws IOException
     */
    public UserAccessToken getUserAccessToken(String code) throws IOException {

        // 测试号信息里的appId
        String appId = "wx8d060669816b5781";
        log.debug("appId:" + appId);

        // 测试号信息里的appsecret
        String appsecret = "cc72511196ff08e2407fe147980b411a";
        log.debug("secret:" + appsecret);

        // 根据传入的code,拼接出访问微信定义好的接口的URL
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + appId + "&secret=" + appsecret + "&code=" + code
                + "&grant_type=authorization_code";

        /*
         向相应URL发送请求获取token json字符串
         */

        String tokenStr = httpsRequest(url, "GET", null);

        log.debug("userAccessToken:" + tokenStr);

        UserAccessToken token = new UserAccessToken();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 将json字符串转换成相应对象
            token = objectMapper.readValue(tokenStr, UserAccessToken.class);
        } catch (JsonParseException e) {
            log.error("获取用户accessToken失败: " + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            log.error("获取用户accessToken失败: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("获取用户accessToken失败: " + e.getMessage());
            e.printStackTrace();
        }
        if (token == null) {
            log.error("获取用户accessToken失败。");
            return null;
        }
        return token;
    }

    /**
     * 获取WechatUser实体类
     *
     * @param accessToken
     * @param openId
     * @return
     */
    public WechatUser getUserInfo(String accessToken, String openId) {
        // 根据传入的accessToken以及openId拼接出访问微信定义的端口并获取用户信息的URL
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId
                + "&lang=zh_CN";
        // 访问该URL获取用户信息json 字符串
        String userStr = httpsRequest(url, "GET", null);
        log.debug("user info :" + userStr);
        WechatUser user = new WechatUser();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将json字符串转换成相应对象
            user = objectMapper.readValue(userStr, WechatUser.class);
        } catch (JsonParseException e) {
            log.error("获取用户信息失败: " + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            log.error("获取用户信息失败: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("获取用户信息失败: " + e.getMessage());
            e.printStackTrace();
        }
        if (user == null) {
            log.error("获取用户信息失败。");
            return null;
        }
        return user;
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return json字符串
     */
    public String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        //JSONObject jsonObject=null;
        StringBuffer buffer = new StringBuffer();
        try {
            //创建SSLcontext对象，并使用我们指定信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            //从上述的sslContext对象中获得SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            //设置请求方式(GET POST)
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equals(httpUrlConn.getRequestMethod())) {
                httpUrlConn.connect();
            }

            //当有数据提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                //防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();

            }
            //将返回的输入流转化为字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);

            }
            //关闭流，释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream = null;
            //断开连接
            httpUrlConn.disconnect();
            log.debug("http str==" + buffer.toString());
            //jsonObject = JSONObject.fromObject(buffer.toString());

        } catch (ConnectException e) {
            log.error("wechat connection timed out");

        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return buffer.toString();
    }

    //根据WechatUser 生成Owner
    public OwnerDO getPersonInfoByWechatUser(WechatUser wechatUser) {
        OwnerDO personInfo = new OwnerDO();
        personInfo.setName(wechatUser.getNickName());
        personInfo.setEnableStatus(1);
        personInfo.setProfileImg(wechatUser.getHeadimgurl());
        personInfo.setGender(wechatUser.getSex() + "");
        return personInfo;
    }

}

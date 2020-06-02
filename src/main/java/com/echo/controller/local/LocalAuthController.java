package com.echo.controller.local;

import com.echo.controller.WeChat.UserController;
import com.echo.dataobject.LocalAuthDO;
import com.echo.dataobject.OwnerDO;
import com.echo.service.LocalAuthService;
import com.echo.util.CodeUtil;
import com.echo.util.HttpServletUtils;
import com.echo.util.execution.LocalAuthExecution;
import com.echo.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/local")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")

public class LocalAuthController {

    @Autowired
    LocalAuthService localAuthService;


    //密码的修改
    @RequestMapping(value = "changepassword", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> changePassword(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        if (!CodeUtil.checkVerfyCode(request)) {
            modelMap.put("status", false);
            modelMap.put("errMsg", "验证码输入错误");
            return modelMap;
        }
        //获取前端的数据
        String userName = HttpServletUtils.getString(request, "userName");
        String password = HttpServletUtils.getString(request, "password");
        String newPassword = HttpServletUtils.getString(request, "newPassword");
        OwnerDO personInfo = (OwnerDO) request.getSession().getAttribute("user");

        if (userName == null || password == null || newPassword == null || personInfo.getUserId() == null) {
            modelMap.put("status", false);
            modelMap.put("errMsg", "用户名和密码不能为空");
            return modelMap;
        } else {
            //获取用户，判断在数据库中是否存在
            LocalAuthDO localAuthDO = localAuthService.queryByUserId(personInfo.getUserId());

            //账号不存在或用户名错误
            if (localAuthDO == null || !localAuthDO.getUsername().equals(userName)) {
                modelMap.put("status", false);
                modelMap.put("errMsg", "账号不存在或用户名错误");
                return modelMap;
            } else {
                try {
                    LocalAuthExecution localAuthExecution = localAuthService.modifypwd(personInfo.getUserId(), userName, password, newPassword);
                    if (localAuthExecution.getStates().equals("success")) {
                        modelMap.put("status", true);
                    } else {
                        modelMap.put("status", false);
                        modelMap.put("errMsg", "修改密码失败");
                        return modelMap;
                    }
                } catch (Exception e) {
                    modelMap.put("status", false);
                    modelMap.put("errMsg", e.getMessage());
                    return modelMap;
                }
            }
        }
        return modelMap;
    }

    //登入的检查
    @RequestMapping(value = "logincheck", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loginCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        String admin = "admin";
        //判断是否需要验证码 (用户名或密码输错3次后需要进行验证)
        boolean needVerify = HttpServletUtils.getBoolean(request, "needVerify");

        if (needVerify && !CodeUtil.checkVerfyCode(request)) {
            modelMap.put("status", false);
            modelMap.put("errMsg", "验证码输入错误");
            return modelMap;
        }

        String userName = HttpServletUtils.getString(request, "userName");
        String password = HttpServletUtils.getString(request, "password");

        if (userName == null || password == null) {
            modelMap.put("status", false);
            modelMap.put("errMsg", "用户名或密码为空");
            return modelMap;
        }


        //数据库中的密码是进行加密处理了的
        password = Md5Util.getMd5(password);

        //判断改用户是否存在
        LocalAuthDO localAuthDO = localAuthService.queryByNamePwd(userName, password);

        if (localAuthDO == null) {
            modelMap.put("status", false);
            modelMap.put("errMsg", "用户名或密码错误");
            return modelMap;
        } else {
            //将登入用户的userType 前端进行跳转
            Integer userType = localAuthDO.getPersonInfo().getUserType();

            //管理员
            if (userName.equals(admin)) {
                modelMap.put("userType", 3);
            } else {
                modelMap.put("userType", userType);
            }
            modelMap.put("status", true);

            System.out.println("======= login" + request.getSession().getId() + "=======");
            //将用户的信息放在session中保存
            request.getSession().setAttribute("user", localAuthDO.getPersonInfo());
            return modelMap;
        }
    }


    //用户的登出
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //将用户的信息置空
        modelMap.put("status", true);
        request.getSession().setAttribute("user", null);
        return modelMap;
    }
}

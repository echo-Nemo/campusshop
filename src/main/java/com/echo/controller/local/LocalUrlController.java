package com.echo.controller.local;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/localurl")
public class LocalUrlController {

    /**
     * 账号的绑定
     */
    @RequestMapping(value = "/bindaccount", method = RequestMethod.GET)
    public String bindaccount() {
        return "localauth/accountbind";
    }


    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping(value = "/changepwd", method = RequestMethod.GET)
    public String changePassword() {
        return "localauth/changepsw";
    }

    /**
     * 登入进行校验
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginCheck() {
        return "localauth/login";
    }

    /**
     * 微信注册
     */
    @RequestMapping(value = "wechatregister", method = RequestMethod.GET)
    public String wechatLogin() {
        return "localauth/register";
    }

    /**
     * 管理员的注册
     */
    @RequestMapping(value = "adminerregister", method = RequestMethod.GET)
    public String adminLogin() {
        return "localauth/adminer";
    }

    /**
     * 商铺的管理
     */
    @RequestMapping(value = "shopmanage", method = RequestMethod.GET)
    public String shopManage() {
        return "localauth/shopmanage";
    }

    /**
     * 区域的管理
     */

    @RequestMapping(value = "areamanage", method = RequestMethod.GET)
    public String areaManage() {
        return "localauth/areamanage";
    }

    /**
     * 区域的新增
     */
    @RequestMapping(value = "addarea", method = RequestMethod.GET)
    public String araeAdd() {
        return "localauth/areaoperation";
    }

}

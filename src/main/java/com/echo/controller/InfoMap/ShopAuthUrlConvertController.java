package com.echo.controller.InfoMap;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "shopmap", method = RequestMethod.GET)
public class ShopAuthUrlConvertController {

    //shopAuth的修改页面
    @RequestMapping(value = "modifyshopauth", method = RequestMethod.GET)
    public String getShopAuthModify() {
        return "InfoMap/shopauthedit";
    }

    //shopAuth的权限管理页面
    @RequestMapping(value = "shopauthmanagement", method = RequestMethod.GET)
    public String getShopManagement() {
        return "InfoMap/shopauthmanagement";
    }

}

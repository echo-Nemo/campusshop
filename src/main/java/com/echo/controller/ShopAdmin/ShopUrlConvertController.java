package com.echo.controller.ShopAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "/shop", method = RequestMethod.GET)

public class ShopUrlConvertController {
    /*
    进行html文件的转化
     */
    //商铺的操作页面
    @RequestMapping(value = "/shopoperation", method = RequestMethod.GET)
    public String convertRegister() {
        System.out.println("========convert==========");
        return "shop/shopOperation";
    }

    //商铺的列表显示
    @RequestMapping(value = "/shoplist", method = RequestMethod.GET)
    public String convertShopList() {
        return "shop/shoplist";
    }

    //商铺的管理
    @RequestMapping(value = "/shopmanagement", method = RequestMethod.GET)
    public String convertShopManagement() {
        return "shop/shopmanagement";
    }

    //商品种类的管理
    @RequestMapping(value = "/productcategorymanagement", method = RequestMethod.GET)
    public String convertProductManagement() {
        return "shop/productcategorymanagement";
    }

    //奖品的隔管理页面
    @RequestMapping(value = "awardmanage", method = RequestMethod.GET)
    public String convertAwardManage() {
        return "shop/awardmanagement";
    }

    //奖品的操作页面
    @RequestMapping(value = "awardoperation", method = RequestMethod.GET)
    public String convertAwardOperation() {
        return "shop/awardoperation";
    }

    //用户奖品列表
    @RequestMapping(value = "userawarddelivery", method = RequestMethod.GET)
    public String userAwardList() {
        return "shop/awarddelivercheck";
    }


}
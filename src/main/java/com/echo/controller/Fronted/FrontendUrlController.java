package com.echo.controller.Fronted;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("frontend")
public class FrontendUrlController {

    @RequestMapping(value = "mainpagelist", method = RequestMethod.GET)
    public String mainPageList() {
        return "frontend/index";
    }


    @RequestMapping(value = "shoplist", method = RequestMethod.GET)
    public String shopList() {
        return "frontend/shoplist";
    }

    //店铺信息的显示
    @RequestMapping(value = "shoplistdetails", method = RequestMethod.GET)
    public String convertShopListDetatis() {
        return "frontend/shopdetail";
    }

    //商品信息的显示
    @RequestMapping(value = "productdetail", method = RequestMethod.GET)
    public String convertProductDetail() {
        return "frontend/productdetail";
    }

    //奖品的页面
    @RequestMapping(value = "shopawardlist", method = RequestMethod.GET)
    public String convertAwardList() {
        return "frontend/awardlist";
    }

    //积分记录的页面
    @RequestMapping(value = "pointrecord", method = RequestMethod.GET)
    public String pointRecord() {
        return "frontend/pointrecord";
    }

    //我的奖品的详情页
    @RequestMapping(value = "myawarddetail", method = RequestMethod.GET)
    public String myAwardDetail() {
        return "frontend/myawarddetail";
    }

    //消费记录详情页
    @RequestMapping(value = "myrecord", method = RequestMethod.GET)
    public String myRedord() {
        return "frontend/myrecord";
    }

    //顾客在某个店铺的积分记录
    @RequestMapping(value = "mypoint", method = RequestMethod.GET)
    public String myShopPoint() {
        return "frontend/mypoint";

    }

    //奖品的查看
    @RequestMapping(value = "awarddetails", method = RequestMethod.GET)
    public String awardDetail() {
        return "frontend/awarddetail";
    }


}


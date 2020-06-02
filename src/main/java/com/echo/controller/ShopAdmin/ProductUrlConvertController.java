package com.echo.controller.ShopAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "productadmin", method = RequestMethod.GET)

public class ProductUrlConvertController {

    //商品添加和编辑的页面
    @RequestMapping("productoperation")
    public String addProductConvert() {
        return "shop/productoperation";
    }

    //商品的管理
    @RequestMapping("productmanagement")
    public String productManagerMent() {
        return "shop/productmanagement";
    }
}

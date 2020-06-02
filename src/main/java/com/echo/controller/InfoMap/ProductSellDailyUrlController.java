package com.echo.controller.InfoMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "product", method = RequestMethod.GET)
public class ProductSellDailyUrlController {

    //商品的日销售量
    @RequestMapping(value = "productselldaily", method = RequestMethod.GET)
    public String productSellDailyConvert() {
        return "InfoMap/productbuycheck";
    }
}

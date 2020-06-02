package com.echo.controller.InfoMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "usershop", method = RequestMethod.GET)
public class UserShopUrlController {

    @RequestMapping(value = "listusershop", method = RequestMethod.GET)
    public String getListUserShop() {
        return "InfoMap/usershopcheck";
    }
}

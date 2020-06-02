package com.echo.controller.WeChat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "test")
public class TestController extends BaseController {

    @RequestMapping(value = "values", method = RequestMethod.GET)
    @ResponseBody
    public String accessvalue(HttpServletRequest request) {
        String personImfo = super.getPersonImfo(request);
        return "hello" + personImfo;
    }
}

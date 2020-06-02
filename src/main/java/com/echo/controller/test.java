package com.echo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class test {
    @RequestMapping(value = "test1", method = RequestMethod.GET)
    @ResponseBody
    public String test1() {
        return "hello";
    }
}

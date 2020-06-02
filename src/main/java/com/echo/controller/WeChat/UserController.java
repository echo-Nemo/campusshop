package com.echo.controller.WeChat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping(value = "user")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class UserController {
    String name = "";

    @RequestMapping(value = "setvalue", method = RequestMethod.GET)
    public void setValues(HttpServletRequest request) {
        name = "alice";
        System.out.println("======usercontro" + request.getSession().getId() + "======");
        request.getSession().setAttribute("names", name);
    }


//    @RequestMapping(value = "getvalue", method = RequestMethod.GET)
//    @ResponseBody
//    public String getValuesv(ModelMap model, String per, HttpSession httpSession) {
//        // return httpSession.getAttribute("currPer").toString();
//        //System.out.println("====="+httpSession.getAttribute("currPer")+"======");
//        return model.getAttribute("user").toString();
//    }


}

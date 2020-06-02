package com.echo.controller.WeChat;


import com.echo.dataobject.OwnerDO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "base")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class BaseController extends UserController {
    private String personImfo;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String getPersonImfo(HttpServletRequest request) {
        String names = (String) request.getSession().getAttribute("names");
        return names;
    }


    @RequestMapping(value = "test1", method = RequestMethod.GET)
    @ResponseBody
    public String gettest(HttpServletRequest request) {
        System.out.println("======test1" + request.getSession().getId() + "=======");
        OwnerDO users = (OwnerDO) request.getSession().getAttribute("user");
        return users.toString();
    }


}

package com.echo.controller.InfoMap;

import com.echo.dataobject.OwnerDO;
import com.echo.dataobject.ShopDO;
import com.echo.dataobject.UserShopMapDO;
import com.echo.service.UserShopMapService;
import com.echo.util.HttpServletUtils;
import com.echo.util.execution.UserShopExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "shop")
public class UserShopController {

    @Autowired
    UserShopMapService userShopMapService;

    @RequestMapping(value = "listusershop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listUserShopInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        // 获取分页信息
        int pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        int pageSize = HttpServletUtils.getInteger(request, "pageSize");

        //获取当前店铺的信息
        ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");

        if (currentShop != null && currentShop.getShopId() !=null && pageIndex > 0 && pageSize > 0) {

            UserShopMapDO userShopMap = new UserShopMapDO();
            userShopMap.setShop(currentShop);

            //是否按顾客的名字进行模糊查询
            String userName = HttpServletUtils.getString(request, "userName");

            if (userName != null) {
                OwnerDO customer = new OwnerDO();
                customer.setName(userName);
                userShopMap.setUser(customer);
            }

            try {
                UserShopExecution userShopExecution = userShopMapService.getUserShopListByShop(userShopMap, pageIndex, pageSize);
                modelMap.put("userShopMapList", userShopExecution.getUserShopMapList());
                modelMap.put("count", userShopExecution.getCount());
                modelMap.put("status", true);
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMag", e.getMessage());
            }
        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "传入的参数不合法");
        }
        return modelMap;
    }
}

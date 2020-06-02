package com.echo.controller.Fronted;


import com.echo.dataobject.OwnerDO;
import com.echo.dataobject.UserShopMapDO;
import com.echo.service.UserShopMapService;
import com.echo.util.HttpServletUtils;
import com.echo.util.execution.UserShopExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "frontend")
public class MyShopPointController {
    @Autowired
    UserShopMapService userShopMapService;
    //我在某个店铺的积分

    @RequestMapping(value = "listusershoppoint")
    @ResponseBody
    public Map<String, Object> listUserShopPointByCustomer(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Integer pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        Integer pageSize = HttpServletUtils.getInteger(request, "pageSize");
        //获取顾客信息
        OwnerDO user = (OwnerDO) request.getSession().getAttribute("user");

        if (pageIndex > -1 && pageSize > -1 && user != null && user.getUserId() != null) {
            UserShopMapDO userShopMap = new UserShopMapDO();
            userShopMap.setUser(user);

            Integer shopId = HttpServletUtils.getInteger(request, "shopId");
            String shopName = HttpServletUtils.getString(request, "shopName");

            if (shopId != null && shopId > -1) {
                userShopMap.getShop().setShopId(shopId);
            }

            if (shopName != null && (!"".equals(shopName))) {
                userShopMap.getShop().setShopName(shopName);
            }

            try {
                UserShopExecution userShopExecution = userShopMapService.getUserShopListByShop(userShopMap, pageIndex, pageSize);
                modelMap.put("userShopMapList", userShopExecution.getUserShopMapList());
                modelMap.put("count", userShopExecution.getCount());
                modelMap.put("status", true);
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        }
        return modelMap;
    }
}

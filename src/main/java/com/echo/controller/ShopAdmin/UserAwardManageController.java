package com.echo.controller.ShopAdmin;

import com.echo.dataobject.*;
import com.echo.dto.UserAccessToken;
import com.echo.dto.WechatInfo;
import com.echo.service.ShopAuthMapService;
import com.echo.service.UserAwardService;
import com.echo.service.WechatAuthService;
import com.echo.util.HttpServletUtils;
import com.echo.util.RedisConfig.RedisDao;
import com.echo.util.execution.ShopAuthMapExecution;
import com.echo.util.execution.UserAwardExecution;
import com.echo.util.wechat.WechatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "shop")
public class UserAwardManageController {
    /*
    用户积分类别
     */

    @Autowired
    UserAwardService userAwardService;

    @Autowired
    ShopAuthMapService shopAuthMapService;

    @Autowired
    WechatAuthService wechatAuthService;

    @Autowired
    RedisDao redisDao;

    @RequestMapping(value = "listuserawardshop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listUserAwardShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        // 获取分页信息
        int pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        int pageSize = HttpServletUtils.getInteger(request, "pageSize");

        ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");

        if (pageIndex > 0 && pageSize > 0 && currentShop != null && currentShop.getShopId() != null) {
            UserAwardMapDO userAwardMap = new UserAwardMapDO();
            userAwardMap.setShop(currentShop);
            String awardName = HttpServletUtils.getString(request, "awardName");

            if (awardName != null) {
                AwardDO award = new AwardDO();
                award.setAwardName(awardName);
                userAwardMap.setAward(award);
            }

            try {
                UserAwardExecution userAwardExecution = userAwardService.getUserAwarrdList(userAwardMap, pageIndex, pageSize);
                modelMap.put("status", true);
                modelMap.put("userAwardMapList", userAwardExecution.getUserAwardMapDOList());
                modelMap.put("count", userAwardExecution.getCount());
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }

        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "传入的参数不合法");
        }
        return modelMap;
    }

}



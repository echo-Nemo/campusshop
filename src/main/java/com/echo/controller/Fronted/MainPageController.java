package com.echo.controller.Fronted;

import com.echo.dataobject.HeadLineDO;
import com.echo.dataobject.ShopCategoryDO;
import com.echo.service.HeadLineService;
import com.echo.service.ShopcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("fronted")
public class MainPageController {
    @Autowired
    ShopcategoryService shopcategoryService;
    @Autowired
    HeadLineService headLineService;

    //初始化前端的主页信息,获取一级店铺类别(parent_id为null)和头条列表
    @RequestMapping(value = "listmainpageinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listMainPageInfo() {
        Map<String, Object> map = new HashMap<>();

        try {
            // 获取一级店铺类别列表(即parentId为空的ShopCategory)
            List<ShopCategoryDO> shopCategoryList = new ArrayList<>();
            shopCategoryList = shopcategoryService.queryShopCategory(null);
            map.put("shopCategoryList", shopCategoryList);
        } catch (Exception e) {
            map.put("status", false);
            map.put("errMsg", e.getMessage());
            return map;
        }

        try {
            //获取头条的类别
            // 获取状态为可用(1)的头条列表
            HeadLineDO headLine = new HeadLineDO();
            headLine.setEnableStatus(1);
            List<HeadLineDO> headLineList = new ArrayList<>();
            headLineList = headLineService.queryHeadLineList(1);
            map.put("headLineList", headLineList);
        } catch (Exception e) {
            map.put("status", false);
            map.put("errMsg", e.getMessage());
            return map;
        }
        map.put("status", true);
        return map;
    }
}

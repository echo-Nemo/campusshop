package com.echo.controller.Fronted;

import com.echo.dataobject.AreaDO;
import com.echo.dataobject.ShopCategoryDO;
import com.echo.dataobject.ShopDO;
import com.echo.service.AreaService;
import com.echo.service.ShopService;
import com.echo.service.ShopcategoryService;
import com.echo.util.HttpServletUtils;
import com.echo.util.execution.ShopExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "frontend")
public class ShopListController {
    @Autowired
    ShopService shopService;

    @Autowired
    AreaService areaService;

    @Autowired
    ShopcategoryService shopcategoryService;

    /*
    返回商品类别页中shopCategory列表(一级或二级列表)，以及区域信息列表
     */

    @RequestMapping(value = "listshoppageinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listShopPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        //判断是一级目录还是二级目录
        Integer parentId = HttpServletUtils.getInteger(request, "parentId");
        List<ShopCategoryDO> shopCategoryList = null;

        //有二级目录
        if (parentId != -1) {
            try {
                ShopCategoryDO shopCategoryCondition = new ShopCategoryDO();
                ShopCategoryDO parent = new ShopCategoryDO();

                parent.setShopCategoryId(parentId);

                shopCategoryCondition.setParent(parent);

                shopCategoryList = shopcategoryService.queryShopCategory(shopCategoryCondition);
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            try {
                shopCategoryList = shopcategoryService.queryShopCategory(null);
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        }
        modelMap.put("shopCategoryList", shopCategoryList);

        List<AreaDO> areaList = null;

        try {
            areaList = areaService.queryArea();
            modelMap.put("areaList", areaList);
        } catch (Exception e) {
            modelMap.put("status", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        modelMap.put("status", true);
        return modelMap;
    }

    //获取指定查询条件下的店铺列表
    @RequestMapping(value = "listshop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        //获取分页的数据
        Integer pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        Integer pageSize = HttpServletUtils.getInteger(request, "pageSize");

        if ((pageIndex > -1) && (pageSize > -1)) {

            Integer parentId = HttpServletUtils.getInteger(request, "parentId");
            Integer shopCategoryId = HttpServletUtils.getInteger(request, "shopCategoryId");

            Integer areaId = HttpServletUtils.getInteger(request, "areaId");
            String shopName = HttpServletUtils.getString(request, "shopName");

            ShopDO shopCondition = compactShopCondition(shopCategoryId, parentId, shopName, areaId);
            ShopExecution shopExecution = shopService.shopList(shopCondition, pageIndex, pageSize);

            modelMap.put("shopList", shopExecution.getShopList());
            modelMap.put("count", shopExecution.getCount());
            modelMap.put("status", true);
        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "pageSize ang pageIndex is not requored null");
        }
        return modelMap;
    }

    //多种条件的组合查询  返回shopCondition
    public ShopDO compactShopCondition(Integer shopCategoryId, Integer parentId, String shopName, Integer areaId) {
        ShopDO shopCondition = new ShopDO();

        if (parentId != -1) {
            ShopCategoryDO childCategory = new ShopCategoryDO();
            ShopCategoryDO parentCategory = new ShopCategoryDO();
            parentCategory = shopcategoryService.getShopCategory(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }

        if (shopCategoryId != -1) {
            ShopCategoryDO shopCategoryDO = new ShopCategoryDO();
            shopCategoryDO.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategoryDO);
        }

        if (areaId != -1) {
            AreaDO area = new AreaDO();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }

        if (shopName != null) {
            shopCondition.setShopName(shopName);
        }
        return shopCondition;
    }


}

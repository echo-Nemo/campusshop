package com.echo.controller.ShopAdmin;

import com.echo.dataobject.ProductCategoryDO;
import com.echo.dataobject.ShopDO;
import com.echo.dto.Result;
import com.echo.execeptions.BusinessException;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.ProductCategoryService;
import com.echo.util.HttpServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductCategoryManagerController {
    @Autowired
    ProductCategoryService productCategoryService;


    //删除商品种类
    @RequestMapping(value = "removeproductcategory", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> removeProductCategory(Integer productCategoryId, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");
        try {
            int result = productCategoryService.deleteProductCategory(productCategoryId, currentShop.getShopId());
            if (result > 0) {
                map.put("status", true);
            } else {
                map.put("status", false);
                map.put("errMsg", "操作失败");
            }
        } catch (Exception e) {
            map.put("status", false);
            map.put("errMsg", e.getMessage());
        }
        return map;
    }

    //批量添加商品种类
    @RequestMapping(value = "addproductcategorys", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addProductCategorys(@RequestBody List<ProductCategoryDO> productCategoryList, HttpServletRequest request) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        /*
        该商品属于那个shop
        service层throw的异常,Controller用于扑捉
         */
        ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");
        //给每个商品给定shopIp
        for (ProductCategoryDO product : productCategoryList) {
            product.setShopId(currentShop.getShopId());
            product.setCreateTime(new Date());
        }

        if (productCategoryList != null && productCategoryList.size() > 0) {
            try {
                int result = productCategoryService.insertProductCategorys(productCategoryList);
                if (result < 0) {
                    map.put("status", false);
                    map.put("errMsg", EmBusinessError.INNER_ERROE);
                } else {
                    map.put("status", true);
                }
            } catch (BusinessException e) {
                map.put("status", false);
                map.put("errMsg", e.getMsg());
            }
        } else {
            map.put("status", false);
            map.put("errMsg", "输入的商品数量不能少于一个");
        }
        return map;
    }

    //获取商品种类列表
    @RequestMapping(value = "queryproductcategorylist", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<ProductCategoryDO>> queryProductCategoryList(HttpServletRequest request) {
        //根据shopId获取商品的信息列表
        //从session中获取shop
        ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");
        Result<List<ProductCategoryDO>> result = null;
        if (currentShop != null && currentShop.getShopId() > 0) {
            try {
                List<ProductCategoryDO> productCategoryList = productCategoryService.getProductCategory(currentShop.getShopId());
                result = new Result<List<ProductCategoryDO>>(productCategoryList, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return new Result<List<ProductCategoryDO>>(false, EmBusinessError.NULL_ERROR.toString());
        }
        return result;
    }
}

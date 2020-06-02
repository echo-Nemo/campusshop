package com.echo.controller.Fronted;


import com.echo.dataobject.ProductCategoryDO;
import com.echo.dataobject.ProductDO;
import com.echo.dataobject.ShopDO;
import com.echo.service.ProductCategoryService;
import com.echo.service.ProductService;
import com.echo.service.ShopService;
import com.echo.service.ShopcategoryService;
import com.echo.util.HttpServletUtils;
import com.echo.util.execution.ProductExecution;
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
public class ShopDetailsController {

    @Autowired
    ShopService shopService;

    @Autowired
    ShopcategoryService shopcategoryService;

    @Autowired
    ProductCategoryService productCategoryService;

    @Autowired
    ProductService productService;

    /*
    根据shopId查看店铺的详细信息,和该店铺下的商品类别列表
     */
    @RequestMapping(value = "listshopdetailspage", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listShopDetailsPage(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        List<ProductCategoryDO> productCategoryList = null;
        Integer shopId = HttpServletUtils.getInteger(request, "shopId");

        if (shopId != null) {
            try {
                ShopDO shop = shopService.getShopById(shopId);
                productCategoryList = productCategoryService.getProductCategory(shopId);
                modelMap.put("shop", shop);
                modelMap.put("productCategoryList", productCategoryList);
                modelMap.put("status", true);
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "shopId is empty");
        }
        return modelMap;
    }

    //依据查询条件分页列出该店铺下面的所有商品
    @RequestMapping(value = "productlistbyshop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> productListByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Integer pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        Integer pageSize = HttpServletUtils.getInteger(request, "pageSize");
        Integer shopId = HttpServletUtils.getInteger(request, "shopId");

        if ((pageIndex > -1) && (pageSize > -1)) {
            Integer productCategoryId = HttpServletUtils.getInteger(request, "productCategoryId");
            String productName = HttpServletUtils.getString(request, "productName");
            ProductDO product = compactProduct(shopId, productName, productCategoryId);
            ProductExecution productExecution = productService.queryProductList(product, pageIndex, pageSize);
            modelMap.put("productList", productExecution.getProductDList());
            modelMap.put("count", productExecution.getCount());
            modelMap.put("status", true);
        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "pageIndex and pageSize is empty");
        }
        return modelMap;
    }

    //组合查询商品的信息
    public ProductDO compactProduct(Integer shopId, String productName, Integer productCategoryId) {
        ProductDO product = new ProductDO();
        ShopDO shop = new ShopDO();
        if (shopId != -1) {
            shop.setShopId(shopId);
            product.setShop(shop);
        }

        if (productCategoryId != -1) {
            ProductCategoryDO productCategory = new ProductCategoryDO();
            productCategory.setProductCategoryId(productCategoryId);
            product.setProductCategory(productCategory);
        }

        if (productName != null) {
            product.setProductName(productName);
        }
        return product;
    }


}

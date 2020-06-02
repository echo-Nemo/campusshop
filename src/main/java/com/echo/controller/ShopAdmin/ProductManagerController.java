package com.echo.controller.ShopAdmin;

import com.echo.dataobject.ProductCategoryDO;
import com.echo.dataobject.ProductDO;
import com.echo.dataobject.ShopDO;
import com.echo.dto.ImageHolder;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.ProductCategoryService;
import com.echo.service.ProductService;
import com.echo.util.CodeUtil;
import com.echo.util.HttpServletUtils;
import com.echo.util.execution.ProductExecution;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class ProductManagerController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductCategoryService productCategoryService;

    //详情图的最大上传的数量
    public static final Integer MAXIMGCOUNT = 6;

    //根据shopId获取商品列表
    @RequestMapping(value = "getproductListbyshopid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopListByShopId(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        //获取前端传来的页面数，和显示的个数
        int pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        int pageSize = HttpServletUtils.getInteger(request, "pageSize");

        //获取shopId
        // 从当前session中获取店铺信息，主要是获取shopId
        ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");
        if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {

            String productName = HttpServletUtils.getString(request, "productName");
            Integer productCategoryId = HttpServletUtils.getInteger(request, "productCategoryId");

            ProductDO productDO = compactProduct(currentShop.getShopId(), productName, productCategoryId);
            ProductExecution productExecution = productService.queryProductList(productDO, pageIndex, pageSize);

            map.put("productList", productExecution.getProductDList());
            map.put("count", productExecution.getCount());
            map.put("status", true);
        } else {
            map.put("status", false);
            map.put("errMsg", " shopId or pageSize or pageIdex is null");
        }
        return map;
    }

    //根据productId 获取商品信息
    @RequestMapping(value = "getbyproductid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getByProductId(@RequestParam Integer productId) {
        Map<String, Object> map = new HashMap<>();
        if (productId == null || productId < 0) {
            map.put("status", false);
            map.put("errMsg", "传入的值不合法");
        } else {
            try {
                //获取商品信息
                ProductDO product = productService.searchProduct(productId);
                //获取商品类别列表
                List<ProductCategoryDO> productCategoryList = productCategoryService.getProductCategory(product.getShop().getShopId());
                {
                    if (product != null) {
                        map.put("status", true);
                        map.put("product", product);
                        map.put("productCategoryList", productCategoryList);
                    } else {
                        map.put("status", false);
                        map.put("errMsg", EmBusinessError.INNER_ERROE);
                    }
                }
            } catch (Exception e) {
                map.put("status", false);
                map.put("errMsg", e.getMessage());
                return map;
            }
        }
        return map;
    }

    //商品的修改
    @RequestMapping(value = "modifyproduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyProduct(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        /*
        // 是商品编辑时候调用还是上下架操作的时候调用
		// 若为前者则进行验证码判断，后者则跳过验证码判断
         */

        boolean statusChange = HttpServletUtils.getBoolean(request, "statusChange");

        //验证码的判断
        if (!CodeUtil.checkVerfyCode(request) && !statusChange) {
            map.put("status", false);
            map.put("errMsg", "输入了错误的验证码");
            return map;
        }

        //获取缩略图和添加详情图
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDO product = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productList = new ArrayList<>();

        try {
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //提取请求文件中的图片
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail, productList);

            }
        } catch (Exception e) {
            map.put("status", false);
            map.put("errMsg", e.getMessage());
            return map;
        }

        //对product进行操作
        //处理product对象
        try {
            String productStr = HttpServletUtils.getString(request, "productStr");
            product = objectMapper.readValue(productStr, ProductDO.class);
        } catch (Exception e) {
            map.put("status", false);
            map.put("errMsg", e.getMessage());
            return map;
        }

        try {
            if (product != null) {
                //从session中获取当前的shopId，减少对前端数据的依赖
                ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                //修改product
                int result = productService.modifyProduct(product, thumbnail, productList);
                if (result > 0) {
                    map.put("status", true);
                } else {
                    map.put("status", false);
                    map.put("errMsg", EmBusinessError.INNER_ERROE);
                }
            } else {
                map.put("status", false);
                map.put("errMsg", "请输入商品信息");
            }
        } catch (Exception e) {
            map.put("status", false);
            map.put("errMsg", e.toString());
            return map;
        }
        return map;
    }

    //商品的添加
    @RequestMapping(value = "addproduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        //获取验证码
        Boolean flag = CodeUtil.checkVerfyCode(request);
        if (!flag) {
            map.put("status", false);
            map.put("errMsg", "验证码输入错误");
            return map;
        }
        //接受前端参数，包括商品，缩略图，详情图
        //将前端的json数据进行转化
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDO product = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productList = new ArrayList<>();

        try {
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //请求中有文件取出文件
            if (multipartResolver.isMultipart(request)) {
                thumbnail = handleImage(request, thumbnail, productList);
            } else {
                map.put("status", false);
                map.put("errMsg", "请求中必须要有图片");
                return map;
            }
        } catch (IOException e) {
            map.put("status", false);
            map.put("errMsg", e.getMessage());
            return map;
        }

        //处理product对象
        try {
            String productStr = HttpServletUtils.getString(request, "productStr");
            product = objectMapper.readValue(productStr, ProductDO.class);
        } catch (Exception e) {
            map.put("status", false);
            map.put("errMsg", e.getMessage());
            return map;
        }

        try {
            if (product != null && thumbnail != null && productList.size() > 0) {
                //从session中获取当前的shopId，减少对前端数据的依赖
                ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");
                //product.setShopId(currentShop.getShopId());
                product.setShop(currentShop);
                //店铺的添加操作
                int res = productService.addProducts(product, thumbnail, productList);
                if (res > 0) {
                    map.put("status", true);
                } else {
                    map.put("status", false);
                    map.put("errMsg", EmBusinessError.INNER_ERROE);
                }
            }
        } catch (Exception e) {
            map.put("status", false);
            map.put("errMsg", e.getMessage());
            return map;
        }
        return map;
    }

    //条件的组合查询
    private ProductDO compactProduct(Integer shopId, String productName, Integer productCategoryId) {
        ProductDO productDO = new ProductDO();
        ShopDO shop = new ShopDO();
        shop.setShopId(shopId);
        productDO.setShop(shop);

        //可能是productCategoryId进行查询
        if (productCategoryId != -1) {
            ProductCategoryDO productCategory = new ProductCategoryDO();
            productCategory.setProductCategoryId(productCategoryId);
            productDO.setProductCategory(productCategory);
        }

        //可能是productName
        if (productName != null) {
            productDO.setProductName(productName);
        }

        return productDO;
    }

    //图片的处理
    public ImageHolder handleImage(HttpServletRequest request, ImageHolder
            thumbnail, List<ImageHolder> productImgList)
            throws IOException {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //取出缩略图
        CommonsMultipartFile thumbail1File = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");

        //创建ImageHoldet对象
        if (thumbail1File != null) {
            thumbnail = new ImageHolder(thumbail1File.getOriginalFilename(), thumbail1File.getInputStream());
        }

        //处理详情图
        for (int i = 0; i < MAXIMGCOUNT; i++) {
            CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
            if (productImgFile != null) {
                ImageHolder imageHolder = new ImageHolder(productImgFile.getOriginalFilename(), productImgFile.getInputStream());
                productImgList.add(imageHolder);
            } else {
                //上传的图片少于6张
                break;
            }
        }
        return thumbnail;
    }
}


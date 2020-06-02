package com.echo.controller.ShopAdmin;

import com.echo.dataobject.*;
import com.echo.dto.ImageHolder;
import com.echo.execeptions.BusinessException;
import com.echo.service.*;
import com.echo.util.CodeUtil;
import com.echo.util.HttpServletUtils;
import com.echo.util.execution.ShopExecution;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/shop")
public class ShopManagerController {
    @Autowired
    ShopService shopService;

    @Autowired
    ShopcategoryService shopcategoryService;

    @Autowired
    AreaService areaService;

    @Autowired
    OwnerService ownerService;

    @Autowired
    LocalAuthService localAuthService;

    //店铺信息的管理
    @RequestMapping(value = "shopmanagementinfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> shopManagementinfo(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();

        //根据shopId获取到店铺的信息
        int shopId = HttpServletUtils.getInteger(request, "shopId");

        if (shopId < 0) {

            ShopDO shop = (ShopDO) request.getSession().getAttribute("currentShop");

            if (shop == null) {
                //重定向到shopList页面
                map.put("redirect", true);
                map.put("url", "../shop/shoplist");
            } else {
                map.put("redirect", false);
                map.put("shopId", shop.getShopId());
            }
        } else {
            ShopDO currentShop = new ShopDO();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", currentShop);
            map.put("redirect", false);
        }
        return map;
    }


    //显示用户拥有的店铺列表
    @RequestMapping(value = "getshoplist", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        try {
            //从登入的用户session中获取，暂时存放固定值
            OwnerDO owner = (OwnerDO) request.getSession().getAttribute("user");
            ShopDO shop = new ShopDO();
            shop.setOwnerId(owner.getUserId());

            //该用户的所有店铺信息都要进行显示
            ShopExecution shopExecution = shopService.shopList(shop, 0, 100);

            //并将shopList的值放在session中 不然咋进行拦截时获取不到shoList的值
            request.getSession().setAttribute("shopList", shopExecution.getShopList());

            //根据owner中的userId获取本地账号的信息
            LocalAuthDO localAuth = localAuthService.queryByUserId(owner.getUserId());

            //将用户的信息返回给前端
            map.put("shopList", shopExecution.getShopList());

            map.put("user", localAuth);

            map.put("status", true);

        } catch (Exception e) {
            map.put("status", false);
            map.put("errMsg", e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "admshoplist", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> admShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Integer pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        Integer pageSize = HttpServletUtils.getInteger(request, "pageSize");
        if (pageIndex > 0 && pageSize > 0) {
            List<ShopDO> shopList = shopService.getShopList(0, 99);
            if (shopList.size() > 0) {
                modelMap.put("status", true);
                modelMap.put("shopList", shopList);
            }
        } else {
            modelMap.put("status", false);
        }
        return modelMap;
    }

    @RequestMapping(value = "modifyadminshop", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyAdminShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        String shopStr = HttpServletUtils.getString(request, "shopStr");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ShopDO shop = objectMapper.readValue(shopStr, ShopDO.class);
            //修改shop中的审核状态
            if (shop != null) {
                ShopDO statusShop = shopService.modifyShopStatus(shop);
                if (statusShop != null) {
                    modelMap.put("status", true);
                } else {
                    modelMap.put("status", false);
                    modelMap.put("errMsg", "shop更新失败");
                }
            }
        } catch (Exception e) {
            modelMap.put("errMsg", e.getMessage());
            modelMap.put("status", false);
        }
        return modelMap;

    }

    //店铺的修改
    @RequestMapping(value = "modifyshop", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        ShopDO shop = null;
        //验证码的比较
        Boolean codeRes = CodeUtil.checkVerfyCode(request);

        if (!codeRes) {
            map.put("status", false);
            map.put("essMsg", "验证码错误");
            return map;
        }

        //从前端获取shop的信息
        String shopStr = HttpServletUtils.getString(request, "shopStr");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            //json中数据的获取
            shop = objectMapper.readValue(shopStr, ShopDO.class);
            //店铺能进行修改就说明 enable_ststus为1
            shop.setEnableStatus(1);
        } catch (Exception e) {
            map.put("status", false);
            map.put("errMsg", e.getMessage());
            return map;
        }

        //图片的处理
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //传入的店铺图片不为空
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            map.put("status", false);
            map.put("errMsg", "传入的图片不能为空");
        }

        System.out.println(shop + "=======" + shop.getShopId());

        //店铺的修改
        if (shop != null && shop.getShopId() != null) {
            try {
                /*
                这里先假设owner的id
                真实的id应该从登入的用户中进行获取
                 */
                //shop.setOwnerId(1);
                OwnerDO personInfo = (OwnerDO) request.getSession().getAttribute("user");
                shop.setOwnerId(personInfo.getUserId());

                //传入的图片为空
                if (shopImg == null) {
                    shopService.modifyShop(shop, null);
                } else {
                    ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
                    shopService.modifyShop(shop, imageHolder);
                }
                map.put("status", true);

            } catch (Exception e) {
                map.put("status", false);
                map.put("errMsg", e.getMessage() + "店铺提交失败");
                return map;
            }
        } else {
            map.put("status", false);
            map.put("errMsg", "店铺信息为空");
        }
        return map;
    }


    @RequestMapping(value = "getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopById(HttpServletRequest request) throws BusinessException {

        Map<String, Object> map = new HashMap<>();
        Integer shopId = HttpServletUtils.getInteger(request, "shopId");
        //前端传来的空值默认是-1
        if (shopId <= -1) {
            map.put("status", false);
            map.put("errMsg", "shopId为空");
        } else {
            ShopDO shop = shopService.getShopById(shopId);
            //将area传入到前端进行选择
            List<AreaDO> areaList = areaService.queryArea();
            map.put("status", true);
            map.put("areaList", areaList);
            map.put("shop", shop);
        }
        return map;
    }

    @RequestMapping(value = "registershop", method = RequestMethod.POST)
    @ResponseBody

    public Map<String, Object> registerShop(HttpServletRequest request) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        //验证码的比较
        Boolean codeRes = CodeUtil.checkVerfyCode(request);

        if (!codeRes) {
            map.put("status", false);
            map.put("essMsg", "验证码错误");
            return map;
        }

        //从前端获取shop的信息
        String shopStr = HttpServletUtils.getString(request, "shopStr");
        ObjectMapper objectMapper = new ObjectMapper();
        ShopDO shop = null;

        try {
            //json中数据的获取
            shop = objectMapper.readValue(shopStr, ShopDO.class);
        } catch (Exception e) {
            map.put("status", false);
            map.put("errMsg", e.getMessage());
            return map;
        }

        //图片的处理
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            map.put("status", false);
            map.put("errMsg", "图片不能为空");
            return map;
        }

        //注册店铺
        if (shop != null && shopImg != null) {
            //注册的用户放到session中 进行获取
            OwnerDO owner = (OwnerDO) request.getSession().getAttribute("user");
            shop.setOwnerId(owner.getUserId());
            try {
                //图片的传入
                ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
                int result = shopService.addShop(shop, imageHolder);
                //
                if (result < 0) {
                    map.put("status", false);
                    map.put("errMsg", "店铺添加失败");
                    return map;
                } else {
                    /*
                    添加店铺并将店铺的信息存放到session中
                     */
                    map.put("status", true);
                    map.put("errMsg", "店铺添加成功");

                    // 该用户可以操作的店铺列表
                    List<ShopDO> shopList = (List<ShopDO>) request.getSession().getAttribute("shopList");

                    //之前该用户没注册过店铺
                    if (shopList == null || shopList.size() < 0) {
                        shopList = new ArrayList<>();
                    }
                    //将shop添加到list中
                    shopList.add(shop);
                    request.getSession().setAttribute("shopList", shopList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            map.put("status", false);
            map.put("errMsg", "店铺信息不能为空");
        }
        return map;
    }


    //获取店铺初始化页面信息
    @RequestMapping(value = "getshopinitfo", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getShopInit(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<ShopCategoryDO> shopCategoryList = new ArrayList<>();
        List<AreaDO> areaList = new ArrayList<>();
        try {
            shopCategoryList = shopcategoryService.queryShopCategory(null);
            areaList = areaService.queryArea();

            map.put("status", true);
            map.put("areaList", areaList);
            map.put("shopCategoryList", shopCategoryList);

        } catch (Exception e) {
            map.put("status", false);
            map.put("errMsg", "操作失败");
        }
        return map;
    }

    /**
     * 区域信息的获取
     */
    @RequestMapping(value = "getareas", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAreainfo() {
        Map<String, Object> map = new HashMap<>();
        List<AreaDO> areaList = areaService.queryArea();
        if (areaList.size() > 0) {
            map.put("status", true);
            map.put("arealist", areaList);
        } else {
            map.put("status", false);
            map.put("errMsg", "arealist 为空");
        }
        return map;
    }

    @RequestMapping(value = "addarea", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addArea(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>();
        //验证码的比较
        Boolean codeRes = CodeUtil.checkVerfyCode(request);

        if (!codeRes) {
            modelMap.put("status", false);
            modelMap.put("essMsg", "验证码错误");
            return modelMap;
        }
        String areaStr = HttpServletUtils.getString(request, "areaStr");
        ObjectMapper objectMapper = new ObjectMapper();
        AreaDO area = null;


        if (areaStr != null) {
            try {
                area = objectMapper.readValue(areaStr, AreaDO.class);
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }

        if (area != null) {
            String str = areaService.addArea(area);
            {
                if ("success".equals(str)) {
                    modelMap.put("status", true);
                } else {
                    modelMap.put("status", false);
                    modelMap.put("errMsg", "添加操作失败");
                }
            }
        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "area is null");
        }
        return modelMap;
    }
}
package com.echo.controller.ShopAdmin;

import com.echo.dataobject.AwardDO;
import com.echo.dataobject.OwnerDO;
import com.echo.dataobject.ShopDO;
import com.echo.dataobject.UserShopMapDO;
import com.echo.dto.ImageHolder;
import com.echo.service.AwardService;
import com.echo.service.UserShopMapService;
import com.echo.util.CodeUtil;
import com.echo.util.HttpServletUtils;
import com.echo.util.execution.AwardExecution;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.echo.controller.util.SolutionController.compactAwardCondition;
import static com.echo.controller.util.SolutionController.handleImage;

@Controller
@RequestMapping(value = "shop")
public class ShopAwardController {

    @Autowired
    AwardService awardService;

    @Autowired
    UserShopMapService userShopMapService;


    //奖品列表
    @RequestMapping(value = "awardlistshop", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAwardListByshop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        AwardExecution awardExecution = new AwardExecution();

        // 获取分页信息
        int pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        int pageSize = HttpServletUtils.getInteger(request, "pageSize");

        // 店铺Id
        //int shopId = HttpServletUtils.getInteger(request, "shopId");
        ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");

        if (pageIndex > 0 && pageSize > 0 && (currentShop.getShopId()) > 0) {

            //获取奖品的名字
            String awardName = HttpServletUtils.getString(request, "awardName");

            AwardDO awardDO = compactAwardCondition((currentShop.getShopId()), awardName);

            try {
                awardExecution = awardService.listAward(awardDO, pageIndex, pageSize);
                modelMap.put("status", true);
                modelMap.put("awardList", awardExecution.getAwardList());
                modelMap.put("count", awardExecution.getCount());

                OwnerDO user = (OwnerDO) request.getSession().getAttribute("user");

                if (user != null && user.getUserId() != null) {
                    //根据userId和shopId查询 用户是否在该shop中
                    UserShopMapDO userShopMap = userShopMapService.getUserShopMap(user.getUserId(), currentShop.getShopId());

                    //新用户
                    if (userShopMap == null) {
                        modelMap.put("totalPoint", 0);
                    } else {
                        modelMap.put("totalPoint", userShopMap.getPoint());
                    }
                }

            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
            }

        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "传入的参数不合法");
        }
        return modelMap;
    }

    //根据awardId进行查询
    @RequestMapping(value = "getawardbyid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getAwardById(HttpServletRequest request) {
        Map<String, Object> modepMap = new HashMap<>();
        //从前端传来的awardId
        Integer awardId = HttpServletUtils.getInteger(request, "awardId");
        if (awardId > 0) {
            AwardDO awardDO = awardService.getAwardId(awardId);
            modepMap.put("status", true);
            modepMap.put("award", awardDO);
        } else {
            modepMap.put("status", false);
            modepMap.put("errMsg", "award is empty");
        }
        return modepMap;
    }

    //新增award
    @RequestMapping(value = "addaward", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addAward(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        //验证码的校验
        if (!CodeUtil.checkVerfyCode(request)) {
            modelMap.put("status", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        //接受前端参数
        ObjectMapper mapper = new ObjectMapper();
        AwardDO award = null;
        String awardStr = HttpServletUtils.getString(request, "awardStr");

        ImageHolder thumbail = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        try {
            //缩略图的判断
            if (multipartResolver.isMultipart(request)) {
                thumbail = handleImage(request, thumbail);
            }
        } catch (Exception e) {
            modelMap.put("status", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        try {
            //将前端传来的指转化为award对象
            award = mapper.readValue(awardStr, AwardDO.class);
        } catch (Exception e) {
            modelMap.put("status", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        if (award != null && thumbail != null) {
            try {
                //获取当前的shop
                ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());

                AwardExecution awardExecution = awardService.addAward(award, thumbail);
                if ("success".equals(awardExecution.getStatus())) {
                    modelMap.put("status", true);
                } else {
                    modelMap.put("status", false);
                    modelMap.put("errMsg", awardExecution.getStatus());
                }
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "请输入Award信息");
        }

        return modelMap;
    }


    //修改award信息

    @RequestMapping(value = "modifyaward", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> modifyAward(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        boolean statusChange = HttpServletUtils.getBoolean(request, "statusChange");

        //验证码的判断
        if (!CodeUtil.checkVerfyCode(request) && !statusChange) {
            modelMap.put("status", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        //接受前端参数
        ObjectMapper mapper = new ObjectMapper();
        AwardDO award = null;
        String awardStr = HttpServletUtils.getString(request, "awardStr");

        ImageHolder thumbail = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        try {
            //缩略图的判断
            if (multipartResolver.isMultipart(request)) {
                thumbail = handleImage(request, thumbail);
            }
        } catch (Exception e) {
            modelMap.put("status", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        try {
            //将前端传来的指转化为award对象
            award = mapper.readValue(awardStr, AwardDO.class);
        } catch (Exception e) {
            modelMap.put("status", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        //空值的判断
        if (award != null) {
            //获取当前店铺
            ShopDO currentShop = (ShopDO) request.getSession().getAttribute("currentShop");
            award.setShopId(currentShop.getShopId());

            try {
                AwardExecution awardExecution = awardService.modifyAward(award, thumbail);
                if ("success".equals(awardExecution.getStatus())) {
                    modelMap.put("status", true);
                } else {
                    modelMap.put("status", false);
                    modelMap.put("errMsg", awardExecution.getStatus());
                }
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "award为空");
        }
        return modelMap;
    }


    //封装商品查询的条件
    public static AwardDO compactAwardCondition(Integer shopId, String awardName) {
        AwardDO awardDO = new AwardDO();
        awardDO.setShopId(shopId);
        //按奖品名进行模糊查询
        if (awardName != null) {
            awardDO.setAwardName(awardName);
        }
        return awardDO;
    }

    //处理缩略图和详情图
    public static ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail)
            throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 取出缩略图并构建ImageHolder对象
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        if (thumbnailFile != null) {
            thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
        }
        return thumbnail;
    }
}
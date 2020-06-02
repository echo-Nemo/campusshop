package com.echo.controller.InfoMap;


import com.echo.dataobject.AwardDO;
import com.echo.dataobject.OwnerDO;
import com.echo.dataobject.UserAwardMapDO;
import com.echo.dto.ImageHolder;
import com.echo.service.AwardService;
import com.echo.service.OwnerService;
import com.echo.service.UserAwardService;
import com.echo.util.HttpServletUtils;
import com.echo.util.execution.UserAwardExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class MyAwardController {
    @Autowired
    UserAwardService userAwardService;

    @Autowired
    OwnerService ownerService;

    @Autowired
    AwardService awardService;

    //添加用户奖品
    @RequestMapping(value = "/adduserawardmap", method = RequestMethod.POST)
    private Map<String, Object> addUserAwardMap(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>();

        UserAwardExecution sd = null;

        OwnerDO user = (OwnerDO) request.getSession().getAttribute("user");

        Integer awardId = HttpServletUtils.getInteger(request, "awardId");

        UserAwardMapDO userAwardMap = compactUserAwardCondition(user, awardId);

        if (userAwardMap != null) {
            try {
                try {
                    sd = userAwardService.addUserAwardMap(userAwardMap);
                } catch (Exception e) {
                    modelMap.put("status", false);
                    modelMap.put("errMsg", e.getMessage());
                    return modelMap;
                }

                if ("success".equals(sd.getStatus())) {
                    modelMap.put("status", true);
                } else {
                    modelMap.put("status", false);
                    modelMap.put("errMsg", sd.getStatus());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请选择领取的奖品");
        }
        return modelMap;
    }


    //某个用户在在该店铺下的积分记录
    @RequestMapping(value = "/listuserawardmapsbycustomer", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserAwardMapsByCustomer(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取分页信息
        int pageIndex = HttpServletUtils.getInteger(request, "pageIndex");
        int pageSize = HttpServletUtils.getInteger(request, "pageSize");
        //从session中获取用户信息
        OwnerDO user = (OwnerDO) request.getSession().getAttribute("user");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (user != null) && (user.getUserId() != null)) {
            //用户查询条件
            UserAwardMapDO userAwardMapCondition = new UserAwardMapDO();
            userAwardMapCondition.setUser(user);
            String awardName = HttpServletUtils.getString(request, "awardName");
            if (null != awardName && "".equals(awardName)) {
                AwardDO award = new AwardDO();
                award.setAwardName(awardName);
                userAwardMapCondition.setAward(award);
            }
            try {
                UserAwardExecution userAwardMapExecution = userAwardService.getUserAwarrdList(userAwardMapCondition, pageIndex, pageSize);
                modelMap.put("userAwardMapList", userAwardMapExecution.getUserAwardMapDOList());
                modelMap.put("count", userAwardMapExecution.getCount());
                modelMap.put("status", true);
            } catch (Exception e) {
                modelMap.put("status", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("status", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or userId");
        }
        return modelMap;
    }

    private UserAwardMapDO compactUserAwardCondition(OwnerDO user, Integer awardId) {
        UserAwardMapDO userAwardMap = new UserAwardMapDO();
        userAwardMap.setUser(user);
        userAwardMap.setUserAwardId(awardId);
        return userAwardMap;
    }

    /**
     * 处理压缩图和详情图
     *
     * @param request
     * @param thumbnail
     * @return
     * @throws IOException
     */
    private ImageHolder handleImage(javax.servlet.http.HttpServletRequest request, ImageHolder thumbnail)
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

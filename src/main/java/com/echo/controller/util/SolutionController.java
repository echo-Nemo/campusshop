package com.echo.controller.util;

import com.echo.dataobject.AwardDO;
import com.echo.dto.ImageHolder;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SolutionController {

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

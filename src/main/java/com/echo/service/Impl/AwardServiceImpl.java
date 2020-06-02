package com.echo.service.Impl;

import com.echo.dao.AwardDOMapper;
import com.echo.dataobject.AwardDO;
import com.echo.dto.ImageHolder;
import com.echo.execeptions.BusinessException;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.AwardService;
import com.echo.util.ImgUtils;
import com.echo.util.PageCalculate;
import com.echo.util.PathUtils;
import com.echo.util.execution.AwardExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AwardServiceImpl implements AwardService {

    @Autowired
    AwardDOMapper awardDOMapper;

    @Override
    public AwardExecution listAward(AwardDO awardCondition, Integer pageIndex, Integer pageSize) throws BusinessException {
        AwardExecution awardExecution = new AwardExecution();

        if (awardCondition != null && pageIndex != null && pageSize != null) {
            Integer beginIndex = PageCalculate.calculateRowIndex(pageIndex, pageSize);

            List<AwardDO> awardList = awardDOMapper.queryAwardList(awardCondition, beginIndex, pageSize);
            Integer count = awardDOMapper.queryAwardCount(awardCondition);

            awardExecution.setAwardList(awardList);
            awardExecution.setCount(count);
            awardExecution.setStatus("success");
            return awardExecution;
        } else {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
    }

    @Override
    public AwardDO getAwardId(Integer awardId) {
        return awardDOMapper.selectByPrimaryKey(awardId);
    }

    @Override
    public AwardExecution addAward(AwardDO awardDO, ImageHolder thumbail) throws BusinessException {

        if (awardDO != null && awardDO.getShopId() != null) {
            awardDO.setCreateTime(new Date());
            awardDO.setEnableStatus(1);

            //缩略图不为空
            if (thumbail != null) {
                addThumbaile(awardDO, thumbail);
            }

            Integer result = awardDOMapper.insertSelective(awardDO);

            if (result < 0) {
                return new AwardExecution(awardDO, "failure");
            } else {
                return new AwardExecution(awardDO, "success");
            }

        } else {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
    }

    @Override
    public AwardExecution modifyAward(AwardDO awardDO, ImageHolder thumbail) throws BusinessException {
        if (awardDO != null && awardDO.getAwardId() != null) {
            awardDO.setLastEditTime(new Date());

            if (thumbail != null) {
                //获取之前的图片并删除
                AwardDO tempAward = awardDOMapper.selectByPrimaryKey(awardDO.getAwardId());
                if (tempAward.getAwardImg() != null) {
                    ImgUtils.deleteFileOrPath(tempAward.getAwardImg());
                }
                //存储图片，获取相对路径
                addThumbaile(awardDO, thumbail);
            }

            //进行修改
            Integer result = awardDOMapper.updateByPrimaryKeySelective(awardDO);

            if (result < 0) {
                return new AwardExecution(awardDO, "failure");
            } else {
                return new AwardExecution(awardDO, "success");
            }
        } else {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
    }

    //添加缩略图
    public void addThumbaile(AwardDO award, ImageHolder thumbail) {
        //获取图片的存储路径
        String desc = PathUtils.getShopImgPath(award.getShopId());
        String thumbailAddr = ImgUtils.generatorThumbnail(thumbail, desc);
        award.setAwardImg(thumbailAddr);
    }
}

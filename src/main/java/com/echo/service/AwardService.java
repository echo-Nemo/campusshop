package com.echo.service;

import com.echo.dataobject.AwardDO;
import com.echo.dto.ImageHolder;
import com.echo.execeptions.BusinessException;
import com.echo.util.execution.AwardExecution;

public interface AwardService {

    //返回奖品列表
    AwardExecution listAward(AwardDO awardCondition, Integer pageIndex, Integer pageSize) throws BusinessException;

    //根据主键进行查询
    AwardDO getAwardId(Integer awardId);

    //添加奖品和器图片
    AwardExecution addAward(AwardDO awardDO, ImageHolder thumbail) throws BusinessException;

    //修改奖品信息，并替换原来的图片
    AwardExecution modifyAward(AwardDO awardDO, ImageHolder thumbail) throws BusinessException;

}

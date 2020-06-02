package com.echo.service;

import com.echo.dataobject.UserAwardMapDO;
import com.echo.execeptions.BusinessException;
import com.echo.util.execution.UserAwardExecution;

public interface UserAwardService {
    //分页获取userAward列表
    UserAwardExecution getUserAwarrdList(UserAwardMapDO userAwardMapDO, Integer pageIndex, Integer pageSize) throws BusinessException;

    //根据传入的Id获取信息
    UserAwardMapDO getUserAwardMapById(Integer userAwardId);

    //领取奖品,添加记录
    UserAwardExecution addUserAwardMap(UserAwardMapDO userAwardMapDO) throws BusinessException;

    //修改奖品的领取状态
    UserAwardExecution modifyUserAwardMap(UserAwardMapDO userAwardMapDO) throws BusinessException;

}

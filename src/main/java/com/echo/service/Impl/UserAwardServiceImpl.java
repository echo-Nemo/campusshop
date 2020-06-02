package com.echo.service.Impl;

import com.echo.dao.UserAwardMapDOMapper;
import com.echo.dao.UserShopMapDOMapper;
import com.echo.dataobject.UserAwardMapDO;
import com.echo.dataobject.UserShopMapDO;
import com.echo.execeptions.BusinessException;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.UserAwardService;
import com.echo.util.PageCalculate;
import com.echo.util.execution.UserAwardExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserAwardServiceImpl implements UserAwardService {
    @Autowired
    UserAwardMapDOMapper userAwardMapDOMapper;

    @Autowired
    UserShopMapDOMapper userShopMapDOMapper;

    @Override
    public UserAwardExecution getUserAwarrdList(UserAwardMapDO userAwardMapDO, Integer pageIndex, Integer pageSize) throws BusinessException {
        if (userAwardMapDO != null && pageIndex != null && pageSize != null) {
            Integer beginIndex = PageCalculate.calculateRowIndex(pageIndex, pageSize);
            UserAwardExecution userAwardExecution = new UserAwardExecution();
            List<UserAwardMapDO> userAwardMapList = userAwardMapDOMapper.queryUserAwardMapList(userAwardMapDO, beginIndex, pageSize);
            Integer count = userAwardMapDOMapper.queryUserAwardMapCount(userAwardMapDO);
            userAwardExecution.setCount(count);
            userAwardExecution.setUserAwardMapDOList(userAwardMapList);
            return userAwardExecution;
        } else {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
    }


    @Override
    public UserAwardMapDO getUserAwardMapById(Integer userAwardId) {
        return userAwardMapDOMapper.queryUserAwardMapById(userAwardId);
    }

    /*
    用户再某个店铺奖品的添加
     */
    @Override
    @Transactional
    public UserAwardExecution addUserAwardMap(UserAwardMapDO userAwardMapDO) throws BusinessException {

        if (userAwardMapDO != null && userAwardMapDO.getShop() != null && userAwardMapDO.getShop().getShopId() != null) {

            UserAwardExecution userAwardExecution = new UserAwardExecution();

            userAwardMapDO.setCreateTime(new Date());
            userAwardMapDO.setUsedStatus(0);

            if (userAwardMapDO.getPoint() != null && userAwardMapDO.getPoint() > 0) {

                //该用户再该shop中的信息
                UserShopMapDO userShopMapDO = userShopMapDOMapper.queryUserShopMap(userAwardMapDO.getUser().getUserId(), userAwardMapDO.getShop().getShopId());

                if (userShopMapDO != null) {
                    //判断积分是否够用
                    if (userShopMapDO.getPoint() >= userAwardMapDO.getPoint()) {

                        Integer result = userAwardMapDOMapper.insertUserAwardMap(userAwardMapDO);

                        if (result < 0) {
                            throw new BusinessException(EmBusinessError.INNER_ERROE);
                        } else {
                            //在该店铺剩余的积分
                            Integer point = userShopMapDO.getPoint() - userAwardMapDO.getPoint();
                            userShopMapDO.setPoint(point);
                            //进行userShop的修改
                            Integer effect = userShopMapDOMapper.updateUserShopMapPoint(userShopMapDO);
                            if (effect < 0) {
                                throw new BusinessException(EmBusinessError.INNER_ERROE);
                            } else {
                                userAwardExecution.setStatus("success");
                                userAwardExecution.setUserAwardMapDO(userAwardMapDO);
                                return userAwardExecution;
                            }
                        }
                    } else {
                        new BusinessException(EmBusinessError.USER_AWARD_NOT_ENOUGH);
                    }
                } else {
                    throw new BusinessException(EmBusinessError.USER_AWARD_NULL);
                }
            }
        } else {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
        return null;
    }


    @Override
    @Transactional
    public UserAwardExecution modifyUserAwardMap(UserAwardMapDO userAwardMapDO) throws BusinessException {
        UserAwardExecution userAwardExecution = new UserAwardExecution();
        //判断奖品的领取状态是否为空

        if (userAwardMapDO != null && userAwardMapDO.getAward() != null && userAwardMapDO.getUsedStatus() != null) {

            Integer result = userAwardMapDOMapper.updateUserAwardMap(userAwardMapDO);
            if (result < 0) {
                userAwardExecution.setStatus("failure");
            } else {
                userAwardExecution.setStatus("success");
            }
        } else {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
        return userAwardExecution;
    }
}

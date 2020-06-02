package com.echo.service.Impl;

import com.echo.dao.UserProductMapDOMapper;
import com.echo.dao.UserShopMapDOMapper;
import com.echo.dataobject.OwnerDO;
import com.echo.dataobject.ShopDO;
import com.echo.dataobject.UserProductMapDO;
import com.echo.dataobject.UserShopMapDO;
import com.echo.execeptions.BusinessException;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.UserProductMapService;
import com.echo.service.UserShopMapService;
import com.echo.util.PageCalculate;
import com.echo.util.execution.UserProductMapExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class UserProductMapServiceImpl implements UserProductMapService {

    @Autowired
    UserProductMapDOMapper userProductMapDOMapper;

    @Autowired
    UserShopMapService userShopMapService;

    @Autowired
    UserShopMapDOMapper userShopMapDOMapper;


    @Transactional
    @Override
    public UserProductMapExecution getUserProductList(UserProductMapDO userProductMapDO, Integer pageIndex, Integer pageSize) throws BusinessException {
        if (userProductMapDO != null && pageIndex != null && pageSize != null) {
            //从那条数据开始显示
            Integer beginIndex = PageCalculate.calculateRowIndex(pageIndex, pageSize);

            List<UserProductMapDO> userProductList = userProductMapDOMapper.queryUserProductList(userProductMapDO, beginIndex, pageSize);

            Integer count = userProductMapDOMapper.queryUserProductMapCount(userProductMapDO);
            UserProductMapExecution userProductMapExecution = new UserProductMapExecution();
            userProductMapExecution.setCount(count);
            userProductMapExecution.setUserProductMapList(userProductList);
            return userProductMapExecution;

        } else {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
    }


    /*
    用户在该店铺的消费记录
     */
    @Transactional
    @Override
    public UserProductMapExecution addUserProductMap(UserProductMapDO userProductMap) throws BusinessException {

        if (userProductMap != null && userProductMap.getShop() != null && userProductMap.getShop().getShopId() != null
                && userProductMap.getUser() != null && userProductMap.getUser().getUserId() != null) {

            Integer result = userProductMapDOMapper.insertUserProductMap(userProductMap);

            if (result < 0) {
                return new UserProductMapExecution("failure");
            }

            /*
            本次消费记录进行积分  对userShop中的积分处理 老用户进行积分的叠加
            新用户 把该积分添加进去
             */
            if (userProductMap.getPoint() != null && userProductMap.getPoint() > 0) {
                //根据userId和shopId生成userShop对象
                UserShopMapDO userShopMap = userShopMapService.getUserShopMap(userProductMap.getUser().getUserId(), userProductMap.getShop().getShopId());

                //判断是否是新用户
                if (userShopMap != null && userShopMap.getShop().getShopId() != null) {

                    //对积分进行操作
                    userShopMap.setPoint(userShopMap.getPoint() + userProductMap.getPoint());

                    //对userShop进行修改
                    int effect = userShopMapDOMapper.updateUserShopMapPoint(userShopMap);

                    if (effect < 0) {
                        return new UserProductMapExecution("false");
                    } else {
                        return new UserProductMapExecution("success");
                    }
                } else {
                    //新用户
                    UserShopMapDO userShopInfo = compactUserShopInfo(userProductMap.getShop().getShopId(), userProductMap.getUser().getUserId(),
                            userProductMap.getPoint(), userProductMap.getUser().getName(), userProductMap.getShop().getShopName());

                    // userShopInfo.setShop();

                    int effectNum = userShopMapDOMapper.insertUserShopMap(userShopInfo);

                    if (effectNum < 0) {
                        return new UserProductMapExecution("update usershop failure");
                    } else {
                        return new UserProductMapExecution("success");
                    }
                }
            }
        } else {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
        return null;
    }


    //新顾客信息进行封装
    public UserShopMapDO compactUserShopInfo(Integer shopId, Integer userId, Integer point, String userName, String shopName) {
        UserShopMapDO userShopMap = new UserShopMapDO();

        userShopMap.setCreateTime(new Date());

        userShopMap.setPoint(point);

        ShopDO shop = new ShopDO();
        shop.setShopId(shopId);
        shop.setShopName(shopName);

        OwnerDO user = new OwnerDO();
        user.setUserId(userId);
        user.setName(userName);

        userShopMap.setShop(shop);
        userShopMap.setUser(user);
        return userShopMap;
    }

}

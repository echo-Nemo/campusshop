package com.echo.service.Impl;

import com.echo.dao.ShopAuthDOMapper;
import com.echo.dataobject.ShopAuthMapDO;
import com.echo.execeptions.BusinessException;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.ShopAuthMapService;
import com.echo.util.PageCalculate;
import com.echo.util.execution.ShopAuthMapExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {

    @Autowired
    ShopAuthDOMapper shopAuthDOMapper;

    @Override
    public ShopAuthMapExecution getShopAuthMapListByShopId(Integer shopId, Integer pageIndex, Integer pageSize) throws BusinessException {
        if (shopId == null || pageIndex < 0 || pageSize < 0) {
            throw new BusinessException(EmBusinessError.SHOP_AUTH_NULL);
        } else {
            //从那条数据开始显示
            Integer beginIndex = PageCalculate.calculateRowIndex(pageIndex, pageSize);

            List<ShopAuthMapDO> shopAuthMapList = shopAuthDOMapper.queryShopAuthList(shopId, beginIndex, pageSize);

            Integer counts = shopAuthDOMapper.queryShopAuthCount(shopId);

            ShopAuthMapExecution shopAuthMapExecution = new ShopAuthMapExecution();

            shopAuthMapExecution.setCount(counts);

            shopAuthMapExecution.setShopAuthMapList(shopAuthMapList);

            return shopAuthMapExecution;
        }
    }


    @Override
    public ShopAuthMapDO getShopAuthMapById(Integer AuthShopId) {
        return shopAuthDOMapper.queryShopAuthMapById(AuthShopId);
    }

    @Transactional
    @Override
    public ShopAuthMapExecution addShopAuthMap(ShopAuthMapDO shopAuthMap) throws BusinessException {
        ShopAuthMapExecution shopAuthMapExecution = new ShopAuthMapExecution();

        if (shopAuthMap != null && shopAuthMap.getShop() != null && shopAuthMap.getShop().getShopId() != null
                && shopAuthMap.getEmployee() != null && shopAuthMap.getEmployee().getUserId() != null) {
            shopAuthMap.setCreateTime(new Date());
            shopAuthMap.setLastEditTime(new Date());
            shopAuthMap.setEnableStatus(1);
            //店员的titleFlag 为1
            shopAuthMap.setTitleFlag(1);

            //添加授权信息
            try {
                int result = shopAuthDOMapper.insertShopAuthMap(shopAuthMap);
                if (result < 0) {
                    shopAuthMapExecution.setStates("failure");
                    return shopAuthMapExecution;
                } else {
                    shopAuthMapExecution.setStates("success");
                    shopAuthMapExecution.setShopAuthMap(shopAuthMap);
                    return shopAuthMapExecution;
                }

            } catch (Exception e) {
                throw new RuntimeException("授权失败" + e.getMessage());
            }
        } else {
            throw new BusinessException(EmBusinessError.INNER_ERROE);
        }
    }


    //主要对授权的id作判断
    @Override
    public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMapDO shopAuthMapDO) {

        if (shopAuthMapDO != null && shopAuthMapDO.getShopAuthId() != null) {
            shopAuthMapDO.setLastEditTime(new Date());
            int result = shopAuthDOMapper.updateShopAuthMap(shopAuthMapDO);
            if (result < 0) {
                return new ShopAuthMapExecution(shopAuthMapDO, "failure");
            } else {
                return new ShopAuthMapExecution(shopAuthMapDO, "success");
            }
        } else {
            return new ShopAuthMapExecution(shopAuthMapDO, "failure");
        }
    }
}

package com.echo.dao;

import com.echo.dataobject.ShopAuthMapDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopAuthDOMapper {

    //根据shopId分页列出店铺下面的授权信息
    List<ShopAuthMapDO> queryShopAuthList(@Param("shopId") Integer shopId,
                                          @Param("rowIndex") Integer rowIndex, @Param("pageSize") Integer pageSize);

    int queryShopAuthCount(@Param("shopId") Integer shopId);

    //新增一条数据
    int insertShopAuthMap( ShopAuthMapDO shopAuth);

    int updateShopAuthMap(ShopAuthMapDO shopAuth);

    //删除某个店铺的权限
    int deleteShopAuthMap(Integer shopAuthId);

    //通过shopAuthId查询授权信息
    ShopAuthMapDO queryShopAuthMapById(Integer shopAuthId);
}

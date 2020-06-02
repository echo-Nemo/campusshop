package com.echo.service;

import com.echo.dataobject.UserProductMapDO;
import com.echo.execeptions.BusinessException;
import com.echo.util.execution.UserProductMapExecution;

public interface UserProductMapService {
    UserProductMapExecution getUserProductList(UserProductMapDO userProductMapDO, Integer pageIndex, Integer pageSize) throws BusinessException;

    //新增一条数据
    UserProductMapExecution addUserProductMap(UserProductMapDO userProductMap) throws BusinessException;
}

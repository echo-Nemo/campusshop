package com.echo.service;

import com.echo.dataobject.ProductCategoryDO;
import com.echo.execeptions.BusinessException;

import java.util.List;

public interface ProductCategoryService {

    List<ProductCategoryDO> getProductCategory(Integer shopId) throws BusinessException;

    int insertProductCategorys(List<ProductCategoryDO> productCategoryList) throws BusinessException;

    int deleteProductCategory(Integer productCategoryId, Integer shopId) throws BusinessException;

}

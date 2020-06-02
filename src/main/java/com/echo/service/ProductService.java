package com.echo.service;

import com.echo.dataobject.ProductDO;
import com.echo.dto.ImageHolder;
import com.echo.execeptions.BusinessException;
import com.echo.util.execution.ProductExecution;

import java.util.List;


public interface ProductService {
    //添加商品，商品中包含缩略图和商品的详情图(多张)
    int addProducts(ProductDO product, ImageHolder thumbail, List<ImageHolder> productImgList) throws BusinessException;

    //修改商品
    int modifyProduct(ProductDO product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws BusinessException;

    //查询商品
    ProductDO searchProduct(Integer productId);

    //商品列表
    ProductExecution queryProductList(ProductDO product, int pageIndex, int pageSize);

}

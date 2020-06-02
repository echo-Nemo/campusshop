package com.echo.service.Impl;

import com.echo.dao.ProductCategoryDOMapper;
import com.echo.dao.ProductDOMapper;
import com.echo.dataobject.ProductCategoryDO;
import com.echo.execeptions.BusinessException;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    ProductCategoryDOMapper productCategoryDOMapper;

    @Autowired
    ProductDOMapper productDOMapper;

    @Transactional
    @Override
    public int deleteProductCategory(Integer productCategoryId, Integer shopId) throws BusinessException {
        /*
        删除商品种类时，要记得把商品(tb_product)的productCategoryId设置为null
         */
        if (productCategoryId == null || shopId == null) {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        } else {
            //将商品种类对应的商品的productCategoryId设置为null
            int productRes = productDOMapper.updateCategoryToNull(productCategoryId);

            if (productRes < 0) {
                throw new BusinessException(EmBusinessError.PRODUCT_UPDATE_FAIL);
            } else {
                int result = productCategoryDOMapper.deleteProductCategory(productCategoryId, shopId);
                if (result <= 0) {
                    throw new BusinessException(EmBusinessError.INNER_ERROE);
                } else {
                    return result;
                }
            }
        }
    }

    @Transactional
    @Override
    public List<ProductCategoryDO> getProductCategory(Integer shopId) throws BusinessException {
        if (shopId == null) {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
        return productCategoryDOMapper.queryProductCategoryList(shopId);
    }

    @Transactional
    @Override
    public int insertProductCategorys(List<ProductCategoryDO> productCategoryList) throws BusinessException {
        if (productCategoryList != null && productCategoryList.size() > 0) {
            int result = productCategoryDOMapper.batchInsertProductCategory(productCategoryList);
            if (result < 0) {
                throw new BusinessException(EmBusinessError.INNER_ERROE);
            } else {
                return result;
            }
        } else {
            throw new BusinessException(EmBusinessError.PRODUCT_NULL_ERROR);
        }
    }
}

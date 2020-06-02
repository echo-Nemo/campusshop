package com.echo.service.Impl;

import com.echo.dao.ProductDOMapper;
import com.echo.dao.ProductImgDOMapper;
import com.echo.dataobject.ProductDO;
import com.echo.dataobject.ProductImgDO;
import com.echo.dto.ImageHolder;
import com.echo.execeptions.BusinessException;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.ProductService;
import com.echo.util.ImgUtils;
import com.echo.util.PageCalculate;
import com.echo.util.PathUtils;
import com.echo.util.execution.ProductExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductDOMapper productDOMapper;

    @Autowired
    ProductImgDOMapper productImgDOMapper;

    @Transactional
    @Override
    public ProductExecution queryProductList(ProductDO product, int pageIndex, int pageSize) {
        ProductExecution productExecution = new ProductExecution();
        //获取开始的行数
        int rowIndex = PageCalculate.calculateRowIndex(pageIndex, pageSize);
        //商品的总数
        int count = productDOMapper.queryProductCount(product);
        List<ProductDO> productList = productDOMapper.getProductList(product, rowIndex, pageSize);
        productExecution.setProductDList(productList);
        productExecution.setCount(count);
        return productExecution;
    }

    @Transactional
    @Override
    public int modifyProduct(ProductDO product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws BusinessException {
        if (product != null && product.getShop().getShopId() != null) {
            //设置product的默认属性
            product.setLastEditTime(new Date());
            //处理缩略图和详情图
            if (thumbnail != null) {
                //将原有的图片删除
                ProductDO temProduct = productDOMapper.selectByPrimaryKey(product.getProductId());
                if (temProduct.getImgAddr() != null) {
                    ImgUtils.deleteFileOrPath(temProduct.getImgAddr());
                }
                //将新的图片添加进来
                addThumbail(product, thumbnail);
            }

            if (productImgList != null && productImgList.size() > 0) {
                deleteProductImgList(product.getProductId());
                //将新的详情图添加进来
                addProductImgList(product, productImgList);
            }

            //更新商品 选用的是updatePrimaryKeySelectice(Product product)这个方法进行更新
            int result = productDOMapper.updateByPrimaryKeySelective(product);
            if (result < 0) {
                throw new BusinessException(EmBusinessError.INNER_ERROE);
            } else {
                return result;
            }
        } else {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
    }


    @Override
    public ProductDO searchProduct(Integer productId) {
        return productDOMapper.selectByPrimaryKey(productId);
    }

    /*
    thumbail:缩略图
    productImgList:商品的详情图
     */
    @Transactional
    @Override
    public int addProducts(ProductDO product, ImageHolder thumbail, List<ImageHolder> productImgList) throws BusinessException {
        //首先处理product
        if (product != null && product.getShop().getShopId() != null) {
            //设置product的一些默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            //上架
            product.setEnableStatus(1);

            if (thumbail != null) {
                addThumbail(product, thumbail);
            } else {
                throw new BusinessException(EmBusinessError.NULL_ERROR);
            }
            //添加商品
            int res = productDOMapper.insertSelective(product);

            if (res < 0) {
                throw new BusinessException(EmBusinessError.INNER_ERROE);
            }

            //添加详情图
            if (productImgList != null && productImgList.size() > 0) {
                addProductImgList(product, productImgList);
            } else {
                throw new BusinessException(EmBusinessError.NULL_ERROR);
            }
            //表示操作成功
            return res;
        } else {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
    }

    /*
    添加缩略图
     */
    public void addThumbail(ProductDO product, ImageHolder thumbail) {
        //图片的子路径
        String dest = PathUtils.getShopImgPath(product.getShop().getShopId());
        //缩略图的路径
        String imgPath = ImgUtils.generatorThumbnail(thumbail, dest);
        product.setImgAddr(imgPath);
    }

    /*
    删除product详情图中的所有图片
     */
    public void deleteProductImgList(Integer productId) {
        //获取到所有的图片
        List<ProductImgDO> productImgList = productImgDOMapper.queryProductImgs(productId);

        for (ProductImgDO p : productImgList) {
            ImgUtils.deleteFileOrPath(p.getImgAddr());
        }
        //删除数据库中的图片
        productImgDOMapper.deleteProductImgByProductId(productId);
    }

    /*
    批量添加商品的图片
     */
    public void addProductImgList(ProductDO product, List<ImageHolder> imageHolderList) throws BusinessException {

        //获取图片的路径
        String dest = PathUtils.getShopImgPath(product.getShop().getShopId());
        List<ProductImgDO> productImgList = new ArrayList<>();

        // 遍历图片一次去处理，并添加进productImg实体类里
        for (ImageHolder imageHodler : imageHolderList) {
            String imgPath = ImgUtils.generatorThumbnail(imageHodler, dest);
            ProductImgDO productImgDO = new ProductImgDO();
            productImgDO.setCreateTime(new Date());
            productImgDO.setImgAddr(imgPath);
            productImgList.add(productImgDO);
        }
        //有图片要添加就添加
        if (productImgList.size() > 0) {
            int result = productImgDOMapper.batchIndertProductimg(productImgList);
            if (result < 0) {
                throw new BusinessException(EmBusinessError.INNER_ERROE);
            }
        }
    }
}

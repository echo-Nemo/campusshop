package com.echo.service.Impl;

import com.echo.dao.OwnerDOMapper;
import com.echo.dao.ShopAuthDOMapper;
import com.echo.dao.ShopDOMapper;
import com.echo.dataobject.OwnerDO;
import com.echo.dataobject.ShopAuthMapDO;
import com.echo.dataobject.ShopDO;
import com.echo.dto.ImageHolder;
import com.echo.execeptions.BusinessException;
import com.echo.execeptions.EmBusinessError;
import com.echo.service.ShopService;
import com.echo.util.ImgUtils;
import com.echo.util.PageCalculate;
import com.echo.util.PathUtils;
import com.echo.util.execution.ShopExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    ShopDOMapper shopDOMapper;

    @Autowired
    OwnerDOMapper ownerDOMapper;

    @Autowired
    ShopAuthDOMapper shopAuthDOMapper;

    @Transactional
    @Override
    public ShopExecution shopList(ShopDO shop, int pageIndex, int pageSize) {

        int rowIndex = PageCalculate.calculateRowIndex(pageIndex, pageSize);
        //在dao层中shop的信息作了判断，service层不用再作判断
        List<ShopDO> shopList = shopDOMapper.queryShopList(shop, rowIndex, pageSize);

        int count = shopDOMapper.shopCount(shop);
        ShopExecution shopExecution = new ShopExecution();
        shopExecution.setCount(count);
        shopExecution.setShopList(shopList);
        return shopExecution;
    }

    @Override
    public List<ShopDO> getShopList(Integer pageIndex, Integer pageSize) {
        Integer startIndex = PageCalculate.calculateRowIndex(pageIndex, pageSize);
        List<ShopDO> shopList = shopDOMapper.getShopList(pageIndex, pageSize);
        return shopList;
    }

    /*
            RuntimeExeception会是事务进行回滚
            新增店铺时,同时设置自己为店铺的店长
             */
    @Transactional
    @Override
    public int addShop(ShopDO shop, ImageHolder shopImg) throws BusinessException {

        int imgRes;
        if (shop == null) {
            throw new BusinessException(EmBusinessError.NULL_SHOP_INFO);
        }
        try {
            //添加店铺 0审核中
            shop.setEnableStatus(0);
            //这些值不能改变
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());

            int addRes = shopDOMapper.insertSelective(shop);

            if (addRes <= 0) {
                throw new BusinessException(EmBusinessError.INNER_ERROE);
            } else {
                //把店铺的图片添加进来
                if (shopImg == null) {
                    throw new BusinessException(EmBusinessError.NULL_IMAGE);
                } else {
                    //给店铺添加图片
                    addShopImg(shop, shopImg);

                    //更新店铺的信息
                    imgRes = shopDOMapper.updateByPrimaryKey(shop);

                    if (imgRes < 0) {
                        throw new BusinessException(EmBusinessError.INNER_ERROE);
                    } else {
                        OwnerDO user = new OwnerDO();
                        user = ownerDOMapper.selectByPrimaryKey(shop.getOwnerId());

                        //执行新增ShopAuth的操作
                        ShopAuthMapDO shopAuthMap = new ShopAuthMapDO();
                        shopAuthMap.setEmployee(user);
                        shopAuthMap.setShop(shop);
                        shopAuthMap.setCreateTime(new Date());
                        shopAuthMap.setLastEditTime(new Date());
                        shopAuthMap.setTitle("店长");
                        shopAuthMap.setTitleFlag(0);
                        shopAuthMap.setEnableStatus(1);
                        int result = shopAuthDOMapper.insertShopAuthMap(shopAuthMap);
                        if (result < 0) {
                            throw new BusinessException(EmBusinessError.SHOP_AUTH_FAILURE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.INNER_ERROE);
        }
        return imgRes;
    }

    public void addShopImg(ShopDO shop, ImageHolder fileImg) {
        //获取图片的目录相对路径
        String dest = PathUtils.getShopImgPath(shop.getShopId());

        String shopImgAddr = ImgUtils.generatorThumbnail(fileImg, dest);
        //把图片设置进去
        shop.setShopImg(shopImgAddr);
    }

    @Override
    public ShopDO getShopById(Integer shopId) throws BusinessException {
        if (shopId == null) {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
        return shopDOMapper.selectById(shopId);
    }


    /*
    更新店铺
     */
    @Transactional
    @Override
    public ShopDO modifyShop(ShopDO shop, ImageHolder thumbnail) throws BusinessException {
        if (shop == null || shop.getShopId() == null) {
            throw new BusinessException(EmBusinessError.NULL_SHOP_INFO);
        } else {
            //判断店铺的图片是否要更新
            if (thumbnail.getImageInputStream() != null || thumbnail.getImageName() != null) {
                ShopDO temShop = shopDOMapper.selectById(shop.getShopId());
                if (temShop.getShopImg() != null) {
                    ImgUtils.deleteFileOrPath(temShop.getShopImg());
                }
                //把新的图片传进来
                addShopImg(shop, thumbnail);
            }

            //更新店铺
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            int res = shopDOMapper.updateByPrimaryKey(shop);
            if (res < 0) {
                throw new BusinessException(EmBusinessError.INNER_ERROE);
            } else {
                shop = shopDOMapper.selectById(shop.getShopId());
            }
        }
        return shop;
    }

    @Override
    public ShopDO modifyShopStatus(ShopDO shop) {
        if (shop != null) {
            int result = shopDOMapper.modifyShopStatus(shop);
            if (result > 0) {
                return shop;
            }
        }
        return null;
    }

    @Override
    public List<ShopDO> queryShopByShopcategory(Integer shopCategoryId) throws BusinessException {
        List<ShopDO> shopDOList = null;

        if (shopCategoryId != null) {
            shopDOList = shopDOMapper.queryShopListByShopCategory(shopCategoryId);
        } else {
            throw new BusinessException(EmBusinessError.NULL_ERROR);
        }
        return shopDOList;
    }
}

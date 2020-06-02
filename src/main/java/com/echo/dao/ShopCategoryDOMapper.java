package com.echo.dao;

import com.echo.dataobject.ShopCategoryDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopCategoryDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_shop_category
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    int deleteByPrimaryKey(Integer shopCategoryId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_shop_category
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    int insert(ShopCategoryDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_shop_category
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    int insertSelective(ShopCategoryDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_shop_category
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    ShopCategoryDO selectByPrimaryKey(Integer shopCategoryId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_shop_category
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    int updateByPrimaryKeySelective(ShopCategoryDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_shop_category
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    int updateByPrimaryKey(ShopCategoryDO record);

    //根据父目录查询除子目录
    List<ShopCategoryDO> queryShopcategory(@Param("shopCategoryCondition") ShopCategoryDO shopCategoryCondition);
}
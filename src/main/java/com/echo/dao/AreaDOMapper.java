package com.echo.dao;

import com.echo.dataobject.AreaDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_area
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    int deleteByPrimaryKey(Integer areaId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_area
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    int insert(AreaDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_area
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    int insertSelective(AreaDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_area
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    AreaDO selectByPrimaryKey(Integer areaId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_area
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    int updateByPrimaryKeySelective(AreaDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_area
     *
     * @mbg.generated Thu Dec 26 18:10:11 CST 2019
     */
    int updateByPrimaryKey(AreaDO record);

    List<AreaDO> queryArea();
}
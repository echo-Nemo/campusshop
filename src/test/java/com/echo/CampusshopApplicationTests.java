//package com.echo;
//
//import com.echo.dao.AreaDOMapper;
////import com.echo.dao.ShopCategoryDOMapper;
//import com.echo.dao.OwnerDOMapper;
//import com.echo.dao.ShopAuthDOMapper;
//import com.echo.dao.ShopDOMapper;
//import com.echo.dataobject.AreaDO;
//import com.echo.dataobject.OwnerDO;
//import com.echo.dataobject.ShopAuthMapDO;
//import com.echo.dataobject.ShopDO;
//import com.echo.util.RedisConfig.RedisDao;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Date;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class CampusshopApplicationTests {
//
//    @Autowired
//    ShopDOMapper shopDOMapper;
//
//    @Autowired
//    AreaDOMapper areaDOMapper;
//
//    @Autowired
//    ShopAuthDOMapper shopAuthDOMapper;
//
//    @Autowired
//    OwnerDOMapper ownerDOMapper;
//
//
//    @Test
//    public void test1() {
////        List<ShopDO> shopList = shopDOMapper.queryShopListByShopCategory(43);
////        System.out.println(shopList);
//        List<AreaDO> areaDOList = areaDOMapper.queryArea();
//        System.out.println(areaDOList);
//    }
//
//    @Autowired
//    RedisDao redisDao;
//
//    @Test
//    public void testRedis() {
//        redisDao.setKey("name", "jike");
//        redisDao.setKey("age", "11");
//        System.out.println(redisDao.getValue("age"));
//    }
//
//}

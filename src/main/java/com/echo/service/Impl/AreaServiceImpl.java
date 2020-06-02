package com.echo.service.Impl;

import com.echo.dao.AreaDOMapper;
import com.echo.dataobject.AreaDO;
import com.echo.service.AreaService;
import com.echo.util.wechat.MyX509TrustManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    //定义一个key
    private static String AREALISTKEY = "AREALIST";

    @Autowired
    AreaDOMapper areaDOMapper;

    @Autowired
    RedisTemplate redisTemplate;


    @Override
    @Transactional
    public List<AreaDO> queryArea() {

        String key = AREALISTKEY;
        List<AreaDO> areaList = null;

        //判断redis中是否有键为key的缓存
        boolean hasKey = redisTemplate.hasKey(key);

        if (hasKey) {
            //直接获取缓存的数据
            areaList = (List<AreaDO>) redisTemplate.opsForList().leftPop(key);
            System.out.println("=====" + areaList.size() + "======");
            return areaList;
        } else {
            //先获取在写到缓存中
            areaList = areaDOMapper.queryArea();
            redisTemplate.opsForList().leftPush(key, areaList);
            return areaList;
        }
    }

    @Override
    public String addArea(AreaDO areaDO) {
        Integer result = 0;

        if (areaDO != null) {
            areaDO.setCreateTime(new Date());
            areaDO.setLastEditTime(new Date());
            result = areaDOMapper.insertSelective(areaDO);
        }
        if (result > 0) {
            return "success";
        } else {
            return "failure";
        }
    }
}

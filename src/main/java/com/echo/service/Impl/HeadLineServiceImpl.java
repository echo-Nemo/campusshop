package com.echo.service.Impl;

import com.echo.dao.HeadLineDOMapper;
import com.echo.dataobject.HeadLineDO;
import com.echo.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {

    @Autowired
    HeadLineDOMapper headLineDOMapper;

    @Autowired
    RedisTemplate redisTemplate;

    private static String HEADLINELIST = "HEADLIST";

    @Override
    public List<HeadLineDO> queryHeadLineList(Integer enableStatus) {

        List<HeadLineDO> headLineList = null;

        String key = HEADLINELIST;
        boolean hasKeys = redisTemplate.hasKey(key);

        if (hasKeys) {
            headLineList = (List<HeadLineDO>) redisTemplate.opsForList().leftPop(key);
            System.out.println(headLineList);
            return headLineList;
        } else {
            headLineList = headLineDOMapper.selectHeadLineList(enableStatus);
            redisTemplate.opsForList().leftPush(key, headLineList);
            return headLineList;
        }
    }
}

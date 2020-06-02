package com.echo.util.RedisConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDao {

    @Resource
    private StringRedisTemplate template=new StringRedisTemplate();

    public void setKey(String key, String value) {
        ValueOperations<String, String> ops = template.opsForValue();
        //设置过期时间
        ops.set(key, value, 1, TimeUnit.MINUTES);
    }

    public String getValue(String key) {
        ValueOperations<String, String> ops = template.opsForValue();
        return ops.get(key);
    }
}

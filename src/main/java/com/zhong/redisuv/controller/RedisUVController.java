package com.zhong.redisuv.controller;

import com.zhong.redisuv.properties.RedisUVProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uv")
public class RedisUVController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUVProperties redisUVProperties;

    /**
     * 获得某天uv
     * @param date
     * @return
     */
    @RequestMapping("/{date}")
    public Integer getUV(@PathVariable(value = "date") String date){
        return redisTemplate.opsForHyperLogLog().size(redisUVProperties.getPrefix()+":"+date).intValue();
    }

}

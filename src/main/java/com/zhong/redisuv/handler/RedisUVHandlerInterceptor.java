package com.zhong.redisuv.handler;

import com.zhong.redisuv.properties.RedisUVProperties;
import com.zhong.redisuv.utils.IpAddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RedisUVHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUVProperties redisUVProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String CUR_DATE = LocalDateTime.now().format(DateTimeFormatter.ofPattern(redisUVProperties.getDateFormat()));
        final String PV_KEY = redisUVProperties.getPrefix() + ":" + CUR_DATE;
        String ipAddress = IpAddressUtil.getIpAddress(request);
        redisTemplate.opsForHyperLogLog().add(PV_KEY,ipAddress);//hll
        return true;
    }





}

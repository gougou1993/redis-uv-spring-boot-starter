package com.zhong.redisuv.config;

import com.zhong.redisuv.controller.RedisUVController;
import com.zhong.redisuv.handler.RedisUVHandlerInterceptor;
import com.zhong.redisuv.properties.RedisUVProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.Serializable;

@Configuration
@ConditionalOnClass(RedisUVHandlerInterceptor.class)
@EnableConfigurationProperties(RedisUVProperties.class)
@ConditionalOnProperty(prefix = "uv",name = "isopen",havingValue = "true")
public class RedisUVWebConfig implements WebMvcConfigurer {

    @Bean(name = "redisTemplate")
    @ConditionalOnClass(value = {RedisTemplate.class, LettuceConnectionFactory.class})
    @ConditionalOnMissingBean(RedisTemplate.class)
    RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return  redisTemplate;

    }

    @Bean(name = "redisUVHandlerInterceptor")
    @ConditionalOnMissingBean(RedisUVHandlerInterceptor.class)
    @ConditionalOnProperty(
            prefix = "uv",
            name = "isopen",
            havingValue = "true"
    )
    public RedisUVHandlerInterceptor redisUVHandlerInterceptor(){ // HandlerInterceptor中注入service

        return new RedisUVHandlerInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean(RedisUVController.class)
    @ConditionalOnProperty(
            prefix = "uv",
            name = "api",
            havingValue = "true"
    )
    RedisUVController redisPVController(){
        return new RedisUVController();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(redisUVHandlerInterceptor()).addPathPatterns("/**");//拦截所有
    }

}

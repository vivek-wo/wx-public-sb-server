package com.wei.wx.sb.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author kaolvkaolv
 * @date 2023/3/19 11:48
 * @description Redis自定义序列化方式
 */
@Slf4j
@Configuration
public class RedisConfig {

  /**
   * RedisTemplate配置
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      LettuceConnectionFactory lettuceConnectionFactory) {
    // 设置序列化
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
        Object.class);
    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(om);
    // 配置redisTemplate
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(lettuceConnectionFactory);
    RedisSerializer<?> stringSerializer = new StringRedisSerializer();
    // key序列化
    redisTemplate.setKeySerializer(stringSerializer);
    // value序列化
    redisTemplate.setValueSerializer(stringSerializer);
    // Hash key序列化
    redisTemplate.setHashKeySerializer(stringSerializer);
    // Hash value序列化
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  @Bean("springSessionDefaultRedisSerializer")
  public RedisSerializer<Object> defaultRedisSerializer() {
    log.debug("自定义Redis Session序列化加载成功");
    return valueSerializer();
  }

  private RedisSerializer<Object> valueSerializer() {

    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
        Object.class);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
        ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    return jackson2JsonRedisSerializer;
  }
}
package com.wei.wx.sb.interceptor;

import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author kaolvkaolv
 * @date 2023/3/19
 * @description 拦截器配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Resource
  private LoginInterceptor loginInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 自定义拦截器，添加拦截路径和排除拦截路径
    registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
  }
}
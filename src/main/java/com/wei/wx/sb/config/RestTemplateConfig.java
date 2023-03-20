package com.wei.wx.sb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author WEI
 * @date 2023/3/20
 * @description
 */
@Configuration
public class RestTemplateConfig {

  /**
   * 基于OkHttp3配置RestTemplate
   * @return
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
  }

}

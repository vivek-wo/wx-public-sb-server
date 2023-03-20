package com.wei.wx.sb.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wei.wx.sb.exception.CustomException;
import java.time.Duration;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


/**
 * @author kaolvkaolv
 * @date 2023/3/19 11:48
 * @description 微信工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WxUtil {

  private final RestTemplate restTemplate;

  private final RedisTemplate<String, Object> redisTemplate;

  public final static String wx_token_key = "wx:token";

  @Value("${wx.appId}")
  private String addId;

  @Value("${wx.secret}")
  private String secret;

  private final String GET_WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

  private final String GET_WX_STABLE_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/stable_token";


  private final String GET_WX_QRCODE_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create";


  private final String GET_WX_QRCODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode";


  /**
   * 获取微信公众号token
   */
  public String getWxToken() {
    HttpHeaders headers = new HttpHeaders();
    String getTokenUrl =
        GET_WX_ACCESS_TOKEN_URL + "?grant_type=client_credential&appid=" + addId + "&secret="
            + secret;
    HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.postForEntity(getTokenUrl,
        request,
        String.class);
    log.info("请求微信获取token返回值为:{{}}", response.getBody());
    if (response.getStatusCode().value() == HttpStatus.OK.value()) {
      if (StrUtil.isBlank(response.getBody())) {
        throw new CustomException("token信返回值为空");
      }
      JSONObject jsonObject = JSONObject.parseObject(response.getBody());
      String token = jsonObject.getString("access_token");
      if (StrUtil.isBlank(token)) {
        throw new CustomException("请求微信token异常");
      } else {
        long expireTime = jsonObject.getLong("expires_in");
        redisTemplate.opsForValue().set(wx_token_key, token, Duration.ofSeconds(expireTime));
      }
      return token;
    }
    throw new CustomException("请求微信获取token接口异常");
  }

  /**
   * 获取微信公众号token，采用stable接口
   */
  public String getWxStableToken() {
    HttpHeaders headers = new HttpHeaders();
    MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
    headers.setContentType(type);
    HashMap<String, Object> map = new HashMap<>(16);
    map.put("grant_type", "client_credential");
    map.put("appid", addId);
    map.put("secret", secret);
    HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(map, headers);
    ResponseEntity<String> response = restTemplate.postForEntity(GET_WX_STABLE_ACCESS_TOKEN_URL,
        request,
        String.class);
    log.info("请求微信获取token返回值为:{{}}", response.getBody());
    if (response.getStatusCode().value() == HttpStatus.OK.value()) {
      if (StrUtil.isBlank(response.getBody())) {
        throw new CustomException("token信返回值为空");
      }
      JSONObject jsonObject = JSONObject.parseObject(response.getBody());
      String token = jsonObject.getString("access_token");
      if (StrUtil.isBlank(token)) {
        throw new CustomException("请求微信token异常");
      } else {
        long expireTime = jsonObject.getLong("expires_in");
        redisTemplate.opsForValue().set(wx_token_key, token, Duration.ofSeconds(expireTime));
      }
      return token;
    }
    throw new CustomException("请求微信获取token接口异常");
  }

  /**
   * 获取公众号的ticket
   */
  public String getQrCode(String token, HashMap<String, Object> map) {
    HttpHeaders headers = new HttpHeaders();
    MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
    headers.setContentType(type);
    HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(map, headers);
    ResponseEntity<String> response = restTemplate.postForEntity(
        GET_WX_QRCODE_TICKET_URL + "?access_token=" + token,
        request,
        String.class);
    log.info("请求微信二维码返回值为:{{}}", response.getBody());
    if (response.getStatusCode().value() == HttpStatus.OK.value()) {
      if (StrUtil.isBlank(response.getBody())) {
        throw new CustomException("请求微信二维码链接返回值为空");
      }
      return response.getBody();
    }
    throw new CustomException("请求微信获取token接口异常");
  }

  /**
   * 通过ticket换取二维码
   */
  public void getQrCode(String ticket) {

  }

}

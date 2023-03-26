package com.wei.wx.sb.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wei.wx.sb.dto.TicketDTO;
import com.wei.wx.sb.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


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

  public final static String WX_TOKEN_KEY = "wx:token";

  @Value("${wx.appId}")
  private String addId;

  @Value("${wx.secret}")
  private String secret;

  /*获取微信公众号Token*/
  private final String GET_WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
  /*获取微信公众号Token*/
  private final String GET_WX_STABLE_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/stable_token";

  /*创建二维码*/
  private final String GET_WX_QRCODE_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create";

  /*换取二维码*/
  private final String GET_WX_QRCODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode";

  /**
   * 获取微信公众号token
   */
  public String getWxToken() {
    String getTokenUrl =
        GET_WX_ACCESS_TOKEN_URL + "?grant_type=client_credential&appid=" + addId + "&secret="
            + secret;
    ResponseEntity<String> response = restTemplate.getForEntity(getTokenUrl, String.class);
    return parseRequestToken(response);
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
        request, String.class);
    return parseRequestToken(response);
  }

  private String parseRequestToken(ResponseEntity<String> response) {
    log.info("请求微信获取token返回值为:{{}}", response.getBody());
    if (response.getStatusCode().value() == HttpStatus.OK.value()) {
      if (StrUtil.isBlank(response.getBody())) {
        throw new CustomException("token返回值为空");
      }
      JSONObject jsonObject = JSONObject.parseObject(response.getBody());
      String token = jsonObject.getString("access_token");
      if (StrUtil.isBlank(token)) {
        throw new CustomException("请求微信token异常");
      } else {
        long expireTime = jsonObject.getLong("expires_in");
//        redisTemplate.opsForValue().set(WX_TOKEN_KEY, token, Duration.ofSeconds(expireTime));
      }
      return token;
    }
    throw new CustomException("请求微信获取token接口异常");
  }

  /**
   * 获取公众号的ticket
   */
  public TicketDTO getQrCodeTicket(String token, Map<String, Object> map) {
    HttpHeaders headers = new HttpHeaders();
    MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
    headers.setContentType(type);
//    String json = JSONObject.toJSONString(map);
//    log.info("请求Ticket JSON {}", json);
    HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
    ResponseEntity<TicketDTO> response = restTemplate.postForEntity(
            GET_WX_QRCODE_TICKET_URL + "?access_token=" + token,
            request, TicketDTO.class);
    log.info("请求微信二维码返回值为:{{}}", response.getBody());
    if (response.getStatusCode().value() == HttpStatus.OK.value()) {
      if (response.getBody() == null) {
        throw new CustomException("获取公众号Ticket失败");
      }
      return response.getBody();
    }
    throw new CustomException("请求微信Ticket异常");
  }

  /**
   * 通过ticket换取二维码
   *
   * @param ticket
   * @return 返回二维码图片Base64
   */
  public String getQrCodeByTicket(String ticket) {
    String getQrCodeUrl;
    try {
      getQrCodeUrl = GET_WX_QRCODE_URL + "?ticket=" + URLEncoder.encode(ticket, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      throw new CustomException("获取二维码失败，ENCODE 失败");
    }
    ResponseEntity<byte[]> response = restTemplate.getForEntity(getQrCodeUrl, byte[].class);
    if (response.getStatusCode().value() == HttpStatus.OK.value()) {
      return Base64.getEncoder().encodeToString(response.getBody());
    }
    throw new CustomException("获取二维码失败异常");
  }
}

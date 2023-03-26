package com.wei.wx.sb.service.impl;

import com.wei.wx.sb.dto.TicketDTO;
import com.wei.wx.sb.service.WxService;
import com.wei.wx.sb.util.WxUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author kaolvkaolv
 * @date 2023/3/19 13:43
 * @description 逻辑实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WxServiceImpl implements WxService {

  private final WxUtil wxUtil;

  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public String gerQrCode(String scene_str, Integer permanent) {
    //1. 请求Token
//    String token = redisTemplate.opsForValue().get(WxUtil.WX_TOKEN_KEY);
//    if (StrUtil.isBlank(token)) {
    String token = wxUtil.getWxToken();
//    }
    //2. 请求Ticket
    Map<String, Object> requestQrCodeTicketMap = createGetQrCodeTicketRequestBody(scene_str, permanent);
    TicketDTO ticketDTO = wxUtil.getQrCodeTicket(token, requestQrCodeTicketMap);

    //3. 换去二维码图片
    return wxUtil.getQrCodeByTicket(ticketDTO.getTicket());
  }

  private Map<String, Object> createGetQrCodeTicketRequestBody(String scene_str, Integer permanent) {
    Map<String, Object> actionInfoMap = new HashMap<>();
    Map<String, Object> sceneMap = new HashMap<>();
    sceneMap.put("scene_str", scene_str);
    actionInfoMap.put("scene", sceneMap);
    log.info("获取Ticket, 请求 {} ", actionInfoMap);
    Map<String, Object> requestMap = new HashMap<>(16);
    // 该参数自定设置
    requestMap.put("action_info", actionInfoMap);
    if (!Objects.equals(permanent, 1)) {
      requestMap.put("action_name", "QR_STR_SCENE");
      // 非永久默认2小时
      requestMap.put("expire_seconds", 2 * 60 * 60);
    } else {
      requestMap.put("action_name", "QR_LIMIT_STR_SCENE");
    }
    return requestMap;
  }

  private void getQrCodeTicket() {

  }
}

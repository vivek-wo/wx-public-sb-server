package com.wei.wx.sb.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wei.wx.sb.service.WxService;
import com.wei.wx.sb.util.WxUtil;
import java.util.HashMap;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
  public void gerQrCode(String scene_id, Integer permanent) {
    JSONObject jsonObject = new JSONObject();
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("scene_id", scene_id);
    jsonObject.put("scene", jsonObject1.toJSONString());
    System.out.println(jsonObject.toJSONString());
    HashMap<String, Object> map = new HashMap<>(16);
    // 该参数自定设置
    map.put("action_info", jsonObject.toJSONString());
    if (!Objects.equals(permanent, 1)) {
      map.put("action_name", "QR_STR_SCENE");
      // 非永久默认2小时
      map.put("expire_seconds", 2 * 60 * 60);
    } else {
      map.put("action_name", "QR_LIMIT_STR_SCENE");
    }
    String token = redisTemplate.opsForValue().get(WxUtil.wx_token_key);
    if (StrUtil.isBlank(token)) {
      token = wxUtil.getWxStableToken();
    }
    String result = wxUtil.getQrCode(token, map);
  }
}

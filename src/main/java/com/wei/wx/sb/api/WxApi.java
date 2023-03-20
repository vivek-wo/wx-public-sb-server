package com.wei.wx.sb.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author WEI
 * @date 2023/3/20
 * @description
 */
public interface WxApi {

  /**
   * 获取二维码
   *
   * @param scene_id  二维码参数
   * @param permanent 是否永久链接 1-永久 0:临时
   */
  @GetMapping("/getQrCode")
  public void getQrCode(@RequestParam("scene_id") String scene_id,
      @RequestParam("permanent") Integer permanent);
}

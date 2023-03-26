package com.wei.wx.sb.service;

/**
 * @author kaolvkaolv
 * @date 2023/3/19 13:42
 * @description 逻辑层
 */
public interface WxService {


  /**
   * 获取二维码
   *
   * @param scene_str
   * @param permanent
   */
  String gerQrCode(String scene_str, Integer permanent);
}

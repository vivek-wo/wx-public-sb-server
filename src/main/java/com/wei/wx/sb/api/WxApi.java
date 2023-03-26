package com.wei.wx.sb.api;

import com.wei.wx.sb.vo.ResultVO;
import org.springframework.http.ResponseEntity;
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
   * @param scene_str 二维码参数
   * @param permanent 是否永久链接 1-永久 0:临时
   */
  @GetMapping("/getQrCode")
  public ResponseEntity<ResultVO<String>> getQrCode(@RequestParam("scene_str") String scene_str,
                                                    @RequestParam("permanent") Integer permanent);
}

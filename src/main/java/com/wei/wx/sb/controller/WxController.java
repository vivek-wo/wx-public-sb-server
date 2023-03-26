package com.wei.wx.sb.controller;

import com.wei.wx.sb.api.WxApi;
import com.wei.wx.sb.service.WxService;
import com.wei.wx.sb.vo.ResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kaolvkaolv
 * @date 2023/3/19 12:53
 * @description 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/wx")
@RequiredArgsConstructor
public class WxController implements WxApi {

  private final WxService wxService;

  /**
   * 回调-获取用户的OpenId
   */
  @PostMapping("/getOpenId")
  public void getOpenId(HttpServletRequest request)
          throws IOException, DocumentException {
    Map<String, String> map = new HashMap<>(16);
    SAXReader saxReader = new SAXReader();
    Document read = saxReader.read(request.getInputStream());
    Element rootElement = read.getRootElement();
    List<Element> elements = rootElement.elements();
    for (Element e : elements) {
      map.put(e.getName(), e.getStringValue());
    }
    log.info("关注/取关公众号数据为:{{}}", map);
    if (map.containsKey("Event") && "subscribe".equals(map.get("Event"))) {
      String openId = map.get("FromUserName");
      log.info("用户:{{}}关注了公众账", openId);
    } else {
      log.info("用户取关了公众号:{{}}", map.get("FromUserName"));
    }
  }

  /**
   * 生成二维码图片
   *
   * @param scene_str 二维码参数
   * @param permanent 是否永久链接 1-永久 0:临时
   */
  @Override
  public ResponseEntity<ResultVO<String>> getQrCode(String scene_str, Integer permanent) {
    String qrCodeBase64Image = wxService.gerQrCode(scene_str, permanent);
    return ResponseEntity.ok().body(ResultVO.success(qrCodeBase64Image));
  }

}

package com.wei.wx.sb.controller;

import com.alibaba.fastjson.JSONObject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kaolvkaolv
 * @date 2023/3/19 12:53
 * @description 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/wx")
@RequiredArgsConstructor
public class WxController {

  /**
   * 获取微信用户的openId
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
   * 生成二维码链接
   *
   * @param scene_id 二维码参数
   * @param permanent 是否永久链接 1-永久 0:临时
   */
  @GetMapping("/getQrCode")
  public void getQrCode(@RequestParam("scene_id") String scene_id,
      @RequestParam("permanent") Integer permanent) {

  }

  public static void main(String[] args) throws FileNotFoundException, DocumentException {
//    InputStream inputStream = new FileInputStream("d:/jar/1.xml");
//    Map<String, String> map = new HashMap<>(16);
//    SAXReader saxReader = new SAXReader();
//    Document read = saxReader.read(inputStream);
//    Element rootElement = read.getRootElement();
//    List<Element> elements = rootElement.elements();
//    for (Element e : elements) {
//      map.put(e.getName(), e.getStringValue());
//    }
//    System.out.println(map);

//    JSONObject jsonObject = new JSONObject();
//    JSONObject jsonObject1 = new JSONObject();
//    jsonObject1.put("scene_id", 123);
//    jsonObject.put("scene", jsonObject1.toJSONString());
//    System.out.println(jsonObject.toJSONString());
  }
}

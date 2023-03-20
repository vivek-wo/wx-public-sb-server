package com.wei.wx.sb.interceptor;

import com.wei.wx.sb.util.IpUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author kaolvkaolv
 * @date 2023/3/19
 * @description 拦截器
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {


  /*
   * 视图渲染之后的操作
   */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj,
      Exception arg3) {
    log.info("============================请求路径:{{}}结束===================", request.getRequestURI());
  }

  /*
   * 处理请求完成后视图渲染之前的处理操作
   */
  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj,
      ModelAndView arg3) {
  }

  /*
   * 进入controller层之前拦截请求
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) {
    log.info("============================请求路径为:{{}},ip为:{{}}===================",
        request.getRequestURI(), IpUtil.getIpAddress(request));
    return true;
  }

}
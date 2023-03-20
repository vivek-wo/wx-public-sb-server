package com.wei.wx.sb.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author kaolvkaolv
 * @date 2023/3/19 12:42
 * @description 自定义异常
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends RuntimeException {

  private String message;


}

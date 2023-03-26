package com.wei.wx.sb.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResultVO<T> {
    private int code;
    private String msg;
    private T body;

    public static <T> ResultVO<T> success(T body) {
        return new ResultVO<>(200, null, body);
    }

    public static ResultVO<Object> error(int code, String message) {
        return new ResultVO<>(code, message, null);
    }
}

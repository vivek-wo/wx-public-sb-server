package com.wei.wx.sb.dto;

import lombok.Data;

@Data
public class TicketDTO {
    private String ticket;

    private Integer expire_seconds;

    private String url;
}

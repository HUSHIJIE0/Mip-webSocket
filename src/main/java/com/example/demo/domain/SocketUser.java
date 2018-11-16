package com.example.demo.domain;

import lombok.Data;

import javax.websocket.Session;

/**
 * @Author: Michael
 * @Date: Created in 14:14 2018/6/9
 * @Desciption:
 */
@Data
public class SocketUser {
    private Session session;
    private String userId;
    private String orgCode;
}

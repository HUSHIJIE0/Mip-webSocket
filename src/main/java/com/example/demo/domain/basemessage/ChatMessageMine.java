package com.example.demo.domain.basemessage;

import lombok.Data;

/**
 * @Author: Michael
 * @Date: Created in 14:27 2018/6/9
 * @Desciption:
 */
@Data
public class ChatMessageMine {
    private String avatar;
    private String content;
    private String id;
    private boolean mine;
    private String username;
    private String msgHisId;
}

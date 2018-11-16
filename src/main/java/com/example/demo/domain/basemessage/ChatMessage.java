package com.example.demo.domain.basemessage;

import lombok.Data;

/**
 * @Author: Michael
 * @Date: Created in 14:22 2018/6/9
 * @Desciption:
 */
@Data
public class ChatMessage {
    private ChatMessageTo to;
    private ChatMessageMine mine;

}

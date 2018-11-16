package com.example.demo.domain.basemessage;

import lombok.Data;

/**
 * @Author: Michael
 * @Date: Created in 14:27 2018/6/9
 * @Desciption:
 */
@Data
public class ChatMessageTo {
    private String avatar;
    private String groupname;
    private String historyTime;
    private String sign;
    private String id;
    private String name;
    private String type;
    private String msgHisId;
}

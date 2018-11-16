package com.example.demo.service;

import com.example.demo.domain.SocketUser;

import java.util.Map;

/**
 * @Author: Michael
 * @Date: Created in 14:16 2018/6/9
 * @Desciption:
 */
public interface IUserManager {
    boolean addUser(SocketUser var1);

    boolean removeUser(SocketUser var1);

    int getOnlineCount();

    SocketUser getUser(String var1);

    Map<String,SocketUser> getOrgUser(String orgCode);

    Map<String,Map<String,SocketUser>> getAllByGroup();
}

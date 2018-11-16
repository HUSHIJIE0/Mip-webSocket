package com.example.demo.service.impl;

import com.example.demo.domain.SocketUser;
import com.example.demo.service.IUserManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Michael
 * @Date: Created in 14:15 2018/6/9
 * @Desciption:
 */
public class UserManager implements IUserManager {
    Log log = LogFactory.getLog(this.getClass());
    //用户组
    private static Map<String, SocketUser> socketUserMap;
    //科室组
    private static Map<String, Map<String,SocketUser>> orgGroupMap;

    private Map<String,SocketUser> socketUserOrgMap;

    private static UserManager manager = new UserManager();


    private UserManager() {
        socketUserMap = new ConcurrentHashMap();
        orgGroupMap = new ConcurrentHashMap<String, Map<String,SocketUser>>();
        socketUserOrgMap = new ConcurrentHashMap<String,SocketUser>();
    }

    public static IUserManager getInstance() {
        return manager;
    }

    @Override
    public boolean addUser(SocketUser user) {
        this.removeUser(user.getUserId(),user.getOrgCode());
        socketUserMap.put(user.getUserId(), user);
        //处理分组信息
        if(orgGroupMap.containsKey(user.getOrgCode())){
            socketUserOrgMap = orgGroupMap.get(user.getOrgCode());
            socketUserOrgMap.put(user.getUserId(),user);
        }else{
            socketUserOrgMap = new ConcurrentHashMap();
            socketUserOrgMap.put(user.getUserId(),user);
            orgGroupMap.put(user.getOrgCode(),socketUserOrgMap);
        }
        return true;
    }

    @Override
    public boolean removeUser(SocketUser user) {
        return user != null ? this.removeUser(user.getUserId(),user.getOrgCode()) : false;
    }

    @Override
    public int getOnlineCount() {
        return socketUserMap.size();
    }

    @Override
    public SocketUser getUser(String userId) {
        return socketUserMap.containsKey(userId) ? socketUserMap.get(userId) : new SocketUser();
    }

    @Override
    public Map<String,SocketUser> getOrgUser(String orgCode) {
        return orgGroupMap.containsKey(orgCode) ? orgGroupMap.get(orgCode) : new ConcurrentHashMap<String,SocketUser>();
    }

    @Override
    public Map<String, Map<String, SocketUser>> getAllByGroup() {
        return orgGroupMap;
    }

    private boolean removeUser(String sessionUserId, String userOrgCode) {
        try {
            if(socketUserMap.containsKey(sessionUserId)){
                socketUserMap.remove(sessionUserId);
                socketUserOrgMap =orgGroupMap.get(userOrgCode);
                socketUserOrgMap.remove(sessionUserId);
                orgGroupMap.put(userOrgCode,socketUserOrgMap);
            }
        } catch (Exception var3) {
            this.log.info("id为" + sessionUserId + "用户已经被移除了");
        }

        return true;
    }

}

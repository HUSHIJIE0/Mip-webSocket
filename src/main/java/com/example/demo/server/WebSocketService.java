package com.example.demo.server;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.HpbPushHistory;
import com.example.demo.domain.SocketUser;
import com.example.demo.domain.basemessage.ChatMessage;
import com.example.demo.domain.basemessage.ChatMessageMine;
import com.example.demo.domain.basemessage.ChatMessageTo;
import com.example.demo.service.HpbPushHistoryService;
import com.example.demo.service.IUserManager;
import com.example.demo.service.impl.UserManager;
import com.example.demo.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Michael
 * @Date: Created in 14:02 2018/6/9
 * @Desciption:
 */
@ServerEndpoint("/WebSocket/{id}/{org}")
@ClientEndpoint()
@Component
public class WebSocketService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    private IUserManager userManager = UserManager.getInstance();

    @OnOpen
    public void onOpen(@PathParam("id") String id, @PathParam("org") String org, Session session) {
        SocketUser socketUserExist = userManager.getUser(id);
        userManager.removeUser(socketUserExist);
        SocketUser socketUser = new SocketUser();
        socketUser.setSession(session);
        socketUser.setUserId(id);
        socketUser.setOrgCode(org);
        userManager.addUser(socketUser);
        logger.info("有新连接加入！当前在线人数为" + userManager.getOnlineCount());
        this.getUnReadMessage(org);
    }


    @OnClose
    public void onClose(@PathParam("id") String id, @PathParam("org") String org, Session session) {
        SocketUser user = new SocketUser();
        user.setSession(session);
        user.setUserId(id);
        user.setOrgCode(org);
        userManager.removeUser(user);
        logger.info("当前在线用户：" + userManager.getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("来自客户端的消息:" + message);
        ChatMessage chatMessage = JSON.parseObject(message, ChatMessage.class);
        ChatMessageTo chatMessageTo = chatMessage.getTo();
        chatMessage.setTo(chatMessageTo);
        insert(chatMessage);
        if ("friend".equals(chatMessageTo.getType())) {
            this.sendToFriend(chatMessage);
        } else if ("group".equals(chatMessageTo.getType())) {
            this.sendTOGroup(chatMessage);
        }


    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("发生错误");
        logger.debug(error.getMessage());
    }


    private void sendToFriend(ChatMessage chatMessage) {
        try {
            String sendToId = chatMessage.getTo().getId();
            if (userManager.getUser(sendToId).getSession() != null) {
                userManager.getUser(sendToId).getSession().getBasicRemote().sendText(JSON.toJSONString(chatMessage));
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    private void sendTOGroup(ChatMessage chatMessage) {
        try {
            String sendToOrg = chatMessage.getTo().getId();
            Map<String,SocketUser> orgUsers = userManager.getOrgUser(sendToOrg);
            for(String userId : orgUsers.keySet()){
                if (userManager.getUser(userId).getSession() != null) {
                    userManager.getUser(userId).getSession().getBasicRemote().sendText(JSON.toJSONString(chatMessage));
                }
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    private void getUnReadMessage(String id) {
        HpbPushHistoryService hpbPushHistoryService = SpringContextUtil.getBean(HpbPushHistoryService.class);
        List<HpbPushHistory> results = hpbPushHistoryService.findData((System.currentTimeMillis() -1000*60*10),id);
        if (results != null && !results.isEmpty()) {
            for(int i = 0; i < results.size(); i++) {
                HpbPushHistory HpbPushHistory = results.get(i);
                logger.info("发送消息：" + HpbPushHistory.getData());
                ChatMessage chatMessage = JSON.parseObject(HpbPushHistory.getData(), ChatMessage.class);
                ChatMessageMine chatMessageMine = chatMessage.getMine();
                chatMessageMine.setMsgHisId(HpbPushHistory.getId());
                chatMessage.setMine(chatMessageMine);
                if ("friend".equals(chatMessage.getTo().getType())) {
                    this.sendToFriend(chatMessage);
                } else if ("group".equals(chatMessage.getTo().getType())) {
                    this.sendTOGroup(chatMessage);
                }

            }
        }

    }

    public String insert(ChatMessage chatMessage) {
        HpbPushHistoryService hpbPushHistoryService = SpringContextUtil.getBean(HpbPushHistoryService.class);
        HpbPushHistory hpbPushHistory = new HpbPushHistory();
        hpbPushHistory.setComeFrom(chatMessage.getMine().getId());
        hpbPushHistory.setPushTo(chatMessage.getTo().getId());
        hpbPushHistory.setData(JSON.toJSONString(chatMessage));
        hpbPushHistory.setType(chatMessage.getTo().getType());
        String id = UUID.randomUUID().toString().replace("-", "");
        hpbPushHistory.setId(id);
        hpbPushHistoryService.save(hpbPushHistory);
        return id;
    }

}

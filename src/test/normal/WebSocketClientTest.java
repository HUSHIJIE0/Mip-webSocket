import com.alibaba.fastjson.JSON;
import com.example.demo.domain.basemessage.ChatMessage;
import com.example.demo.domain.basemessage.ChatMessageMine;
import com.example.demo.domain.basemessage.ChatMessageTo;
import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClientTest {

    private static Logger logger = Logger.getLogger(WebSocketClientTest.class);
    public static WebSocketClient client;
    public static void main(String[] args) {
        try {
            client = new WebSocketClient(new URI("ws://localhost:8080/WebSocket/server"),new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    logger.info("握手成功");
                }

                @Override
                public void onMessage(String msg) {
                    logger.info("收到消息=========="+msg);
                    if(msg.equals("over")){
                        client.close();
                    }

                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    logger.info("链接已关闭");
                }

                @Override
                public void onError(Exception e){
                    e.printStackTrace();
                    logger.info("发生错误已关闭");
                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        client.connect();
        logger.info(client.getDraft());
        while(!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)){
            logger.info("正在连接...");
        }
        //连接成功,发送信息
        ChatMessage chatMessage = new ChatMessage();
        ChatMessageTo chatMessageTo = new ChatMessageTo();
        chatMessageTo.setId("1");
        ChatMessageMine chatMessageMine = new ChatMessageMine();
        chatMessageMine.setContent("我是服务端发送的消息哦！");
        chatMessage.setTo(chatMessageTo);
        chatMessage.setMine(chatMessageMine);
        String message = JSON.toJSONString(chatMessage);
        client.send(message);

    }
}

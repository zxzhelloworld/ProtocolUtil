package com.zxz.protocol.WebScoket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@ServerEndpoint(value = "/ws/{projectId}",configurator = WebSocketConfig.class)
public class WebSocketServer {
    @PostConstruct
    public void init() {
        System.out.println("websocket 加载");
    }

    private static final AtomicInteger OnlineCount = new AtomicInteger(0);

    private static ConcurrentHashMap<String, CopyOnWriteArraySet<Session>> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("projectId") String projectId) {
        if (webSocketMap.containsKey(projectId)) {
            CopyOnWriteArraySet<Session> sessions = webSocketMap.get(projectId);
            sessions.add(session);
        } else {
            CopyOnWriteArraySet<Session> sessionSet = new CopyOnWriteArraySet<>();
            sessionSet.add(session);
            webSocketMap.put(projectId, sessionSet);
        }
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        log.info("有连接加入，当前连接数为：{}", cnt);

        SendMessage(session, "连接建立成功！");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        String projectId = session.getRequestParameterMap().get("projectId").get(0);
        if (webSocketMap.containsKey(projectId)) {
            CopyOnWriteArraySet<Session> sessions = webSocketMap.get(projectId);
            sessions.remove(session);
            if (sessions.isEmpty()) {
                webSocketMap.remove(projectId);
            }
        }
        int cnt = OnlineCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息：{}", message);
        //SendMessage(session, "收到消息，消息内容：" + message);

    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session
     * @param message
     */
    public static void SendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(String.format("%s (From Server，Session ID=%s)", message, session.getId()));
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session
     * @param object
     */
    public static void SendMessage(Session session, Object object) {
        try {
            String message = JSON.toJSONString(object);
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 指定projectId发送消息
     *
     * @param object    消息体
     * @param projectId
     * @throws IOException
     */
    public static void BroadCastInfo(Object object, String projectId) throws IOException {
        CopyOnWriteArraySet<Session> sessions = webSocketMap.get(projectId);
        if (sessions != null) {
            for (Session session : sessions) {
                if (session.isOpen()) {
                    SendMessage(session, object);
                }
            }
        }
    }
}

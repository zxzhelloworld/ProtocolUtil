package com.zxz.protocol.SocketIO;

import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @Author: zhangxiaozhou
 * @Date: 2020/8/10 18:05
 * @Des: 监听器（运行于整个项目运行时周期:init-destoryed）
 */

@Configuration
@WebListener
public class SocketIOLisener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //启动Socketio服务
        SocketIO socketio = new SocketIO();
        socketio.startServer();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //关闭Socketio服务
        SocketIO socketio = new SocketIO();
        socketio.stopSocketio();
    }
}

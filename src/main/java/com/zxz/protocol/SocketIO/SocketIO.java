package com.zxz.protocol.SocketIO;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;

import java.util.Collection;

/**
 * @Author: zhangxiaozhou
 * @Date: 2020/8/10 18:03
 * @Description: netty-socketio工具类 创建、添加和启动客户端 消息推送 关闭服务
 */
public class SocketIO {
    private static SocketIOServer socketIOServer;


    /**
     * @Title: startSocketio
     * @Description: 创建服务添加客户端
     */
    public void startSocketio() {

        // 配置
        Configuration conf = new Configuration();
        // 指定要主机ip地址，这个和页面请求ip地址一致
        conf.setHostname( "localhost" );
        // 指定端口号
        conf.setPort( 8092 );
        // 设置最大的WebSocket帧内容长度限制
        conf.setMaxFramePayloadLength( 1024 * 1024 );
        // 设置最大HTTP内容长度限制
        conf.setMaxHttpContentLength( 1024 * 1024 );

        socketIOServer = new SocketIOServer( conf );

        ConnectListener connect = new ConnectListener() {

            @Override
            public void onConnect( SocketIOClient client ) {}
        };
        // 添加客户端
        socketIOServer.addConnectListener( connect );
        socketIOServer.start();
    }


    /**
     * @Title: pushArr
     * @Description: 全体消息推送
     * @param type
     *            前台根据类型接收消息，所以接收的消息类型不同，收到的通知就不同 推送的事件类型
     * @param content
     *            推送的内容
     */
    public void pushArr( String type, Object content ) {

        // 获取全部客户端
        Collection<SocketIOClient> allClients = socketIOServer.getAllClients();
        for( SocketIOClient socket : allClients ) {
            socket.sendEvent( type, content );
        }
    }


    /**
     * @Title: startServer
     * @Description: 启动服务
     */
    public void startServer() {

        if( socketIOServer == null ) {
            new Thread( new Runnable() {

                @Override
                public void run() {

                    startSocketio();
                }
            } ).start();
        }
    }


    /**
     * @Title: stopSocketio
     * @Description: 停止服务
     */
    public void stopSocketio() {

        if( socketIOServer != null ) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

}

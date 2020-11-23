package com.zxz.protocol.Controller;

import com.zxz.protocol.SocketIO.SocketIO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: zhangxiaozhou
 * @Date: 2020/8/11 15:44
 */
@RestController
public class SocketIOController {
    /**
     * 测试报警推送服务:主要应用一个方法pushArr(前端代码有问题，测试未通过)
     */
    @GetMapping("/pushMessage")
    public void pushMessage(){
        SocketIO socketio = new SocketIO();
        //这里发送的消息内容可以结合具体场景自定义对象
        socketio.pushArr("connect_msg", "今天下午2点开会");
    }

}

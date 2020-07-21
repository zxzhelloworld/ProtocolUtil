package com.zxz.protocol.MQTT;
import lombok.extern.log4j.Log4j2;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;


/**
 * @author zhangxiaozhou
 * @date 2020/7/20 15:45
 * @des 配置channel消息处理
 */
@Log4j2
@Component
public class MqttMessageHandler implements MessageHandler {

	@ServiceActivator(inputChannel = "channel")
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		String payload=String.valueOf(message.getPayload()); //消息内容
		String topic= (String) message.getHeaders().get("mqtt_receivedTopic"); //topic
		log.info("收到消息---{}", message);
	}

}

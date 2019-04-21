package com.shi.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.shi.Contasnts;

/**
 * 
 * @ClassName NewTask
 * @Description 该类主要目的是使用RabbitMQ的消息确认功能,详情见该类的消息消费方:Work.java
 * @author Shi
 * @Date 2019年3月17日 下午1:27:41
 * @version 1.0.0
 */
public class NewTask {

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("10.250.1.58");
		connectionFactory.setVirtualHost("aic45dev");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		/**
		 * rabbitMQ不允许定一个名称相同,但参数不一样的队列,会返回报错
		 */
		channel.queueDeclare(Contasnts.QUEUENAME, false, false, false, null);
		
		System.out.println(" [x] begin send message");
		for(int i=0;i<3;i++){
			String message = "sent message.....";
			System.out.println("开始发送消息,time:"+System.currentTimeMillis());
			channel.basicPublish("", Contasnts.QUEUENAME, null, message.getBytes());
		}
		System.out.println(" [x] end send message");
		channel.close();
		connection.close();
	}
}

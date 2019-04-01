package com.shi.consumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogs {

//	private static final String EXCHANG_NAME="logs";
	private static final String EXCHANG_NAME="direct_logs";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("10.250.1.58");
		connectionFactory.setVirtualHost("aic45dev");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
//		channel.exchangeDeclare(EXCHANG_NAME, BuiltinExchangeType.FANOUT);
		channel.exchangeDeclare(EXCHANG_NAME, BuiltinExchangeType.DIRECT);
		
		String queueName = channel.queueDeclare().getQueue();
		/**
		 * 当EXCHANGE为FANOUT时,队列的routing_key是无效的,所以可以不用写
		 */
//		channel.queueBind(queueName, EXCHANG_NAME, "");
		/**
		 * 当EXCHANGE为DIRECT时,绑定routing_key
		 */
		channel.queueBind(queueName, EXCHANG_NAME, "3");
		channel.queueBind(queueName, EXCHANG_NAME, "2");
		
		System.out.println(queueName+": [*] Waiting for message:");
		DeliverCallback callback = (consumerTag,deliver)->{
			String message = new String(deliver.getBody(),"UTF-8");
			System.out.println(" [x] Received '"+message+"'");
		};
		channel.basicConsume(queueName, callback, consumerTag -> {});
	}
}

package com.shi.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLog {

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
		
		/**
		 * 注释掉的这两行实际上没有什么用的,因为对于生产者来说,消息是直接发送到exchange上去,不会发送到队列
		 */
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANG_NAME, "");
		
		String severity = getSeverty();
		String message = args.length<1?"info:HELLO WORLD! severty level '"+2+"'":String.join("", args);
//		channel.basicPublish(EXCHANG_NAME, "", null, message.getBytes());
		channel.basicPublish(EXCHANG_NAME, severity, null, message.getBytes());
		
		
		System.out.println(" [x] sent message '"+message+"'");
		channel.close();
		connection.close();
	}
	
	public static String getSeverty(){
		String level = "default";
		int severity = (int) (Math.random()*10);
		if(severity>=0 && severity<=3){
			level = "1";
		}else if(severity>=4 && severity<=7){
			level = "2";
		}else{
			level = "3";
		}
		return level;
	}
}

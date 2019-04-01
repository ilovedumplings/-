package com.shi.client;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.RpcClient;
import com.shi.Contasnts;

public class Send {

	
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("10.250.1.58");
		connectionFactory.setVirtualHost("aic45dev");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(Contasnts.QUEUENAME, false, false, false, null);
		String message = "I AM IRONMANSSSS";
		channel.basicPublish("", Contasnts.QUEUENAME, null, message.getBytes());
		System.out.println(" [X] sent '"+message+"'");
		
	}
}

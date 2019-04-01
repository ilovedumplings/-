package com.shi.consumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.shi.Contasnts;

public class Recv {

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("10.250.1.58");
		connectionFactory.setVirtualHost("aic45dev");
		
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(Contasnts.QUEUENAME, false, false, false, null);
		
		System.out.println("Waiting for a message ...");
		
		DeliverCallback deliverCallback = (consumerTag,delivery) -> {
			String message = new String(delivery.getBody(),"UTF-8");
			System.out.println(" [x] received '"+message+"'");
		};
		
		channel.basicConsume(Contasnts.QUEUENAME, false, deliverCallback, consumerTag->{});
		
	}
}

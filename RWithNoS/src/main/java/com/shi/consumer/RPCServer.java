package com.shi.consumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RPCServer {

	private final static String RPC_QUEUE_NAME = "rpc_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("10.250.1.58");
		connectionFactory.setVirtualHost("aic45dev");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
		channel.queuePurge(RPC_QUEUE_NAME);
		channel.basicQos(1);

		
		System.out.println(" [x] Awaiting RPC requests");
		channel.basicConsume(RPC_QUEUE_NAME, (consumer,deliver)->{
			//???这个方法是不是相当于rabbitMq的一个监听???
			System.out.println("start new thread");
			AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
					.correlationId(deliver.getProperties().getCorrelationId()).build();
			String message = new String(deliver.getBody(),"UTF-8");
			System.out.println(" [*] receive message :'"+message+"' ");
			
			channel.basicPublish("", deliver.getProperties().getReplyTo(), basicProperties, "I am IRON MAN".getBytes());
			channel.basicAck(deliver.getEnvelope().getDeliveryTag(), false);
		}, consumerTag->{});
		System.out.println("main Thread over");
		
	}
}

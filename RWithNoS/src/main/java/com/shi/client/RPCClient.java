package com.shi.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RPCClient {

	private final static String RPC_QUEUE_NAME = "rpc_queue";
	private final static String RPC_QUEUE_CALLBACK_NAME = "rpc_queue_callBack";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("10.250.1.58");
		connectionFactory.setVirtualHost("aic45dev");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		/**
		 * 此行代码在通联的rpc框架配置中,相当于声明一个队列
		 */
		channel.queueDeclare(RPC_QUEUE_CALLBACK_NAME, false, false, false, null);
		
		//每个新建队列(queue)都会自动绑定到默认交换机上，绑定的路由键（routing key）名称与队列名称相同
		BasicProperties basicProperties = new BasicProperties().
				builder().replyTo(RPC_QUEUE_CALLBACK_NAME)
				.correlationId("fffff").build();
		channel.basicPublish("", RPC_QUEUE_NAME, basicProperties, "RPC请求".getBytes());
		
		System.out.println(" [x] send message :'"+"RPC请求"+"',OVER!");
		
		channel.basicConsume(RPC_QUEUE_CALLBACK_NAME, true, (consumerTag,delivery)->{
			System.out.println("开启线程.等待rpc回复");
			if(delivery.getProperties().getCorrelationId().equals(basicProperties.getCorrelationId())){
				String response = new String(delivery.getBody(),"UTF-8");
				System.out.println(response);
				try {
					channel.close();
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		},consumerTag->{
		});
		System.out.println("主线程结束!");
	}
	
}

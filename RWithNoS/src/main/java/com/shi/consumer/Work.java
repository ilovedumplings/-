package com.shi.consumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.shi.Contasnts;

public class Work {

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("10.250.1.58");
		connectionFactory.setVirtualHost("aic45dev");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(Contasnts.QUEUENAME, false, false, false, null);
		System.out.println(" [*] Waiting for message .");
		
		channel.basicQos(1);
		/**
		 * 第二个参数 autoAck消息确认,默认是开启的,autoAck=true时关闭
		 * 之前测试的时候为false,开启了消息确认,但却没有进行确认,导致了
		 * 每启动一个进程,就会有消息进来.
		 */
		channel.basicConsume(Contasnts.QUEUENAME, false, (consumerTag,deliver)->{
			String message = new String(deliver.getBody(),"utf-8");
			System.out.println(" [x] receive :"+message);
			channel.basicAck(deliver.getEnvelope().getDeliveryTag(), false);
		},consumerTag->{});
	}
	
	
	@SuppressWarnings("unused")
	private static void doWork(String message){
		for(char ch : message.toCharArray()){
			if(ch == '.'){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

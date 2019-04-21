package com.shi.consumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.shi.Contasnts;

/**
 * 
 * @ClassName Work
 * @Description 该类主要验证了RabbitMQ的消息确认机制
 * 当channel.basicConsume的autoACK参数为true的时候,意味着队列的消息确认机制是默认开启的
 * 此时每一个消息进来,如果服务正常,RabbitMQ服务就会收到一个确认消息,然后将刚才发出的消息
 * 在服务中删除掉,如果channel消费的时候autoACK的为false的时候,默认不使用rabbitMQ的自动确认
 * 机制,需要手动实现消息确认:channel.basicAck(deliver.getEnvelope().getDeliveryTag(), false);
 * @author Shi
 * @Date 2019年4月21日 下午2:46:52
 * @version 1.0.0
 */
public class Work {

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("10.250.1.58");
		connectionFactory.setVirtualHost("aic45dev");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(Contasnts.QUEUENAME, false, false, false, null);
		System.out.println(" [*] Waiting for message .");
		
		channel.basicQos(2);//此方法声明该队列一次可以处理多少条消息
		/**
		 * 第二个参数 autoAck消息确认,默认是开启的,autoAck=true时关闭
		 * 之前测试的时候为false,开启了消息确认,但却没有进行确认,导致了
		 * 每启动一个进程,就会有消息进来.
		 */
		channel.basicConsume(Contasnts.QUEUENAME, false, (consumerTag,deliver)->{
			System.out.println("开始处理消息,time:"+System.currentTimeMillis());
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

package com.shi.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * @ClassName RPCClient
 * @Description 这个类就是RabbitMQ中比较高级的RPC的实现
 * 简单描述下这个功能的实现,相当于在生产者中定义了两个队列,
 * 队列A:用来作为rounting key,约定作为rounting key的队列A在消费者中
 * 就是消费队列,
 * 队列B:用来作为RPC中生产者接收返回消息的消费者
 * 生产者发送消息的时候,需要将队列B绑定到Properties中去,此时到了消费者后,
 * 消费者读取来读取小的Properties来分析,需要发送到那个队列中去
 * 其实说白了,就是两个消息队列,互相发送消息,这两个消息队列是如何知道彼此的
 * 信息的呢,一个是约定,一个就是配置到消息中去
 * @author Shi
 * @Date 2019年4月21日 下午3:04:36
 * @version 1.0.0
 */
public class RPCClient {

	private final static String RPC_QUEUE_NAME = "rpc_queue";
	private final static String RPC_QUEUE_CALLBACK_NAME = "rpc_queue_callBack";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("10.250.1.58");
		connectionFactory.setVirtualHost("aic45dev");
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		//声明一个返回队列,用来接收消息
		channel.queueDeclare(RPC_QUEUE_CALLBACK_NAME, false, false, false, null);
		//每个新建队列(queue)都会自动绑定到默认交换机上，绑定的路由键（routing key）名称与队列名称相同
		BasicProperties basicProperties = new BasicProperties().
				builder().replyTo(RPC_QUEUE_CALLBACK_NAME)
				.correlationId("fffff").build();
		//把消息和rounting key绑定在一起,在RPC实现中Rounting key其实就是消费端的对列名
		channel.basicPublish("", RPC_QUEUE_NAME, basicProperties, "RPC请求".getBytes());
		
		System.out.println(" [x] send message :'"+"RPC请求"+"',OVER!");
		
		//因为是RPC,所以生产者同样的也是要作为消费者的,所以要定义一个消费队列
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

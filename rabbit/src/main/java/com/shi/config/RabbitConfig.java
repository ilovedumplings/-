package com.shi.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


/**
 * 
 * @ClassName RabbitConfig
 * @Description 要先定义工厂bean,然后获取rabbitTemplate,再定义exchange
 * RabbitTemplate 相当于定义一个channel,绑定交换机和队列,但是单单绑定交换机和队列
 * 是不行的,还要声明交换机和队列,最后将交换机和队列还有rounting key绑定到一起,形成
 * 一个完成的消息链路
 * @author Shi
 * @Date 2019年3月15日 上午11:04:13
 * @version 1.0.0
 */
@Configuration
public class RabbitConfig {

	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	
	@Bean
	public ConnectionFactory connectionFactory(){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setVirtualHost("aic45dev");
		//这个设置是为了确认消息发到了队列上去
		connectionFactory.setPublisherConfirms(true);
		return connectionFactory;
	}
	
	/**
	 * 
	 * @Description (创建一个rabbit消息传递的模板类)
	 * @return
	 */
	@Bean(name="rabbitTemplate1")
	@Scope("prototype")
	public RabbitTemplate rabbitTemplate1(){
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setExchange("shi.test.exchange1");
		rabbitTemplate.setRoutingKey("bmp.param");
		return rabbitTemplate;
	}
	
	/**
	 * 交换机可以定义多个
	 * @Description (定义一个交换机)
	 * @return
	 */
	@Bean
	public TopicExchange topicExchange(){
		return new TopicExchange("shi.test.exchange1");
	}
	/**
	 * 
	 * @Description (注册一个队列,队列名为shi.queueA)
	 * @return
	 */
	@Bean
	public Queue queueA(){
		Queue queue = new Queue("shi.queueA", false);
		return queue;
	}
	/**
	 * 
	 * @Description (将队列,交换机和routing key绑定在一起)
	 * @return
	 */
	@Bean
	public Binding binding(){
		return BindingBuilder.bind(queueA()).to(topicExchange()).with("bmp.param");
	}	
	
	
	/**
	 * 
	 * @Description (说是自动创建队列用的,暂时先注释掉看一下,注释掉发现也没啥...)
	 * @return
	 */
	/*@Bean
	public RabbitAdmin rabbitadmin(){
		return new RabbitAdmin(connectionFactory());
	}*/
}

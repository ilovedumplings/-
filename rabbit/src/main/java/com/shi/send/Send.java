package com.shi.send;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.shi.entity.Messages;

@Component
public class Send {

	@Resource(name="rabbitTemplate1")
	private RabbitTemplate rabbitTemplate1;

	public void sendMessage(Messages message){
		rabbitTemplate1.convertAndSend(message);
	}
	
	/**
	 * 
	 * @Description (RPC实现)
	 * @param messages
	 */
	public void sendMessageReceive(Messages messages){
		Messages messages2 = (Messages) rabbitTemplate1.convertSendAndReceive(messages);
		System.out.println(messages2.getName());
	}
}

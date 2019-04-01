package com.shi.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.shi.entity.Messages;

@Component
@RabbitListener(queues="shi.queueA")
public class MsgReceive {
	
	@RabbitHandler()
	public Messages receive(Messages messages){
		System.out.println(messages.getName());
		messages.setName("HHH");
		return messages;
	}
	
}

package com;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shi.entity.Messages;
import com.shi.send.Send;
import com.shi.start.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})
public class ApplicationTest {

	@Autowired
	private Send send;
	
	
	@Test
	public void test() throws Exception{
		System.out.println("begin..test");
		Messages message = new Messages();
		message.setName("shiguofeng");
//		send.sendMessage(message);
		send.sendMessageReceive(message);
	}
}

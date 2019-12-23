package com.org.jms.hospitalmanagement.eligibilitycheck;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class EligibilityCheckerApp {

	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {

		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
		try(ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
				JMSContext context = connectionFactory.createContext("eligibilityUser", "eligibilityPass")){
			JMSConsumer consumer1 = context.createConsumer(requestQueue);
			JMSConsumer consumer2 = context.createConsumer(requestQueue);
			
			for (int i = 0; i < 5; i++) {
				System.out.println("1 : " + consumer1.receive());
				System.out.println("2 : " + consumer2.receive());
			}
//			consumer.setMessageListener(new EligibilityCheckListener());
			
//			Thread.sleep(10000);
		}
	}
}
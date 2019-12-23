package com.org.jms.hospitalmanagement.clinicals;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.org.jms.hospitalmanagement.model.Patient;

public class ClinicalsApp {

	public static void main(String[] args) throws NamingException, JMSException {

		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueue");
//		Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
		try(ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
				JMSContext context = connectionFactory.createContext("clinicalUser", "clinicalPass")){
			JMSProducer producer = context.createProducer();
			ObjectMessage objectMessage = context.createObjectMessage();
			Patient patient = new Patient();
			patient.setName("Mrinal");
			patient.setId(10);
			patient.setInsuranceProvider("Bajaj");
			patient.setCopay(300D);
			patient.setAmountToPay(500D);
			objectMessage.setObject(patient);
			
			System.out.println("Message about to send to reply queue");
			
			for (int i = 0; i < 10; i++) {
				producer.send(requestQueue, patient);
			}
			
//			JMSConsumer consumer = context.createConsumer(replyQueue);
//			MapMessage message = (MapMessage) consumer.receive(30000);
//			System.out.println("Response is eligibility : " + message.getBoolean("Eligible"));
		}
	}
}
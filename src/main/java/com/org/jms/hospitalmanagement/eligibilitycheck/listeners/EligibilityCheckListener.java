package com.org.jms.hospitalmanagement.eligibilitycheck.listeners;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import com.org.jms.hospitalmanagement.model.Patient;

public class EligibilityCheckListener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		ObjectMessage objectMessage = (ObjectMessage) message;
		try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
				JMSContext context = connectionFactory.createContext()) {
			InitialContext initialContext = new InitialContext();
			Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
			MapMessage mapMessage = context.createMapMessage();
			Patient patientObject = (Patient) objectMessage.getObject();
			
			System.out.println("Received patient");

			if (patientObject.getInsuranceProvider().equals("Bajaj")) {
				System.out.println("Patient's copay is : " + patientObject.getCopay());
				if (patientObject.getCopay() <= 40 && patientObject.getAmountToPay() < 1000) {
					mapMessage.setBoolean("Eligible", true);
				}
			} else {
				mapMessage.setBoolean("Eligible", false);
			}
			JMSProducer producer = context.createProducer();
			producer.send(replyQueue, mapMessage);
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
	}
}
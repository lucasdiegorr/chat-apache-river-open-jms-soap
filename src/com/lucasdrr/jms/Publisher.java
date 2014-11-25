/**
 * 
 */
package com.lucasdrr.jms;

import java.util.Hashtable;

import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * @author lucas
 *
 */
public class Publisher {

	public void publish(String messageText, String topic) {
		try{

			Hashtable properties = new Hashtable();
			properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.exolab.jms.jndi.InitialContextFactory");
			properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

			Context context = new InitialContext(properties);

			TopicConnectionFactory tfactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");

			TopicConnection tconnection = tfactory.createTopicConnection();
			TopicSession tsession = tconnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

			TextMessage message = tsession.createTextMessage();
			message.setText(messageText);
	      
			Topic dest = (Topic) context.lookup(topic);
			TopicPublisher publisher = tsession.createPublisher(dest);
			publisher.publish(message);
	        
			context.close();
			tconnection.close();

	        
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	}
	
}

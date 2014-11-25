/**
 * 
 */
package com.lucasdrr.jms;

import java.util.Hashtable;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * @author lucas
 *
 */
public class Subscriber implements MessageListener{

	public void subscriber(String topic){
	     
        try{

		Hashtable properties = new Hashtable();
		properties.put(Context.INITIAL_CONTEXT_FACTORY,"org.exolab.jms.jndi.InitialContextFactory");
		properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

		Context context = new InitialContext(properties);

		TopicConnectionFactory tfactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");

		TopicConnection tconnection = tfactory.createTopicConnection();
		TopicSession tsession = tconnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

		tconnection.start();

	        Topic dest = (Topic)context.lookup(topic);
       	TopicSubscriber tsubscriber = tsession.createSubscriber(dest);

		tsubscriber.setMessageListener(this);
        
                       	
			 
        }catch(Exception e){
            e.printStackTrace();
        }       
    }   
	@Override
    public void onMessage(Message message){
        if(message instanceof TextMessage){
            try{
                System.out.println( ((TextMessage)message).getText());
            }catch(Exception e){
            }
        }
    }
}

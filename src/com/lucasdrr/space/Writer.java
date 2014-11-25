package com.lucasdrr.space;

import java.rmi.RemoteException;
import java.util.Date;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

/**
 * @author Lucas Diego
 *
 * Data: Nov 13, 2014
 */
public class Writer {

	private ClientChat client;
	private JavaSpace space;

	public Writer(ClientChat clientChat) {
		this.client = clientChat;
	}
	
	public void init() {
		System.out.println("Procurando pelo servico JavaSpace...");
        Lookup finder = new Lookup(JavaSpace.class);
        space = (JavaSpace) finder.getService();
        if (space == null) {
                System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                System.exit(-1);
        } 
        System.out.println("O servico JavaSpace foi encontrado.");
	}
	
	public void sendMessage(String receiver, String textMessage) {
		
		Message messagePackage = new Message();
		messagePackage.setSender(this.client.getNickName());
		messagePackage.setReceiver("All");
		messagePackage.setMessage(textMessage);
		messagePackage.setLatitude(this.client.getLatitude());
		messagePackage.setLongitude(this.client.getLongitude());
		messagePackage.setDate(new Date());
		messagePackage.setRegister(0);
		
		try {
			this.space.write(messagePackage, null, Lease.FOREVER);
			System.out.print(messagePackage.getMessage());
		} catch (RemoteException | TransactionException e) {
			e.printStackTrace();
		}
	}

	public void register() {
		
		Message register = new Message();
		
		register.setSender(this.client.getNickName());
		register.setMessage("");
		register.setRegister(1);
		register.setDate(new Date());
		
		try {
			this.space.write(register, null, Lease.FOREVER);
		} catch (RemoteException | TransactionException e) {
			e.printStackTrace();
		}
	}

}
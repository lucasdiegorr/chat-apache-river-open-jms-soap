package com.lucasdrr.space;

import java.rmi.RemoteException;
import java.util.Date;

import net.jini.core.entry.UnusableEntryException;
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
		
		try {
			this.space.write(messagePackage, null, Lease.FOREVER);
			System.out.print(messagePackage.getMessage());
		} catch (RemoteException | TransactionException e) {
			e.printStackTrace();
		}
	}

	public void register() {
		
		RegisterClient register = new RegisterClient();
		
		register.setNickName(this.client.getNickName());
		register.setLatitude(this.client.getLatitude());
		register.setLongitude(this.client.getLongitude());
		try {
			this.space.write(register, null, Lease.FOREVER);
			System.out.println("Registrado.");
		} catch (RemoteException | TransactionException e) {
			e.printStackTrace();
		}
	}

	public void changeLocation(String text, String text2) {
		RegisterClient oldRegister = new RegisterClient();
		oldRegister.setNickName(this.client.getNickName());
		try {
			space.takeIfExists(oldRegister, null, Long.MAX_VALUE);
		} catch (RemoteException | UnusableEntryException
				| TransactionException | InterruptedException e) {
			e.printStackTrace();
		}
		
		RegisterClient newRegister = new RegisterClient();
		newRegister.setNickName(this.client.getNickName());
		newRegister.setLatitude(Float.parseFloat(text));
		newRegister.setLongitude(Float.parseFloat(text2));
		
		try {
			space.write(newRegister, null, Lease.FOREVER);
		} catch (RemoteException | TransactionException e) {
			e.printStackTrace();
		}
		
	}

}
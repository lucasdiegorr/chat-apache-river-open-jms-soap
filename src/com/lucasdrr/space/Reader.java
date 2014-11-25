package com.lucasdrr.space;

import java.rmi.RemoteException;
import java.util.TreeMap;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

/**
 * @author Lucas Diego
 *
 * Data: Nov 13, 2014
 */
public class Reader implements Runnable{

	private ClientChat client;
	private JavaSpace space;
	private TreeMap<String, String> list;

	public Reader(ClientChat client) {
		this.client = client;
		this.list = new TreeMap<String, String>();
	}

	public void init() {
		System.out.println(" Reader - Procurando pelo servico JavaSpace...");
		space = null;
		try {
			Lookup finder = new Lookup(JavaSpace.class);
			space = (JavaSpace) finder.getService();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (space == null) {
			System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
			System.exit(-1);
		}
		new Thread(new ScheduledTask()).start();
		System.out.println("O servico JavaSpace foi encontrado.");
	}

	@Override
	public void run() {
		Message lastMessage = new Message();
		while (true) {
			Message template = new Message();
			template.setReceiver("All");
			template.setRegister(0);
			Message msg = null;
			try {
				msg = (Message) space.readIfExists(template, null, 50);
				if (msg != null) {
					lastMessage = msg;
					if (!msg.getSender().equals(this.client.getNickName())) {
						System.out.println("O usuario " + this.client.getNickName() + " recebeu a msg  de " +msg.getSender() + " foi " + msg.getMessage());
						this.client.showMessage(msg);
						space.take(msg, null, Long.MAX_VALUE);
					}
				}
			} catch (Exception e) {
				System.out.println("Erro aqui" + e);
				e.printStackTrace();
			}
		}
	}

	public TreeMap<String, String> getListContacts() {
		return list;
	}

	private boolean distanceBetween(Float latitude, Float longitude,
			Float latitude2, Float longitude2) {
		int R = 6378000;

		System.out.println("Distancia entre "+ latitude + "," + longitude + " e " +latitude2+","+longitude2 + " é ");

		double x1 = (double) (R*Math.cos(latitude.doubleValue())*Math.cos(longitude.doubleValue()));
		double y1 =(double) (R*Math.cos(latitude.doubleValue())*Math.sin(longitude.doubleValue()));
		double z1 = (double) (R*Math.sin(latitude.doubleValue()));

		double x2 = (double) (R*Math.cos(latitude2.doubleValue())*Math.cos(longitude2.doubleValue()));
		double y2 =(double) (R*Math.cos(latitude2.doubleValue())*Math.sin(longitude2.doubleValue()));
		double z2 = (double) (R*Math.sin(latitude2.doubleValue()));

		double distance = (double) (Math.sqrt((Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2)+Math.pow(z2-z1, 2)))); 
		if (distance <= 200) {
			System.out.println("Distancia entre "+ latitude + "," + longitude + " e " +latitude2+","+longitude2 + " é " + distance);
			return true;
		}
		return false;
	}

	private class ScheduledTask implements Runnable{

		@Override
		public void run() {
			while (true) {
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				Message register = new Message();
				register.setRegister(1);
				Message user = null;
				register.latitude = new Float(0);
				register.longitude = new Float(0);
				do {
					try {
						user = (Message) space.read(register, null, 50);
					} catch (RemoteException | UnusableEntryException
							| TransactionException | InterruptedException e) {
						e.printStackTrace();
					}
					if (user != null) {
						if (!list.containsKey(user.sender)) {
							System.out.println("Foi inserido o usuario "
									+ user.sender);
							list.put(user.sender, "online");
						}
					}
				} while (user != null);
			}
		}
		
		
	}
	
}
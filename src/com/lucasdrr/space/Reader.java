package com.lucasdrr.space;

import java.rmi.RemoteException;
import java.util.ArrayList;
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
	private ArrayList<String> list;

	public Reader(ClientChat client) {
		this.client = client;
		this.list = new ArrayList<String>();
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
		while (true) {
			Message template = new Message();
			template.setReceiver("All");
			Message msg = null;
			try {
				msg = (Message) space.read(template, null, Long.MAX_VALUE);
				if (msg != null) {
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

	//Distancia Euclidiana de latitude e longitude
	//Fonte:http://wiki.sj.ifsc.edu.br/wiki/index.php/Fundamenta%C3%A7%C3%A3o_Te%C3%B3rica_do_Projeto_-_O_Sistema_GPS_e_m%C3%A9todos_para_c%C3%A1lculo_de_%C3%A1rea_e_dist%C3%A2ncia
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
				
				ArrayList<RegisterClient> listRegister = new ArrayList<RegisterClient>();
				list = new ArrayList<String>();
				try {
					Thread.sleep(120*1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				client.getWindow().getTextAreaVision().setText(null);
				RegisterClient template = new RegisterClient();
				RegisterClient otherUser = null;
				do {
					try {
						otherUser = (RegisterClient) space.takeIfExists(template, null,
								100);
						if (otherUser != null) {
							if (!list.contains(otherUser.getNickName()) && distanceBetween(client.getLatitude(), client.getLongitude(), otherUser.getLatitude(), otherUser.getLongitude())) {
								list.add(otherUser.getNickName());
								listRegister.add(otherUser);
								client.getWindow().getTextAreaVision().append(otherUser.getNickName()+"\n");
								System.out.println("O usuario " + otherUser.getNickName()
										+ " foi inserido na lista.");
							}
						}
					} catch (Exception e) {
						System.out.println("Erro aqui" + e);
						e.printStackTrace();
					}
				} while (otherUser != null);
				System.out.println("Fim da Lista com " + listRegister.size() + " elementos");
				
				for (int index = 0; index < listRegister.size(); index++ ) {
					try {
						space.write(listRegister.get(index), null, Lease.FOREVER);
						System.out.println("Foi reintroduzido: " + listRegister.get(index).getNickName());
					} catch (RemoteException e) {
						e.printStackTrace();
					} catch (TransactionException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		
	}

	public void disconnect() {
		Message template = new Message();
		template.setSender(this.client.getNickName());
		template.setReceiver(null);
			try {
				space.takeIfExists(template, null,
						Long.MAX_VALUE);
				System.out.println("Usuario removido do espaco");
			} catch (Exception e) {
				System.out.println("Erro aqui" + e);
				e.printStackTrace();
			}
	}
	
}
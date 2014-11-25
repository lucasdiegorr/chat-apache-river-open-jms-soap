package com.lucasdrr.space;

import java.util.TreeMap;

import javax.swing.JFrame;

/**
 * @author Lucas Diego
 *
 * Data: Nov 13, 2014
 */
public class ClientChat {

	private String nickName;
	private Float latitude;
	private Float longitude;
	private Reader reader;
	private Writer writer;
	private Chat window;
	private TreeMap<String, Integer> listContacts;
	
	public ClientChat(Chat window) {
		this.latitude = new Float(0);
		this.longitude = new Float(0);
		this.window = window;
		this.listContacts = new TreeMap<String, Integer>();
	}

	public void initServices() {
		this.reader = new Reader(this);
		this.reader.init();
		this.writer = new Writer(this);
		this.writer.init();
		this.writer.register();
		new Thread(reader).start();
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the latitude
	 */
	public Float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public void sendMessage(String receiver, String textMessage) {
		this.writer.sendMessage(receiver, textMessage);
	}
	
	public void showMessage(Message msg) {
		this.window.getTextAreaChat().append(msg.getDate().getHours() + ":"+ msg.getDate().getMinutes() +":" + msg.getDate().getSeconds() +" - " + msg.getSender() + ": " + msg.getMessage());
		this.window.getTextAreaChat().setCaretPosition(this.window.getTextAreaChat().getDocument().getLength());
	}

	public TreeMap<String, String> getListContacts() {
		return this.reader.getListContacts();
	}
	
}
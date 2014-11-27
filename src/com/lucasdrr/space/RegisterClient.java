/**
 * 
 */
package com.lucasdrr.space;

import net.jini.core.entry.Entry;

/**
 * @author lucas
 *
 */
public class RegisterClient implements Entry{

	public String nickName;
	public Float latitude;
	public Float longitude;
	
	public RegisterClient() {
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	
}

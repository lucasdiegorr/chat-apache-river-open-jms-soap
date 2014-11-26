/**
 * 
 */
package com.lucasdrr.soap;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author lucas
 *
 */
@WebService
public interface ListChatInterface {

	@WebMethod public ArrayList<String> getUsers();
	@WebMethod public ArrayList<String> getPosts(String user);
}

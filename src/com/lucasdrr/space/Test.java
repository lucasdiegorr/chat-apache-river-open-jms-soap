/**
 * 
 */
package com.lucasdrr.space;

import java.util.ArrayList;

import net.jini.space.JavaSpace;

/**
 * @author lucas
 *
 */
public class Test {

	private static JavaSpace space;

	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(" Test - Procurando pelo servico JavaSpace...");
		space = null;
		ArrayList<String> list = new ArrayList<String>();
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
		System.out.println("O servico JavaSpace foi encontrado.");
		
		RegisterClient template = new RegisterClient();
		RegisterClient msg = null;
		do {
			try {
				msg = (RegisterClient) space.takeIfExists(template, null,
						Long.MAX_VALUE);
				
			} catch (Exception e) {
				System.out.println("Erro aqui" + e);
				e.printStackTrace();
			}
		} while (msg != null);
		System.out.println("Fim da Lista com: " + list.size());
	}

}

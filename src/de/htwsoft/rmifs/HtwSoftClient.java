package de.htwsoft.rmifs;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class HtwSoftClient {
	
	private final static String VERWALTER_IP	= "192.168.0.19";
	private final static int 	VERWALTER_PORT	= 4712;

	public static void main (String [] args) {
		System.setProperty("java.security.policy", "java.policy");
		System.setProperty("java.rmi.server.hostname", VERWALTER_IP);
		start();
	}

	private static void start() {
		
		try {
			FileSystemClient client = new FileSystemClient(VERWALTER_PORT, VERWALTER_IP);
			new ClientTerminalController(client);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} 		
	}
}
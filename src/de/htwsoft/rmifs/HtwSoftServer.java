package de.htwsoft.rmifs;

import java.rmi.RemoteException;

public class HtwSoftServer {

	private final static String SERVER_IP 	= "192.168.0.19";
	private final static int 	SERVER_PORT = 4711;

	public static void main(String args[]) {
		start();
	}

	private static void start() {
		try {
			
			System.setProperty("java.security.policy", "java.policy");
			FileSystemServer server = new FileSystemServer(SERVER_IP, SERVER_PORT);
			server.logToTerminal("Hostname is:\t" +server.getHostName() + "\n");
			server.logToTerminal("System runs on: " + server.getOSName() + "\n");
		} catch (RemoteException rme) {
			rme.printStackTrace();
		}
	}

}

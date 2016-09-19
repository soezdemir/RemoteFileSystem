package de.htwsoft.rmifs;

/**
 * VerwalterServer ist gleichzeitig Client und Server.
 * Zwischenstelle zwischen Client und FileServer.
 * @author cpatzek, soezdemir
 * @version 1.03
 * @date 2016-09-14
 */

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class VerwalterServer implements VerwalterInterface, RMIClientSocketFactory {
	
	private static final String VERWALTER_LISTE = "Name: BspServer[1] IP: 192.168.0.101\n"
			+ "Name: BspServer[2] IP: 192.168.0.102\n" + "Name: BspServer[3] IP: 192.168.0.103";

	private final static String VERWALTER_IP = "192.168.0.19";
	private final static String HOST = "localhost"; // 192.168.0.101
	private final static int PORT_NR = 4711;

	private FSInterface fsserver;

	/**
	 * Konstruktor, baut Verbindung zum lokalen FileServer auf
	 * 
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public VerwalterServer() throws RemoteException, NotBoundException {

		System.setProperty("java.security.policy", "java.policy");
		System.setProperty("java.rmi.server.hostname", "localhost");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		Registry registry = LocateRegistry.getRegistry(HOST, PORT_NR);
		this.fsserver = (FSInterface) registry.lookup("FileSystemServer");
	}

	/**
	 * Erstellt einen Socket für remote Verbindungen(Funktion des Interface
	 * RMIClientSocketFactory)
	 * 
	 * @param host
	 *            Adresse des Clients
	 * @param port
	 *            Port der Verbindung
	 * @return Den Socket für den Client
	 * @throws IOException
	 */
	public Socket createSocket(String host, int port) throws IOException {
		return new Socket(host, port);
	}

	/**
	 *
	 * @return Name und IP-Adressen aller VerwalterServer
	 */
	public String getServerList() {
		System.out.println("\t[Request]$ serverlist");
		return VERWALTER_LISTE;
	}

	public boolean rename(String oldName, String newName) throws RemoteException {
		System.out.println("\t[Request]$ rename");
		return this.fsserver.rename(oldName, newName);
	}

	public String getOSName() throws RemoteException {
		System.out.println("\t[Request]$ serverOSname");
		return this.fsserver.getOSName();
	}

	public String getHostName() throws RemoteException {
		System.out.println("\t[Request]$ hostname ");
		return this.fsserver.getHostName();
	}

	public String getHostAddress() throws RemoteException {
		System.out.println("\t[Request]$ hostaddress");
		return this.fsserver.getHostAddress();
	}

	public void sendClientAddress(String clientAddress) throws RemoteException {
		System.out.println("\n[" + clientAddress + "]$ connected...");
		fsserver.sendClientAddress(clientAddress);
	}

	public boolean delete(String file) throws RemoteException {
		System.out.println("\t[Request]$ delete");
		return this.fsserver.delete(file);
	}

	public boolean createFile(String file) throws RemoteException {
		System.out.println("\t[Request]$ createFile");
		return this.fsserver.createFile(file);
	}

	public boolean createDir(String dir) throws RemoteException {
		System.out.println("\t[Request]$ createDir");
		return this.fsserver.createDir(dir);
	}

	/**
	 * Prüft ob eine Datei gefunden wurde und macht entsprechende Rückgaben
	 * 
	 * @param file
	 *            Name der Datei
	 * @param startDir
	 *            Name des StartDirectories
	 * @return Entweder die Angabe, dass keine Datei gefunden wurde, oder die
	 *         Dateien die gefunden wurden und weitere Rueckgabe von Server
	 *         Liste
	 * @throws RemoteException
	 */
	public String search(String file, String startDir) throws RemoteException {
		System.out.println("\t[Request]$ search");
		return this.fsserver.search(file, startDir);
	}

	public String browseFiles(String dir) throws RemoteException {
		System.out.println("\t[Request]$ browseFiles");
		return this.fsserver.browseFiles(dir);
	}

	public String browseDirs(String dir) throws RemoteException {
		System.out.println("\t[Request]$ browseDirs");
		return this.fsserver.browseDirs(dir);
	}

	/**
	 * Legt Port für Verbindung fest und baut Verbindung zum FileServer
	 * auf(siehe Konstruktor)
	 * 
	 * @param args
	 *            IP und Port des VerwalterServers(Konstanten stattdessen
	 *            verwenden?)
	 */
	public static void main(String args[]) {
		try {
			VerwalterServer verwalterServer = new VerwalterServer();
			if (args.length >= 1) {
				int serverPort = 0;// Clientaufruf mit 4711
				serverPort = Integer.parseInt(args[0]);
				// Noetig für RMI Client Anbindung zum VerwalterServer z.B.
				// 192.168.0.101 Port 4711

				// Stellt das Objekt dem System zur Verfügung
				VerwalterInterface stub = (VerwalterInterface) UnicastRemoteObject.exportObject(verwalterServer,
						serverPort);
				// Registry erstellen um Objekt ansprechen zu können
				Registry registry = LocateRegistry.createRegistry(serverPort);
				// Objekt an registry binden
				registry.rebind("VerwalterServer", stub);
				System.out.println("Server bound ...\n Port open at " + serverPort);
			} else {
				System.out.println("Bitte Server-Port zum binden angeben!");
			}
		} catch (RemoteException rme) {
			rme.printStackTrace();
		} catch (NotBoundException nbe) {
			nbe.printStackTrace();
		}
	}
}
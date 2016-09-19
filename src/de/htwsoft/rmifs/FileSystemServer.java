package de.htwsoft.rmifs;

/**
* RMI-Server für ein FileSystem
* @author Marco Palumbo
* @version 1.01
*/

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import static java.nio.file.StandardCopyOption.*;
import java.util.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.rmi.AccessException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * RMI-Server für das FileSystem
 */
public class FileSystemServer implements FSInterface {
	
	private FileSystem fs = new FileSystem();
	private FSInterface verwalterInterface;
	private String clientIP;

	public FileSystemServer(String serverIP, int serverPort) throws RemoteException {
		initServer(serverIP);
		bindServer(serverPort);	
		logToTerminal("Port now open at " + serverPort + "\n", true);
	}
	
	private void initServer(String serverIP) {
		System.setProperty("java.security.policy", "java.policy");
		System.setProperty("java.rmi.server.hostname", serverIP);
		
		if (System.getSecurityManager() == null) 
			System.setSecurityManager(new SecurityManager());
		logToTerminal("Server bound to " + serverIP,true);
	}
	
	private void bindServer(int serverPort) throws AccessException, RemoteException {
		FSInterface stub = (FSInterface) UnicastRemoteObject.exportObject(this, serverPort);
		Registry registry = LocateRegistry.createRegistry(serverPort);
		registry.rebind("FileSystemServer", stub);
		
	}
	

	/**
	 * Funktion sucht alle Ordner eines angegebenen Directory
	 * 
	 * @param dir
	 *            Ordner der durchsucht werden soll
	 * @return einen String mit allen gefunden Ordner durch ";" getrennt
	 */
	public String browseDirs(String dir) throws RemoteException {
		Path[] dirListe = null;
		StringBuffer ergListe = new StringBuffer();
		logToTerminal("Funktion: browseDirs - Param: " + dir, true);
		try {
			fs.browse(dir);
			dirListe = this.fs.getDirListe();
			for (int i = 0; i < dirListe.length; i++) {
				if (dirListe[i] != null) 
					ergListe.append("\n" + dirListe[i]);	
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return ergListe.toString();
	}

	/**
	 * Funktion sucht alle Dateien eines angegebenen Directory
	 * 
	 * @param file
	 *            Ordner der durchsucht werden soll
	 * @return einen String mit allen gefunden Dateien durch ";" getrennt
	 */
	public String browseFiles(String file) throws RemoteException {
		Path[] fileListe = null;
		StringBuffer ergListe = new StringBuffer();
		logToTerminal("Funktion: browseFiles - Param: " + file, true);
		try {
			this.fs.browse(file);
			fileListe = this.fs.getFileListe();
			for (int i = 0; i < fileListe.length; i++) {
				if (fileListe[i] != null) 
					ergListe.append("\n" + fileListe[i]);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return ergListe.toString();
	}

	/**
	 * Funktion sucht nach der übergebenen Datei ab dem angegebenen Ordner
	 * 
	 * @param file
	 *            Datei nach der gesucht werden soll
	 * @param startDir
	 *            Ordner ab dem die Datei gesucht werden soll
	 * @return Liste mit Dateien die auf den Such-String passen mit ";" getrennt
	 */
	public String search(String file, String startDir) throws RemoteException {
		logToTerminal("Funktion: search - Params: " + file + ", " + startDir, true);

		Path[] fileListe = null;
		StringBuffer erg = new StringBuffer();

		try {
			if (fs.search(file, startDir)) {
				fileListe = this.fs.getFileListe();
				for (int i = 0; i < fileListe.length; i++) {
					if (fileListe[i] != null) {
						erg.append("\n\t" + fileListe[i]);
					}
				}
				return erg.toString();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return erg.toString();
	}

	/**
	 * Funktion erstellt eine Datei
	 * 
	 * @param file
	 *            Datei die erstellt werden soll
	 * @return True wenn die Datei erstellt wurde
	 */
	public boolean createFile(String file) throws RemoteException {
		boolean fileCreated = false;
		logToTerminal("Funktion: createFile - Param: " + file, true);
		try {
			fileCreated = fs.create(file, "file");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		logToTerminal("Return: " + fileCreated);
		return fileCreated;
	}

	/**
	 * Funktion erstellt einen Ordner
	 * 
	 * @param dir
	 *            Ordner der erstellt werden soll
	 * @return True wenn der Ordner erstellt wurde
	 */
	public boolean createDir(String dir) throws RemoteException {
		boolean dirCreated = false;
		logToTerminal("Funktion: createDir - Param: " + dir, true);
		try {
			dirCreated = fs.create(dir, "dir");
		} catch (IOException e) {
			e.printStackTrace();
		}
		logToTerminal("Return: " + dirCreated);
		return dirCreated;		
	}

	/**
	 * Funktion löscht einen Ordner oder eine Datei
	 * 
	 * @param file
	 *            Ordner/Datei der/die gelöscht werden soll
	 * @return True wenn der Ordner/die Datei geloescht wurde
	 */
	public boolean delete(String file) throws RemoteException {
		logToTerminal("Funktion: delete - Param: " + file, true);
		return this.fs.delete(file);
	}

	/**
	 * Funktion benennt einen Ordner oder eine Datei um
	 * 
	 * @param oldName
	 *            aktueller Name der Datei oder des Ordners
	 * @param newName
	 *            neuer Name der Datei oder des Ordners
	 * @return True wenn der Ordner/die Datei umbenannt werden konnte
	 */
	public boolean rename(String oldName, String newName) throws RemoteException {
		logToTerminal("Funktion: rename - Params: " + oldName + ", " + newName, true);
		boolean foundFile = false;
		try {
			foundFile = this.fs.rename(oldName, newName);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return foundFile;
	}

	/**
	 * Funktion liefert den Namen des OS zurück
	 * 
	 * @return Name des OS-Systems des FileSystems
	 */
	public String getOSName() throws RemoteException {
		logToTerminal("Funktion: getOSName", true);	
		return fs.getOSName();
	}

	/**
	 * Funktion liefert den Namen, IP & OS eines Hosts zurück
	 * 
	 * @return Host Name des FileSystems
	 * @throws RemoteException
	 * @author soezdemir
	 */
	public String getHostName() throws RemoteException {
		logToTerminal("Funktion: getHostName", true);
		return fs.getHostName();
	}

	public String getHostAddress() throws RemoteException {
		logToTerminal("Funktion: getHostAddress", true);
		return fs.getHostAddress();
	}

	public void sendClientAddress(String clientAddress) throws RemoteException {
		this.clientIP = clientAddress;
	}
	
	public String getClientIP() {
		return this.clientIP;
	}

	/**
	 * Funktion liefert die Dateien der letzten suche (browse)
	 * 
	 * @return Dateien des Letzten Browse befehls
	 */
	public String getFileListe() throws RemoteException {
		System.out.println("Funktion: getFileListe");
		
		StringBuffer ergListe 	= new StringBuffer();
		Path [] fileListe 		= fs.getFileListe();
		
		for (int i = 0; i < fileListe.length; i++)
			ergListe.append("\n" + fileListe[i]);

		return ergListe.toString();
	}

	/**
	 * Funktion liefert die Ordner der letzten suche (browse)
	 * 
	 * @return Ordner des Letzten Browse befehls
	 */
	public String getDirListe() throws RemoteException {
		System.out.println("Funktion: getDirListe");
		
		StringBuffer ergListe 	= new StringBuffer();
		Path [] fileListe 		= fs.getDirListe();
		
		for (int i = 0; i < fileListe.length; i++)
			ergListe.append("\n" + fileListe[i]);

		return ergListe.toString();
	}
	
	public void logToTerminal(String message) {
		logToTerminal(message, false);
	}
	
	public void logToTerminal(String message, boolean timeStamp) {
		StringBuffer logger = new StringBuffer();
		String clientIP = getClientIP();
		
		if (clientIP == null)
			clientIP = "SERVER";
		
		if(timeStamp)
			logger.append("[" + new Date()+ "] [" + clientIP + "]$ " + message );
		else
			logger.append(message);
		
		System.out.println(logger.toString());
		
		
	}
	
}
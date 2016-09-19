package de.htwsoft.rmifs;

/**
 * @author mpalumbo, cpatzek, soezdemir
 * @version 1.03
 * @date indefinitely
 */

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class FileSystemClient implements Remote
{
	private VerwalterInterface vserver;  //Attribute zum Zugriff auf Verwalter Server Funktionen
	
	private String clientOS 		= "not set!";
	private String clientAddress 	= "not set!";
	private String clientName 		= "not set!";
	
	/**
	* Konstruktor 
	* erzeugt eine FileSystem-Klasse
	 * host und portNr lösen den Verwalter-Server des lokalen Systems auf
	*/
	public FileSystemClient(int portNr, String host) throws RemoteException, NotBoundException
	{
		System.setProperty("java.security.policy", "java.policy");
		connect(host, portNr);
	}
	
	private void connect(String host, int portNr) throws RemoteException, NotBoundException {
		if (System.getSecurityManager() == null) 
		{
			System.setSecurityManager(new SecurityManager());
		}
		Registry registry = LocateRegistry.getRegistry(host, portNr);
		this.vserver = (VerwalterInterface) registry.lookup("VerwalterServer");
		sendClientAddress();
	}

	/**
	 * Fragt die verfuegbaren VerwalterServer ab, also deren Name und IP
	 * @throws RemoteException 
	*/
	public String getSeverlist() throws RemoteException
	{
		return vserver.getServerList();
	}

	/**
	* Führt die Browse-Methode der FileSystemServer-Klasse aus
	 * @throws RemoteException 
	*/
	public String browseDirs(String name) throws RemoteException
	{
		return this.vserver.browseDirs(name);
 	}
	
	public String browseFiles(String name) throws RemoteException
	{
		return this.vserver.browseFiles(name);
	}
	
	public String search(String pfad, String startDir) throws RemoteException
	{
		return this.vserver.search(pfad, startDir);
	}

	public boolean createDir(String name) throws RemoteException
	{
		return this.vserver.createDir(name);				
	}
	
	public boolean createFile(String name) throws RemoteException
	{
		return this.vserver.createFile(name);
	}
	
	public boolean delete(String name) throws RemoteException
	{
		return this.vserver.delete(name);
	}
	
	public boolean rename(String oldName, String newName) throws RemoteException
	{
		return this.vserver.rename(oldName, newName); 
	}	
	
	public String getOSName() throws RemoteException
	{	
		return this.vserver.getOSName();		
	}	

	public String getClientAddress(){
		return NetworkController.getHostAddress();
	}
		
	public String getClientName() {
		return NetworkController.getHostName();
	}

	public String getClientOS (){
		return NetworkController.getClientOS();
	}
	
	public void sendClientAddress() throws RemoteException {
		vserver.sendClientAddress(getClientAddress());
	}
	
	//TODO
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("*clientaddress: " + clientAddress);
		return " ";  //sb.toString();
	}
}
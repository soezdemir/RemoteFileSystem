package de.htwsoft.rmifs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

public class ClientTerminalController {
	
	private boolean isRunning = true;
	
	private FileSystemClient client;
	private ClientTerminalGUI gui;

	public ClientTerminalController(FileSystemClient client) {
		initController(client);
	}

	private void initController(FileSystemClient client) {
		this.client = client;
		gui = new ClientTerminalGUI();
		mainLoop();
	}
	
	private void mainLoop() {
		while(isRunning) {
			gui.showMenu();
			int input = readIntInput();
			switch(input) {
				case 0: 
					isRunning = false;
					gui.printInformation("---PROGRAMM-ENDE---");
					break;
				case 1:
					listServer();
					break;
				case 2:
					browse();
					break;
				case 3:
					search();
					break;
				case 4:
					createDir();
					break;
				case 5:
					createFile();
					break;
				case 6:
					delete();
					break;
				case 7:
					rename();
					break;
				case 8:
					clientData();
					break;
				default:
					gui.printInformation("Fehlerhafte Eingabe --> " + input);
			}
		}
	}
	
	private void listServer() {
		try {
			client.sendClientAddress();
			gui.printInformation("Server:\n" + client.getSeverlist(),1);
		} catch (RemoteException ree) {
			ree.printStackTrace();
		}
	}
	
	private void browse() {
		try {
			client.sendClientAddress();
		
			gui.printInformation("Welcher Ornder soll untersucht werden?");
			String name = readStringInput();
			
			gui.printInformation("Ordner:\n" + client.browseDirs(name) 
								+"\nFiles: \n" + client.browseFiles(name),1);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void delete() {
		try {
			client.sendClientAddress();
			
			gui.printInformation("Welcher Ordner|File soll geloescht werden!");
			String name = readStringInput();
			
			if(!client.delete(name))
				gui.printInformation("Ordner|File:\n" + name + " konnte NICHT geloescht werden!" ,1);
			else
				gui.printInformation("Ordner|File:\n" + name + " wurde geloescht!",1);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void createFile() {
		try {
			client.sendClientAddress();
			
			gui.printInformation("Welche Datei soll erstellt werden?");
			String name = readStringInput();

			if(!client.createFile(name))
				gui.printInformation("Datei:\n" + name + " konnte NICHT ertellt werden!",1);
			else
				gui.printInformation("Datei:\n" + name + " wurde erstellt!");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void createDir() {
		try {
			client.sendClientAddress();
			
			gui.printInformation("Welcher Ordner soll erstellt werden?");
			String dir = readStringInput();

			if(!client.createDir(dir))
				gui.printInformation("Ordner:\n" + dir + " konnte NICHT erstellt werden!",1);
			else
				gui.printInformation("Ordner:\n" + dir + " wurde erstellt!",1);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
		
	private void search() {
		try {
			client.sendClientAddress();

			gui.printInformation("Was soll gesucht werden?");
			String target = readStringInput();

			gui.printInformation("Wo soll gesucht werden?");
			String path = readStringInput();
					
			String result = client.search(target, path);
			if (result.length() == 0)
				gui.printInformation("File:\n" + target 
						+ " konnte nicht in " + path 
						+ " gefunden werden",1);
			else
				gui.printInformation("Ordner: " + path + " \nFile(s): " + result,1);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void rename() {
		try {
			client.sendClientAddress();
		
			gui.printInformation("Welcher Ordner soll umbenannt werden?");
			String oldName = readStringInput();
			
			gui.printInformation("Welcher Zielname?");
			String newName = readStringInput();

			if(!client.rename(oldName, newName))
				gui.printInformation("Ordner|Datei:\n" + oldName 
									+ " konnte NICHT umbenannt werden!",1);
			else	
				gui.printInformation("Ordner|Datei:\n" + oldName
									+ " wurde in " + newName + " umbenannt",1);
		} catch (RemoteException e) {
			e.printStackTrace();
		}	
	}
	
	private void clientData() {
		try {
			client.sendClientAddress();
			gui.printInformation("OS:\n\t" + client.getOSName() 
								+"\nIP:\n\t" + client.getClientAddress()
								+"\nClient Name:\n\t" + client.getClientName(),1);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private int readIntInput(){
		boolean isCorrect = false;
		int input = 0;
		
		try {
			while(!isCorrect){
				gui.printInformation("\nInput --> ");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String line;
					line = br.readLine();
				
				if (line.length()> 0) {
					input = Integer.parseInt(line);
					isCorrect = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}
	
	private String readStringInput(){
		boolean isCorrect = false;
		String input = "";
		
		try {
			while(!isCorrect){
				gui.printInformation("\nInput --> ");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String line;
					line = br.readLine();
				
				if (line.length()> 0) {
					input = line;
					isCorrect = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}
}
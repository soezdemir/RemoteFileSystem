package de.htwsoft.rmifs;

import java.lang.StringBuffer;

public class ClientTerminalGUI {
	
	private final static String MENU_HEAD	= "\n+-------------- MENU --------------+";
	private final static String MENU_FOOT 	= "\n+----------------------------------+";
	private final static String RESULT_HEAD	= "\n+------------- RESULT -------------+";
	private final static String RESULT_FOOT = "\n+----------------------------------+";
	
	private final static int 	MSG_FLAG_DEFAULT = 0;
	
	private StringBuffer menu;
	private StringBuffer result;
	
	public ClientTerminalGUI() {
		createMenu();
	}

	private void createMenu() {
		menu = new StringBuffer(MENU_HEAD);
		menu.append("\n\t0 --> exit");
		menu.append("\n\t1 --> list server");
		menu.append("\n\t2 --> browse");
		menu.append("\n\t3 --> search");
		menu.append("\n\t4 --> create dir");
		menu.append("\n\t5 --> create file");
		menu.append("\n\t6 --> delete file|dir");
		menu.append("\n\t7 --> rename file|dir");
		menu.append("\n\t8 --> client data");
		menu.append(MENU_FOOT);
	}

	public void showMenu() {
		printInformation(menu.toString());
	}
	
	public void printInformation(String message) {
		printInformation(message, MSG_FLAG_DEFAULT);
	}
	
	public void printInformation(String message, int flag) {
		result = new StringBuffer();		
		if (flag == MSG_FLAG_DEFAULT)
			result.append(message);
		else{
			result.append(RESULT_HEAD);
			result.append("\n" + message);
			result.append(RESULT_FOOT);
		}
		System.out.println(result.toString());
	}
}
package de.htwsoft.rmifs;

import java.net.*;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by soezdemir on 14.09.16.
 */
public class NetworkController {

	private final static String VALUE_NOT_SET = "not set yet!";
	
    public static String getHostAddress() {
		try {
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		        for (NetworkInterface netint : Collections.list(nets))
						if( netint.isUp() && !netint.isLoopback()) {
							Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
							for (InetAddress inetAddress : Collections.list(inetAddresses))
								if(inetAddress.toString().length() <= 15 && inetAddress.toString().length() >= 7)
									return inetAddress.getHostAddress();
						}
			} catch (SocketException e) {
				e.printStackTrace();
			}
        return VALUE_NOT_SET;
    }

    public static String getHostName() {
    	try {
    		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
    		for (NetworkInterface netint : Collections.list(nets))
    			if( netint.isUp() && !netint.isLoopback()) {
    				Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
    				for (InetAddress inetAddress : Collections.list(inetAddresses))
    					if(inetAddress.toString().length() <= 15 && inetAddress.toString().length() >= 7)
    						return InetAddress.getLocalHost().getHostName();
    			}
    	} catch (SocketException e) {
    		e.printStackTrace();
    	} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	return VALUE_NOT_SET;
    }

    public static String getClientOS()
    {
    	return System.getProperty("os.name") +
                    ", Version " + System.getProperty("os.version") +
                    " on " + System.getProperty("os.arch") + " architecture.";
    }

}

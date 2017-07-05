package org.mindroid.server.app;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * The main application of the Mindroid server
 *
 * @author Roland Kluge - Initial implementation
 */
public class MindroidServerApplicationMain {

	private static MindroidServerFrame mindroidServerFrame;
	private final static int port = 33044;
	private static ServerSocket server = null;

	public static void main(String[] args) {

		System.setProperty("apple.laf.useScreenMenuBar","true");
		mindroidServerFrame = new MindroidServerFrame();
        try {
			runServer();
		} catch (Exception e) {
			MindroidServerConsole console = MindroidServerConsole.getMindroidServerConsole();
			console.setVisible(true);
			console.appendLine("Unknown Exception. Please restart the Application. Exception Message: "+ e.getMessage());
			console.appendLine(e.toString());
			mindroidServerFrame.addContentLine("Local","ERROR","Server not running.");
			mindroidServerFrame.addContentLine("Local","INFO","See Error console.");
			mindroidServerFrame.disableRefresh(true);
		}

	}

	public static void runServer() {
		try{
			server = new ServerSocket(port);
			invokeDisplayIPAdress();
			mindroidServerFrame.addContentLine("Local","INFO","Server started");
		} catch (IOException e) {
			MindroidServerConsole console = MindroidServerConsole.getMindroidServerConsole();
			console.setVisible(true);
			console.appendLine("The Server could not be started. Try restarting the Application.");
			console.appendLine(e.toString() + "\n");
			mindroidServerFrame.addContentLine("Local","ERROR","Server not running.");
			mindroidServerFrame.addContentLine("Local","INFO","See Error console.");
			mindroidServerFrame.disableRefresh(true);
			return;
		}

		Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread th, Throwable ex) {
				System.out.println("ex handler");
				MindroidServerConsole console = MindroidServerConsole.getMindroidServerConsole();
				console.setVisible(true);
				console.appendLine("Messages from one connection might have been lost. No need to restart the application.");
				console.appendLine(ex.toString());
			}
		};

		int iteration = 0;
		while(true){
			++iteration;
			System.out.println("[C# " + iteration + "] Waiting...");
			MindroidServerWorker w;
			try{
				w = new MindroidServerWorker(server.accept(), mindroidServerFrame);
				Thread t = new Thread(w);
				t.setUncaughtExceptionHandler(exceptionHandler);
				t.start();
			} catch (IOException e) {
				MindroidServerConsole console = MindroidServerConsole.getMindroidServerConsole();
				console.setVisible(true);
				console.appendLine("Error while receiving a message.");
				console.appendLine("IOException: "+e.getMessage()+"\n");
				return;
			}
		}
	}


	public static void invokeDisplayIPAdress() {

		Enumeration networkInterfaces = null;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e1) {
			e1.printStackTrace();
			mindroidServerFrame.displayIPAdress("Check connection.", Color.RED);
			MindroidServerConsole console = MindroidServerConsole.getMindroidServerConsole();
			console.setVisible(true);
			console.appendLine("Check your connection to the router.");
			console.appendLine("Try refreshing the IP Address (in File Menu). \n");
		}

		InetAddress loopback = null;
		InetAddress privateIP = null;
		while(networkInterfaces.hasMoreElements())
		{
			NetworkInterface n = (NetworkInterface) networkInterfaces.nextElement();
			Enumeration addresses = n.getInetAddresses();
			while (addresses.hasMoreElements())
			{
				InetAddress address = (InetAddress) addresses.nextElement();

				if (address.isLoopbackAddress()) {
					loopback = address;
				}
				if (address.isSiteLocalAddress()) {
					privateIP = address;
				}
			}

		}
		if (privateIP!=null) {
			mindroidServerFrame.displayIPAdress(privateIP.getHostAddress(),Color.BLACK);
			MindroidServerConsole console = MindroidServerConsole.getMindroidServerConsole();
			console.appendLine("Connected to the router.");
			console.appendLine("Server IP Address: "+ privateIP.getHostAddress() +"\n");
		} else if (loopback!=null) {
			mindroidServerFrame.displayIPAdress(loopback.getHostAddress(),Color.RED);
			MindroidServerConsole console = MindroidServerConsole.getMindroidServerConsole();
			console.setVisible(true);
			console.appendLine("Check your connection to the router. Only a loopback address was found.");
			console.appendLine("Try refreshing the IP Address (in File Menu). \n");
		} else {
			mindroidServerFrame.displayIPAdress("Check connection.", Color.RED);
			MindroidServerConsole console = MindroidServerConsole.getMindroidServerConsole();
			console.setVisible(true);
			console.appendLine("Check your connection to the router.");
			console.appendLine("Try refreshing the IP Address (in File Menu). \n");
		}



	}


}

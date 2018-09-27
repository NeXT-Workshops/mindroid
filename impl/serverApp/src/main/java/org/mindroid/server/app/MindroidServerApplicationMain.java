package org.mindroid.server.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * The main application of the Mindroid server
 *
 * @author Roland Kluge - Initial implementation
 */
public class MindroidServerApplicationMain {

    private static Logger logger;
    private final static int SERVER_PORT = 33044;
    private static MindroidServerFrame mindroidServerFrame;
    private static ServerSocket server = null;

    public static void main(String[] args) {

        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("log4j.configurationFile", "log4j2.xml");

        logger = LogManager.getLogger(MindroidServerApplicationMain.class);
        mindroidServerFrame = new MindroidServerFrame();
        try {
            runServer();
        } catch (Exception e) {
            MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
            console.setVisible(true);
            console.appendLine("Unknown Exception. Please restart the Application. Exception Message: " + e.getMessage());
            console.appendLine(e.toString());
            mindroidServerFrame.addContentLine("Local", "-", "ERROR", "Server not running.", "-");
            mindroidServerFrame.addContentLine("Local", "-", "LOG", "See Error console.", "-");
            mindroidServerFrame.disableRefresh(true);
        }
    }

    public static void runServer() {
        try {
            server = new ServerSocket(SERVER_PORT);
            invokeDisplayIPAdress();
            mindroidServerFrame.addContentLine("Local", "-", "LOG", "Server started", "-");
        } catch (IOException e) {
            MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
            console.setVisible(true);
            console.appendLine("The Server could not be started. Try restarting the Application.");
            console.appendLine(e.toString() + "\n");
            mindroidServerFrame.addContentLine("Local", "-", "ERROR", "Server not running.", "-");
            mindroidServerFrame.addContentLine("Local", "-", "LOG", "See Error console.", "-");
            mindroidServerFrame.disableRefresh(true);
            return;
        }

        Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread th, Throwable ex) {
                MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
                console.setVisible(true);
                console.appendLine("Messages from one connection might have been lost. No need to restart the application.");
                console.appendLine(ex.toString());
            }
        };

        int iteration = 0;
        while (true) {
            ++iteration;
            logger.info("[C# " + iteration + "] Waiting...");
            MindroidServerWorker w;
            try {
                w = new MindroidServerWorker(server.accept(), mindroidServerFrame);
                Thread t = new Thread(w);
                t.setUncaughtExceptionHandler(exceptionHandler);
                t.start();
            } catch (IOException e) {
                MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
                console.setVisible(true);
                console.appendLine("Error while receiving a message.");
                console.appendLine("IOException: " + e.getMessage() + "\n");
                return;
            }
        }
    }


    public static void invokeDisplayIPAdress() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();

            InetAddress loopback = null;
            List<InetAddress> privateIP = new ArrayList<>();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) networkInterfaces.nextElement();
                Enumeration addresses = n.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = (InetAddress) addresses.nextElement();

                    if (address.isLoopbackAddress()) {
                        loopback = address;
                    }

                    // The following filter has only been tested on Windows 8.1
                    if (address.isSiteLocalAddress() && isWirelessInterface(n)) {
                        privateIP.add(address);
                    }
                }

            }
            if (!privateIP.isEmpty()) {
                mindroidServerFrame.displayIPAdress(privateIP.get(0).getHostAddress(), Color.BLACK);
                MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
                console.appendLine("Available IP addresses: " + privateIP.toString());
                console.appendLine("Connected to the router.");
                console.appendLine("Server IP Address: " + privateIP.get(0).getHostAddress() + "\n");
            } else if (loopback != null) {
                mindroidServerFrame.displayIPAdress(loopback.getHostAddress(), Color.RED);
                MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
                console.setVisible(true);
                console.appendLine("Check your connection to the router. Only a loopback address was found.");
                console.appendLine("Try refreshing the IP Address (in File Menu). \n");
            } else {
                mindroidServerFrame.displayIPAdress("Check connection.", Color.RED);
                MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
                console.setVisible(true);
                console.appendLine("Check your connection to the router.");
                console.appendLine("Try refreshing the IP Address (in File Menu). \n");
            }

        } catch (SocketException e1) {
            e1.printStackTrace();
            mindroidServerFrame.displayIPAdress("Check connection.", Color.RED);
            MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
            console.setVisible(true);
            console.appendLine("Check your connection to the router.");
            console.appendLine("Try refreshing the IP Address (in File Menu). \n");
        }

    }

    private static boolean isWirelessInterface(final NetworkInterface n) {
        return n.getName().contains("wlan") || n.getDisplayName().toLowerCase().contains("wireless");
    }


}

package org.mindroid.server.app.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.mindroid.server.app.MindroidServerConsoleFrame;
import se.vidstige.jadb.ConnectionToRemoteDeviceException;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.List;

public class ADBService {
    private final static int ADB_TCP_PORT = 5555;
    static JadbConnection jadb = new JadbConnection();
    static List<JadbDevice> devices;
    static MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();

    public static List<JadbDevice> getDevices(){
        refreshAdbDevices();
        return devices;
    }

    /**
     * Connects via ADB to the given address.
     * @param socketAddress
     * @throws IOException
     * @throws JadbException
     * @throws ConnectionToRemoteDeviceException
     */
    public static void connectADB(InetSocketAddress socketAddress) throws IOException, JadbException, ConnectionToRemoteDeviceException {
        jadb.connectToTcpDevice(new InetSocketAddress(socketAddress.getAddress(), ADB_TCP_PORT));

        devices = jadb.getDevices();

        /*if (!devices.isEmpty()) {
            MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();

        }*/
    }

    public static void connectADB(String IP) throws ConnectionToRemoteDeviceException, IOException, JadbException {
        connectADB(new InetSocketAddress(IP,ADB_TCP_PORT));
    }

    public static JadbDevice getDeviceByIP(InetSocketAddress inetSocketAddress){
        refreshAdbDevices();
        MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
        for ( JadbDevice device : devices){
            console.appendLine(device.getSerial());
            console.appendLine(inetSocketAddress.getHostString());

            boolean state = inetSocketAddress.getHostString().concat( ":" + String.valueOf(ADB_TCP_PORT)).equals(device.getSerial());
            console.appendLine(String.valueOf(state));

            return device;
        }
        return null;
    }

    public static void refreshAdbDevices() {
        try {
            devices = jadb.getDevices();
        } catch (IOException e) {
            LogManager.getLogger(ADBService.class).log(Level.ERROR, "[refreshAdbDevices()] IOException: " + e.getMessage());
        }catch(JadbException e1){
            LogManager.getLogger(ADBService.class).log(Level.ERROR, "[refreshAdbDevices()] JadbException: " + e1.getMessage());
        }
    }

    public static void activateTethering(JadbDevice device){
        try {
            device.execute("su");
            device.execute("service call connectivity 33 i32 1");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JadbException e) {
            e.printStackTrace();
        }

    }

    /**
     * Retrieves ADB-Connection State by running "adb devices" and scanning the output
     * @param ip as a String
     * @return String to show in dialog
     * @throws IOException
     */
    public static String getADBStateByIP(String ip) throws IOException {
        Process proc = Runtime.getRuntime().exec("adb devices");
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        String[] parts = null;
        while ((line = reader.readLine()) != null){
            if (line.contains(ip)){
                parts = line.split("\\p{Blank}+");
                for (String part : parts) {
                    System.out.print(part+"||");
                }
                System.out.println();
            }
        }
        if (parts != null){
            String state = parts[parts.length -1];
            if(state == "device")
                return "connected";
            else
                return state;
        }else{
            return "IP not found";
        }

    }
}

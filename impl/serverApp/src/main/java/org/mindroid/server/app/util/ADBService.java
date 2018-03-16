package org.mindroid.server.app.util;

import org.mindroid.server.app.MindroidServerConsoleFrame;
import se.vidstige.jadb.ConnectionToRemoteDeviceException;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.List;

public class ADBService {
    private final static int ADB_TCP_PORT = 12345;
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
        if (!devices.isEmpty()) {
            MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
            console.setVisible(true);
            console.appendLine("Connected devices:");
            for (JadbDevice device : devices) {
                console.appendLine(device.toString());
            }
            //console.appendLine(devices.toString());
        }
    }
    public static void runDmesg() {
        InputStream is = null;
        try {
            is = devices.get(0).executeShell("dmesg | tail -n 5");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JadbException e) {
            e.printStackTrace();
        }

        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line, timestamp;
            while(br.read() != -1){
                line = br.readLine();
                console.appendLine(line);
                if(line.contains("USB_STATE=CONFIGURED")){
                    timestamp = line.substring(1,12);
                    console.appendLine(timestamp);
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static JadbDevice getDeviceByIP(InetSocketAddress inetSocketAddress){
        refreshAdbDevices();
        MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
        for ( JadbDevice device : devices){
            console.appendLine(device.getSerial());
            console.appendLine(inetSocketAddress.getHostString());

            boolean state = inetSocketAddress.getHostString().concat( ":" + String.valueOf(ADB_TCP_PORT)).equals(device.getSerial());
            console.appendLine(String.valueOf(state));
        }
        return null;
    }

    public static void refreshAdbDevices() {
        try {
            devices = jadb.getDevices();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JadbException e) {
            e.printStackTrace();
        }
    }
}

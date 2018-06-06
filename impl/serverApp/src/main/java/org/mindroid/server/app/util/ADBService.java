package org.mindroid.server.app.util;

import org.apache.logging.log4j.core.util.Throwables;
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

            return device;
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
}

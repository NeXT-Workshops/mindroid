package org.mindroid.server.app.util;

import org.mindroid.server.app.MindroidServerConsoleFrame;
import se.vidstige.jadb.ConnectionToRemoteDeviceException;
import se.vidstige.jadb.JadbException;

import javax.swing.*;
import java.io.IOException;

public class ManualADB extends JFrame {

    public static ManualADB instance = new ManualADB();

    public static ManualADB getInstance() {
        return instance;
    }

    private static String enteredIP = "192.168.3.1";

    private ManualADB() {
        this.setTitle("ADB Connect");
    }

    public void showDialog() {
        String IP = enteredIP;

        IP = JOptionPane.showInputDialog(this, "Enter IP Address", IP);

        if(!IPService.isValidIP(IP)) {
            MindroidServerConsoleFrame.getMindroidServerConsole().appendLine("Invalid IP Address! Enter a valid IPv4 address! (without a PORT)");
            MindroidServerConsoleFrame.getMindroidServerConsole().setVisible(true);
        }

        if (IP != null && IPService.isValidIP(IP)) {
            enteredIP = IP;
            connectADB(IP);
        }
    }

    private static void connectADB(String IP) {
        try {
            ADBService.connectADB(IP);
        } catch (ConnectionToRemoteDeviceException | IOException | JadbException e) {
            MindroidServerConsoleFrame.getMindroidServerConsole().appendLine(e.getMessage());
            MindroidServerConsoleFrame.getMindroidServerConsole().setVisible(true);
        }
    }

}

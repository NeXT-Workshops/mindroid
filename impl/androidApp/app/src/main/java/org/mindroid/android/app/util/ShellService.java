package org.mindroid.android.app.util;

import eu.chainfire.libsuperuser.Shell;

import java.util.ArrayList;
import java.util.List;

public class ShellService {

    public static final String ADB_DEFAULT_PORT = "12345";

    /**
     * Starts the ADB Service on Port.
     *
     * @param port the adb service is listening to. you can use the default port {@link #ADB_DEFAULT_PORT}
     */
    public static void startADB(String port){
        List<String> cmds = new ArrayList<String>();
        cmds.add("setprop service.adb.tcp.port "+port);
        cmds.add("stop adbd");
        cmds.add("start adbd");
        Shell.SU.run(cmds);
    }

    /**
     * Enables/Disables USB-Charging, and sets Symbol in Status Bar correspondingly
     * @param on
     */

    public static void usbChargingControl(boolean on){
        String value = on ? "1" : "0";
        List<String> cmds = new ArrayList<String>();

        // En-/Disbales Charging
        cmds.add("chmod +w /sys/class/power_supply/ac/charging_enabled");
        cmds.add("echo " + value + " > /sys/class/power_supply/ac/charging_enabled");

        // sets the charging Symbol to curernt function
        cmds.add("dumpsys battery set usb " + value);
        Shell.SU.run(cmds);
    }
    public static void activateTethering(boolean on){
        Shell.SU.run("service call connectivity 33 i32 " + (on ? "1" : "0") );

    }
}

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
     * Enables/Disables USB-Charging
     * @param on
     */

    public static void usbChargingControl(boolean on){
        Shell.SU.run("dumpsys battery set usb " + (on ? "1" : "0") );
    }
}

package org.mindroid.android.app.util;

import eu.chainfire.libsuperuser.Shell;

import java.util.ArrayList;
import java.util.List;

public class ADBService {

    public static final String ADB_DEFAULT_PORT = "12345";

    /**
     * Starts the ADB Service on Port.
     *
     * @param port the adb service is listening to. you can use the default port {@link #ADB_DEFAULT_PORT}
     */
    public static void start(String port){
        List<String> cmds = new ArrayList<String>();
        cmds.add("setprop service.adb.tcp.port "+port);
        cmds.add("stop adbd");
        cmds.add("start adbd");
        Shell.SU.run(cmds);
    }
}

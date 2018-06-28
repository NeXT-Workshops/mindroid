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

    public static void execIfConfig(String pathToOutputFile){
        List<String> cmds = new ArrayList<String>(2);
        cmds.add("su");
        cmds.add("ifconfig >"+ pathToOutputFile);
        Shell.SU.run(cmds);
    }

    /**
     *
     * @param on
     */
    public static void setTethering(boolean on){
        Shell.SU.run("service call connectivity 33 i32 " + (on ? "1" : "0") );
    }

    public static void grantWriteExternalSotragePermission(){
        Shell.SU.run("pm grant org.mindroid.android.app android.permission.WRITE_EXTERNAL_STORAGE" );
    }

    public static void grantReadExternalSotragePermission(){
        Shell.SU.run("pm grant org.mindroid.android.app android.permission.READ_EXTERNAL_STORAGE" );
    }
}

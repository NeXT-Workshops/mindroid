package org.mindroid.impl.util;

public class Messaging {

    private static final String regexIP = "((((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)[.]){3})(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?){1}){1}";


    public static boolean isValidTCPPort(int brickTCPport) {
        return (brickTCPport >= 0 && brickTCPport <= 65535);
    }

    public static boolean isValidIP(String brickIP) {
        if(brickIP == null){
            System.err.println("RobotFactory: BrickIP is null");
            return false;
        }
        return brickIP.matches(regexIP);
    }


}

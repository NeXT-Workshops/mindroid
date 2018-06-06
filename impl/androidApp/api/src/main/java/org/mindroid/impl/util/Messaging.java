package org.mindroid.impl.util;

public class Messaging {

    private static final String regexIP = "((((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)[.]){3})(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?){1}){1}";


    public static boolean isValidPort(int port) {
        return (port >= 0 && port <= 65535);
    }

    public static boolean isValidIP(String ip) {
        if(ip == null){
            System.err.println("Messaging: IP is null");
            return false;
        }
        return ip.matches(regexIP);
    }


}

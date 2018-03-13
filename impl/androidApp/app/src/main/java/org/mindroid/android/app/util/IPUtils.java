package org.mindroid.android.app.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import org.mindroid.impl.util.Messaging;

import static android.content.Context.WIFI_SERVICE;

public class IPUtils {

    /**
     * Concatenates the for parts of an IP:
     * Does not check if it is an valid IP address
     * @param part1 first '0-255'
     * @param part2 second '0-255'
     * @param part3 third '0-255'
     * @param part4 fourth '0-255'
     * @return IP: '<first>.<second>.<third>.<fourth>'
     */
    public static String getConcatenatedIPAddress(String part1,String part2,String part3,String part4){
        return getConcatenatedString(part1,".",part2,".",part3,".",part4);
    }

    //Concatenates an array of Strings
    public static String getConcatenatedString(String... str){
        String result = "";
        for (int i = 0; i < str.length; i++) {
            result = result.concat(str[i]);
        }
        return result;
    }

    public static boolean validateIP(String IP){
        return Messaging.isValidIP(IP);
    }

    public static String getDevIP(Context context){
        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
}

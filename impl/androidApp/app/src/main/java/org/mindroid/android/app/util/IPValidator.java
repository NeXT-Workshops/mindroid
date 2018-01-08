package org.mindroid.android.app.util;

import org.mindroid.impl.util.Messaging;

public class IPValidator {

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
}

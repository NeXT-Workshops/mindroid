package org.mindroid.server.app.util;

import org.mindroid.common.messages.server.Destination;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

public class IPService {

    private static HashMap<Destination, InetSocketAddress> ipMapping = new HashMap<>();
    private static HashMap<Destination, Socket> socketMapping = new HashMap<>();

    public static InetSocketAddress findAddress(Destination destination) {
        return ipMapping.get(destination);
    }

    public static Socket findSocket(Destination destination){
        return socketMapping.get(destination);
    }

    public static HashMap<Destination, InetSocketAddress> getIPMapping() {return ipMapping;}

    public static HashMap<Destination, Socket> getSocketMapping() {
        return socketMapping;
    }

}

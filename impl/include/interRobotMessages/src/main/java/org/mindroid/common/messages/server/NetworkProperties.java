package org.mindroid.common.messages.server;

/**
 *
 *
 */
public enum NetworkProperties {
    LOG_SERVER(2550); //IMPORTANT: PORT HAS TO BE HIGHER THEN 1024 DUE TO ANDROID PERMISSIONS ON BINDING SOCKETS!
    
    
    int port;

    NetworkProperties(int port){
        this.port = port;
    }
    
    public int getPort(){
        return port;
    }
}

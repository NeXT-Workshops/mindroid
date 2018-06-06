package org.mindroid.common.messages.hardware;

public enum EV3SensorPort {
    	S1("S1"),
    	S2("S2"),
    	S3("S3"),
    	S4("S4");
    
    	String identifier;
    	
    	EV3SensorPort(String identifier){
    		this.identifier = identifier;
    	}
    	
    	public String getValue(){
    		return identifier;
    	}
    	
    	public static EV3SensorPort getPort(String identifier){
    		switch(identifier){
    			case "S1": return S1;
    			case "S2": return S2;
    			case "S3": return S3;
    			case "S4": return S4;
    			default: return null;
    		}

    	}
}

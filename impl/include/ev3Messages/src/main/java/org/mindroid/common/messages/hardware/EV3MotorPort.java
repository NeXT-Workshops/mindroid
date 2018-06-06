package org.mindroid.common.messages.hardware;

public enum EV3MotorPort {
    	A("A"),
    	B("B"),
    	C("C"),
    	D("D");
    
    	String identifier;
    	
    	EV3MotorPort(String identifier){
    		this.identifier = identifier;
    	}
    	
    	public String getValue(){
    		return identifier;
    	}
    	
    	public static EV3MotorPort getPort(String identifier){
    		switch(identifier){
    			case "A": return A;
    			case "B": return B;
    			case "C": return C;
    			case "D": return D;
    			default: return null;
    		}

    	}

}

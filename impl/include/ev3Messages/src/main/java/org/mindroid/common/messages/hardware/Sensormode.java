package org.mindroid.common.messages.hardware;


public enum Sensormode {
    //Modes of EV3ColorSensor
    RED("Red"),
    AMBIENT("Ambient"),
    COLOR_ID("ColorID"),
    RGB("RGB"),

    //Modes of EV3UltraSonicSensor
    DISTANCE("Distance"),
    LISTEN("Listen"),

    //Mode of EV3IRSensor, also DISTANCE
    SEEK("Seek"),	//Untested Identifier(Stringvalue)

    //Modes of EV3GyroSensor
    ANGLE("Angle"),
    RATE("Rate"),					//Untested Identifier
    RATEANDANGLE("Angle and Rate"),

    //Mode of EV3TouchSensor
    TOUCH("Touch");			//Untested Identifier(Stringvalue)

    String mode ="";

    Sensormode(String mode){
        this.mode = mode;
    }

    public String getValue(){
        return mode;
    }

}

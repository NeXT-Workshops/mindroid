package mindroid.common.ev3.app;

public enum DeviceSignatures {
	
	//Sensors
	EV3_COLORSENSOR_SIGNATURE("UART:COL-REFLECT"),
	EV3_ULTRASONIC_SIGNATURE("UART:US-DIST-CM"),
	EV3_TOUCH_SIGNATURE("EV3_ANALOG:EV3_TOUCH"),
	EV3_IRSENSOR_SIGNATURE(null), //TODO find signature!
	EV3_GYRO_SIGNATURE("UART:GYRO-ANG"),
	
	//Motors
	UNREGULATED_MOTOR_SIGNATURE("OUTPUT_TACHO:TACHO"),
	REGULATED_MOTOR_SIGNATURE("OUTPUT_TACHO:MINITACHO"); 
	
	
	String signature;
	
	DeviceSignatures(String signature){
		this.signature = signature;
	}
	
	public String getSignature(){
		return signature;
	}
}

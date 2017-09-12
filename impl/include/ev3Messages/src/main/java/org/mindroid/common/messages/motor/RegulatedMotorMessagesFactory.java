package org.mindroid.common.messages.motor;

public abstract class RegulatedMotorMessagesFactory {

	/**
	 * Creates a MotorState message
	 * @param motorState the motorstate
	 * @return the Motorstate-Message object
	 */
	public static MotorStateMessage createMotorStateMessage(MotorState motorState){
		return new MotorStateMessage(motorState);
	}

	public static RotateToMessage createRotateToMessage(int angle){
		return new RotateToMessage(angle);
	}

	public static RotateToMessage createRotateToMessage(int angle, boolean immidiateReturn){
		return new RotateToMessage(angle,immidiateReturn);
	}

	public static RotateMessage createRotateMessage(int angle){
		return new RotateMessage(angle);
	}



	public static RotateMessage createRotateMessage(int angle, boolean immidiateReturn){
		return new RotateMessage(angle,immidiateReturn);
	}
	
	public static SetMotorSpeedMessage createSetSpeedMessage(int speed) {
		return  new SetMotorSpeedMessage(speed);
	}

	public static ForwardMessage createForwardMessage() {
		return new ForwardMessage();
	}

	public static BackwardMessage createBackwardMessage() {
		return new BackwardMessage();
	}

	public static StopMessage createStopMessage() {
		return new StopMessage();
	}

	public static StopMessage createStopMessage(boolean immidiateReturn) {
		return new StopMessage(immidiateReturn);
	}

	public static FltMessage createFltMessage() { return new FltMessage();}

}

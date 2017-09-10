package org.mindroid.common.messages.motor;

public abstract class RegulatedMotorMessagesFactory {
	

	public static MotorStateMessage createMotorStateMessage(MotorState motorState){
		return new MotorStateMessage(motorState);
	}

	public static RotateToMessage createRotateToMessage(int angle){
		RotateToMessage rtm = new RotateToMessage();
		rtm.setAngle(angle);
		return rtm;
	}

	public static RotateMessage createRotateMessage(int angle){
		RotateMessage rm = new RotateMessage();
		rm.setAngle(angle);
		return rm;
	}
	
	public static SetMotorSpeedMessage createSetSpeedMessage(int speed) {
		SetMotorSpeedMessage spm = new SetMotorSpeedMessage();
		spm.setSpeed(speed);
		return spm;
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

}

package org.mindroid.common.messages;

/**
 * Created by markus on 20.04.16.
 */
public abstract class UnregulatedMotorMessages {

	public static class MotorState{
		int tachoCount;
		int power;
		boolean isMoving;
		public MotorState(){}
		public MotorState(int tachoCount, int power, boolean isMoving) {
			super();
			this.tachoCount = tachoCount;
			this.power = power;
			this.isMoving = isMoving;
		}
		public int getTachoCount() {
			return tachoCount;
		}
		public void setTachoCount(int tachoCount) {
			this.tachoCount = tachoCount;
		}
		public int getPower() {
			return power;
		}
		public void setPower(int power) {
			this.power = power;
		}
		public boolean isMoving() {
			return isMoving;
		}
		public void setMoving(boolean isMoving) {
			this.isMoving = isMoving;
		};
	}
	
	public static SetPowerMsg setPower(int power) {
		SetPowerMsg setPower = new SetPowerMsg();
		setPower.setPower(power);
		return setPower;
	}

	public static ForwardMsg forward() {
		return new ForwardMsg();
	}

	public static BackwardMsg backward() {
		return new BackwardMsg();
	}

	public static StopMsg stop() {
		return new StopMsg();
	}

	public static class SetPowerMsg {
		private int power;
		
		public SetPowerMsg(){}

		public int getPower() {
			return power;
		}

		public void setPower(int power) {
			this.power = power;
		}

	}

	public static class ForwardMsg {
		public ForwardMsg(){}
	}

	public static class BackwardMsg {
		public BackwardMsg(){}
	}

	public static class StopMsg {
		public StopMsg(){}
	}

}

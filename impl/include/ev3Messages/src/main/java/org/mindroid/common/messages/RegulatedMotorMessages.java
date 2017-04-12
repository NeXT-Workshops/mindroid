package org.mindroid.common.messages;

public abstract class RegulatedMotorMessages {
	
	public static class MotorState{
		
		int rotationSpeed;
		int limitAngle;
		int acceleration;
		int tachoCount;
		float position;
		float maxSpeed;
		
		public MotorState() { }

		
		
		public MotorState(int rotationSpeed, int limitAngle, int acceleration, int tachoCount, float position,
				float maxSpeed) {
			super();
			this.rotationSpeed = rotationSpeed;
			this.limitAngle = limitAngle;
			this.acceleration = acceleration;
			this.tachoCount = tachoCount;
			this.position = position;
			this.maxSpeed = maxSpeed;
		}



		public int getRotationSpeed() {
			return rotationSpeed;
		}

		public void setRotationSpeed(int rotationSpeed) {
			this.rotationSpeed = rotationSpeed;
		}

		public int getLimitAngle() {
			return limitAngle;
		}

		public void setLimitAngle(int limitAngle) {
			this.limitAngle = limitAngle;
		}

		public int getAcceleration() {
			return acceleration;
		}

		public void setAcceleration(int acceleration) {
			this.acceleration = acceleration;
		}

		public int getTachoCount() {
			return tachoCount;
		}

		public void setTachoCount(int tachoCount) {
			this.tachoCount = tachoCount;
		}

		public float getPosition() {
			return position;
		}

		public void setPosition(float position) {
			this.position = position;
		}

		public float getMaxSpeed() {
			return maxSpeed;
		}

		public void setMaxSpeed(float maxSpeed) {
			this.maxSpeed = maxSpeed;
		}
		
		
	}
	
	public static RotateToMessage rotateTo(int angle){
		RotateToMessage rtm = new RotateToMessage();
		rtm.setAngle(angle);
		return rtm;
	}
	
	public static class RotateToMessage{
		int angle;
		public RotateToMessage(){ }
		public int getAngle() {
			return angle;
		}
		public void setAngle(int anlge) {
			this.angle = anlge;
		};
	}

	public static RotateMessage rotate(int angle){
		RotateMessage rm = new RotateMessage();
		rm.setAngle(angle);
		return rm;
	}
	
	public static class RotateMessage{
		int angle;
		public RotateMessage(){ }
		public int getAngle() {
			return angle;
		}
		public void setAngle(int anlge) {
			this.angle = anlge;
		};
	}
	
	public static SetSpeedMsg setSpeed(int speed) {
		SetSpeedMsg spm = new SetSpeedMsg();
		spm.setSpeed(speed);
		return spm;
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

	public static class SetSpeedMsg {
		private int speed;
		
		public SetSpeedMsg(){}

		public int getSpeed() {
			return speed;
		}

		public void setSpeed(int speed) {
			this.speed = speed;
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

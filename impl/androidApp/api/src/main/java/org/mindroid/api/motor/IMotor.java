package org.mindroid.api.motor;

public interface IMotor {
	void forward();
	void backward();
	void stop();
	void setSpeed(int speed);
	
}

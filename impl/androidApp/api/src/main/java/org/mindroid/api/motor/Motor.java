package org.mindroid.api.motor;

public interface Motor {
	void forward();
	void backward();
	void stop();
	void setSpeed(int speed);
	
}

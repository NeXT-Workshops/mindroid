package org.mindroid.api.statemachine;

public interface ITimeEvent {
	float getDelay();
	IState getOwner();
}

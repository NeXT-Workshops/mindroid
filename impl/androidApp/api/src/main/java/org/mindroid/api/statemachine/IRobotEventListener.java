package org.mindroid.api.statemachine;

import org.mindroid.api.statemachine.constraints.IConstraint;

public interface IRobotEventListener {
	
	void handleSatisfiedConstraint(IConstraint event);
}

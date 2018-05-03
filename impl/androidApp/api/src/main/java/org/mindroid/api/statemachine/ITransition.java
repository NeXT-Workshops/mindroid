package org.mindroid.api.statemachine;

import org.mindroid.api.statemachine.constraints.IConstraint;

public interface ITransition {
	

	/** if constraint is satisfied, returns destination Sate else null **/
	IState fire();

	void run();

	void setConstraint(IConstraint constraint);
	
	IConstraint getConstraint();

	IState getDestination();

	void setDestination(IState state);
}

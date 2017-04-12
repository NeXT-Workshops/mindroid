package org.mindroid.api.statemachine;

import java.util.List;

import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.exception.DuplicateTransitionException;

public interface IState {

	/**
	 * Contains the behavior while the state is active
	 */
	void run();
	
	void activate();
	
	void deactivate();
	
	boolean isActive();
	
	List<? extends ITransition> getTransitions();
	
	/**
	 * Add a Transition to this State.
	 *  
	 * @param transition
	 * @throws DuplicateTransitionException 
	 */
	void addTransition(ITransition transition) throws DuplicateTransitionException;
	
	/**
	 * Get the ID from this state
	 * @return
	 */
	String getName();
	
	/** returns a transition, which is satisfied to the given IConstraint. If no satisfied transition exists it returns null **/
	ITransition getSatisfiedTransition(IConstraint tmpEvent);

	List<IConstraint> getConstraints();
}

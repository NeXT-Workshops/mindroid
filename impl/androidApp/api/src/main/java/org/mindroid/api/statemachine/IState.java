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
	 * @param transition transition to add
	 *
	 * @throws DuplicateTransitionException thrown if the transition which should be added already exists
	 */
	void addTransition(ITransition transition) throws DuplicateTransitionException;
	
	/**
	 * Get the ID from this state
	 *
	 * @return name as a string
	 */
	String getName();

	/**
	 *  returns a transition, which is satisfied to the given IConstraint. If no satisfied transition exists it returns null
	 *
	 * @param tmpEvent constraint that should be satisfied
	 *
	 * @return satisfied transition
	 */
	ITransition getSatisfiedTransition(IConstraint tmpEvent);

	List<IConstraint> getConstraints();
}

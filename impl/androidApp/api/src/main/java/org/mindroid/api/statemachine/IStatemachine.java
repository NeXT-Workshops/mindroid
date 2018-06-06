package org.mindroid.api.statemachine;

import java.util.Collection;

import org.mindroid.api.statemachine.exception.NoCurrentStateSetException;
import org.mindroid.api.statemachine.exception.StateAlreadyExistsException;

public interface IStatemachine {

	IState getCurrentState();
	
	public void setStartState(IState startstate);
	
	public Collection<? extends IState> getStates();

	public Collection<? extends ITransition> currentStateTransitions();

	public void addTransition(ITransition transition, IState fromState, IState toState);

	public void addState(IState state);
	
	public IState getState(String name);

	public void setID(String id);

	public String getID();

	public IState getStartState();

	public void reset();
	
	public void start() throws NoStartStateException;
	
	public void stop();

	void addStates(Collection<IState> states);

	boolean isActive();

	boolean isMessageingAllowed();

	void setIsMessageingAllowed(boolean value);

	boolean isInvalidStatemachine();
}

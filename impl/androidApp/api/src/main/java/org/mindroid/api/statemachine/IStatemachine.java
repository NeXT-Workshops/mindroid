package org.mindroid.api.statemachine;

import java.util.Collection;

import org.mindroid.api.statemachine.exception.NoCurrentStateSetException;
import org.mindroid.api.statemachine.exception.StateAlreadyExists;

public interface IStatemachine {

	IState getCurrentState();
	
	public void setStartState(IState startstate);
	
	public Collection<? extends IState> getStates();

	public Collection<? extends ITransition> currentStateTransitions() throws NoCurrentStateSetException;

	public void addTransition(ITransition transition, IState fromState, IState toState);

	public void addState(IState state) throws StateAlreadyExists;
	
	public IState getState(String name);

	public void setID(String id);

	public String getID();

	public IState getStartState();

	public void reset();
	
	public void start() throws NoStartStateException;
	
	public void stop();

	void addStates(Collection<IState> states) throws StateAlreadyExists;

	boolean isActive();
}

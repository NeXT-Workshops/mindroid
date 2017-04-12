package org.mindroid.impl.statemachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.mindroid.api.statemachine.*;
import org.mindroid.api.statemachine.constraints.AbstractLogicOperator;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.exception.DuplicateTransitionException;
import org.mindroid.api.statemachine.exception.NoCurrentStateSetException;
import org.mindroid.api.statemachine.exception.NoSuchStateException;
import org.mindroid.api.statemachine.exception.StateAlreadyExsists;
import org.mindroid.api.statemachine.properties.ITimeProperty;
import org.mindroid.impl.statemachine.constraints.TimeExpired;

public class Statemachine implements IStatemachine{

	private String ID = null;
	IState currentState = null; //TODO refactor -> remove currentState attribute from Statemachine

	IState startState;
	/**
	 * contains all states
	 * Key: State name
	 * Value: state
	 */
	HashMap<String,IState> states = new HashMap<String,IState>();
	
	private ArrayList<IState> lstStates = new ArrayList<IState>();

	boolean isActive = false;


	public Statemachine(String ID){
		this.ID = ID;
	}

	@Override
	public IState getCurrentState() {
		return currentState;
	}

	@Override
	public void setStartState(IState startState) {
		this.startState = startState;
	}


	@Override
	public Collection<? extends IState> getStates() {
		lstStates.clear();
		for(String key : states.keySet()){
			lstStates.add(states.get(key));
		}
		return lstStates;
	}

	@Override
	public Collection<? extends ITransition> currentStateTransitions() throws NoCurrentStateSetException {
		if(currentState != null){
			return currentState.getTransitions();
		}else{
			throw new NoCurrentStateSetException("Current State is null");
		}
	}

	@Override
	public void addState(IState state) throws StateAlreadyExsists {
		if(this.states.containsKey(state.getName())){
			throw new StateAlreadyExsists("This Statemachine already has a State with this name: "+state.getName());
		}else{
			this.states.put(state.getName(), state);
		}
	}
	
	
	@Override
	public void addTransition(ITransition transition, IState fromState, IState toState){
		assert transition != null;
		assert fromState != null;
		assert toState != null;
		
		if(states.containsKey(fromState.getName()) && states.containsKey(toState.getName())){
			try {
				//Make new transition-object, so the user can use the same transition multiple times at differnt source and destination states without creating new object of the same transition!
				ITransition tmpTransition = new Transition(transition.getConstraint(),toState);
				tmpTransition.setDestination(toState);
				fromState.addTransition(tmpTransition);

				//Add StateInformation to TimeProperties/(no more yet) in Constraint
				addStateInformationToProperties(transition.getConstraint(),fromState);

				//transition.setDestination(toState);
				//fromState.addTransition(transition);
			} catch (DuplicateTransitionException e) {
				e.printStackTrace();
			}
			
		}else{
			new NoSuchStateException("At least one of the given sates: "+fromState.getName()+", "+toState.getName()+" does not exist!").printStackTrace();
		}
		
	}

	private void addStateInformationToProperties(IConstraint constraint,IState source) {
		if(constraint instanceof AbstractLogicOperator){
			addStateInformationToProperties(((AbstractLogicOperator) constraint).getLeftConstraint(),source);
			addStateInformationToProperties(((AbstractLogicOperator) constraint).getRightConstraint(),source);
		}else{
			if(constraint instanceof TimeExpired){
				if( ((TimeExpired) constraint).getProperty() instanceof ITimeProperty) {
					((ITimeProperty) ((TimeExpired) constraint).getProperty()).setSource(source);
				}
			}
		}
	}

	@Override
	public void addStates(Collection<IState> states) throws StateAlreadyExsists{
		for(IState s : states){
			addState(s);
		}
	}
	
	@Override
	public IState getState(String name) {
		return states.get(name);
	}

	@Override
	public void reset() {
		stop();
		currentState = startState;		
	}
	
	@Override
	public void start(){
		if(currentState == null){
			if(startState == null){
				//TODO Throw exception!
			}
			currentState = startState;
		}
		currentState.activate();
		isActive = true;
	}

	@Override
	public void stop(){
		currentState.deactivate();

		isActive = false;
	}
	
	
	@Override
	public String toString() {
		return "Statemachine [currentState=" + currentState + ", startState=" + startState + ", states=" + states + "]";
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public IState getStartState() {
		return startState;
	}
}

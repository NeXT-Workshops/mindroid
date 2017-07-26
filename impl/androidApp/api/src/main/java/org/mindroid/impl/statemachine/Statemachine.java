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
import org.mindroid.api.statemachine.exception.StateAlreadyExists;
import org.mindroid.api.statemachine.properties.ITimeProperty;
import org.mindroid.impl.statemachine.constraints.TimeExpired;

public class Statemachine implements IStatemachine{

	private String ID = null;
	IState currentState = null;

	IState startState;
	/**
	 * contains all states
	 * Key: State name
	 * Value: state
	 */
	HashMap<String,IState> states = new HashMap<String,IState>();
	
	private ArrayList<IState> lstStates = new ArrayList<IState>();

	private boolean isActive = false;


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
	public void addState(IState state) throws StateAlreadyExists {
		if(this.states.containsKey(state.getName())){
			throw new StateAlreadyExists("This Statemachine already has a State with this name: "+state.getName());
		}else{
			this.states.put(state.getName(), state);
		}
	}

	@Override
	public void addTransition(final ITransition transition, IState source, IState destination) {
		assert transition != null;
		assert source != null;
		assert destination != null;
		
		if(states.containsKey(source.getName()) && states.containsKey(destination.getName())){
			try {
				//Make new transition-object, so the user can use the same transition multiple times at differnt source and destination states without creating new object of the same transition!
				//Possible reuse of defined-constraints as well
				ITransition tmpTransition = new Transition(transition.getConstraint().copy(),destination){
					@Override
					public void run(){
						transition.run();
					}
				};
				tmpTransition.setDestination(destination);
				source.addTransition(tmpTransition);

				//Add StateInformation to TimeProperties/(no more yet) in Constraint
				addStateInformationToProperties(tmpTransition.getConstraint(),source);
			} catch (DuplicateTransitionException e) {
				e.printStackTrace();
			}
			
		}else{
			new NoSuchStateException("At least one of the given sates: "+source.getName()+", "+destination.getName()+" does not exist!").printStackTrace();
		}
		
	}

	/**
	 * Completes the Information of the ConstraintProperties. (Source needed)
	 * @param constraint
	 * @param source
	 */
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
	public void addStates(Collection<IState> states) throws StateAlreadyExists{
		for(IState s : states){
			addState(s);
		}
	}
	
	@Override
	public IState getState(String name) {
		return states.get(name);
	}



	@Override
	public synchronized void reset() {
		stop();
		currentState = startState;		
	}
	
	@Override
	public void start() throws NoStartStateException {
		if(currentState == null){
			if(startState == null){
				throw new NoStartStateException("No Start State specified for this (ID:'"+getID()+"') Statemachine. Use setStartState(..) to specify a State to begin with!");
			}
			currentState = startState;
			this.isActive = true;
			currentState.activate();
		}
	}

	@Override
	public void stop(){
		currentState.deactivate();
		this.isActive = false;
	}


	@Override
	public String toString() {
		return "Statemachine{" +
				"ID='" + ID + '\'' +
				", startState=" + startState.getName() +
				", currentState="+ ((currentState == null) ? "null" : currentState.getName()) +
				", isActive=" + isActive +
				", nbOfStates ="+ states.keySet().size()+
				'}';
	}

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public void setID(String id) {
		this.ID = id;
	}

	@Override
	public IState getStartState() {
		return startState;
	}

	@Override
	public synchronized boolean isActive() {
		return isActive;
	}
}

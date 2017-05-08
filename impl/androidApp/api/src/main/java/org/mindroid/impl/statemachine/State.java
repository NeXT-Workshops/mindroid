package org.mindroid.impl.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mindroid.api.statemachine.*;
import org.mindroid.api.statemachine.constraints.IConstraint;
import org.mindroid.api.statemachine.exception.DuplicateTransitionException;
import org.mindroid.api.statemachine.exception.NoSuchStateException;

public class State implements IState{

	String name = null;
	
	private boolean isActive = false;
	
	HashMap<IConstraint, ITransition> transitions = new HashMap<IConstraint, ITransition>();
	
	/**
	 * Temporary list to return;
	 * may not be consistent during runtime!
	 */
	private ArrayList<ITransition> list = new ArrayList<ITransition>(transitions.size());

	public State(String name){
		this.name = name;
	}
	
	@Override
	public void run() {
		// do something
	}

	@Override
	public List<? extends ITransition> getTransitions() {
		list.clear();
		for(IConstraint key : transitions.keySet()){
			list.add(transitions.get(key));
		}
		return list;
	}

	@Override
	public void addTransition(ITransition transition) throws DuplicateTransitionException {
		if(transition.getDestination() != null){
		
			if(transitions.containsKey(transition.getConstraint())){
				throw new DuplicateTransitionException(getDuplicateExceptionMessage());
			}else{
				transitions.put(transition.getConstraint(), transition);
				
			}
		}else{
			new NoSuchStateException("The Destination State of the Transition can't be null").printStackTrace();
		}
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	private String getDuplicateExceptionMessage(){
		return "The state '"+name+"' already contains a Transition with this constraint! Transition-Constraints have to be unique for each outgoing Transition!";
	}

	@Override
	public ITransition getSatisfiedTransition(IConstraint constraint) {
		return transitions.get(constraint);
	}

	@Override
	public List<IConstraint> getConstraints() {
		Object[] constraints = transitions.keySet().toArray();
		ArrayList<IConstraint> listOfConstraints = new ArrayList<>(constraints.length);
		for (int i = 0; i< constraints.length; i++) {
			listOfConstraints.add((IConstraint) constraints[i]);
		}
		return listOfConstraints;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	@Override
	public synchronized void activate() {
		synchronized(this){
			System.out.println("State.activate(): "+getName()+"->State.activate()");

			this.isActive = true;
			run();
		}
	}

	@Override
	public synchronized void deactivate() {
		this.isActive = false;
		System.out.println("State.deactivate(): "+getName()+" is not active anymore");
	}

	@Override
	public synchronized boolean isActive() {
		return this.isActive;
	}

	@Override
	public String toString() {
		return "State [name=" + name + ", isActive=" + isActive + ", transitions=" + transitions + ", list=" + list + "]";
	}
	
	

}

package org.mindroid.impl.statemachine;

import org.mindroid.api.statemachine.IState;
import org.mindroid.api.statemachine.ITransition;
import org.mindroid.api.statemachine.constraints.IConstraint;


public class Transition implements ITransition{

	IConstraint constraint;
	
	IState destination = null;

	/**
	 * Transition object between states
	 *
	 * @param constraint constraint of the transition
	 * @param destination - destination state of the transition
	 */
	public Transition(IConstraint constraint, IState destination){
		this.constraint = constraint;
		this.destination = destination;
	}
	
	public Transition(IConstraint constraint){
		this.constraint = constraint;
	}

	@Override
	public void run() {
		System.out.println("TRANSITION: TRANSITION FIRED! -> Standard run method executed!");
		//do something
	}

	@Override
	public IState fire() {
		run();
		return getDestination();
	}

	@Override
	public IConstraint getConstraint() {
		return constraint;
	}

	@Override
	public void setConstraint(IConstraint constraint) {
		this.constraint = constraint;		
	}


	@Override
	public IState getDestination() {
		return destination;
	}

	@Override 
	public void setDestination(IState state){
		this.destination = state;
	}
	
	@Override
	public String toString() {
		return "Transition [constraint=" + constraint + ", destination=" + destination.getName() + "]";
	}



}

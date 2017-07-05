package org.mindroid.api.statemachine.exception;

public class StateAlreadyExists extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3380370092058182711L;

	public StateAlreadyExists(String msg){
		super(msg);
	}
}

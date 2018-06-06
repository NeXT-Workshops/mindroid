package org.mindroid.api.statemachine.exception;

public class StateAlreadyExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3380370092058182711L;

	public StateAlreadyExistsException(String msg){
		super(msg);
	}
}

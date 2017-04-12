package org.mindroid.api.statemachine.exception;

public class DuplicateTransitionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateTransitionException(String msg){
		super(msg);
	}
}

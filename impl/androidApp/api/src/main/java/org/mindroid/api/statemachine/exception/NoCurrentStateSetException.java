package org.mindroid.api.statemachine.exception;

public class NoCurrentStateSetException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6643849495694385190L;

	public NoCurrentStateSetException(String msg){
		super(msg);
	}

}

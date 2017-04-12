package org.mindroid.api.statemachine.exception;

public class NoSuchStateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1566317962922705954L;

	public NoSuchStateException(String msg){
		super(msg);
	}
}

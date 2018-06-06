package org.mindroid.api.endpoint;

public interface ClientEndpoint {
	/**
	 * Connect to server
	 */
	void connect();

	void disconnect();

	/**
	 * checks if client is ready 
	 * returns true if connection is established.
	 * @return true if the client is ready
	 */
	public boolean isClientReady();
	
	/**
	 * Stops the client
	 */
	public void stop();
}

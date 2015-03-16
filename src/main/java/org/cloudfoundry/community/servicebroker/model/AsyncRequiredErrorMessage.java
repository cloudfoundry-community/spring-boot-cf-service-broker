package org.cloudfoundry.community.servicebroker.model;

/**
 * Async Required error message to give back to CC.
 *
 */
public class AsyncRequiredErrorMessage extends ErrorMessage {

	private static String error = "AsyncRequired";
		
	/**
	 * This broker requires asynchronous processing. 
	 *
	 * @param description user facing error message.
	 */
	public AsyncRequiredErrorMessage(String description) {
		super(description);
	}
	
	public String getError() { 
		return error;
	}
}

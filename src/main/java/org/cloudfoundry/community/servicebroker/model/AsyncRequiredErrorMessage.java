package org.cloudfoundry.community.servicebroker.model;

/**
 * Async Required error message to give back to CC.
 *
 */
public class AsyncRequiredErrorMessage extends ErrorMessage {

	private static String error = "AsyncRequired";
	
	public AsyncRequiredErrorMessage() {
		super("This service plan requires client support for asynchronous service operations.");
	}

	public String getError() { 
		return error;
	}
}

package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Async Required error message to give back to CC.
 *
 */
public class AsyncRequiredErrorMessage extends ErrorMessage {

	public final static String ASYNC_REQUIRED_ERROR = "AsyncRequired";
		
	/**
	 * This broker requires asynchronous processing. 
	 *
	 * @param description user facing error message.
	 */
	public AsyncRequiredErrorMessage(String description) {
		super(description);
	}

	@JsonProperty("error")
	public String getError() { 
		return ASYNC_REQUIRED_ERROR;
	}
}

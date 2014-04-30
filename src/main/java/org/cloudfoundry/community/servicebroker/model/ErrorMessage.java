package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used to send errors back to the cloud controller.
 * 
 * @author sgreenberg@gopivotal.com
 *
 */
public class ErrorMessage {

	@JsonProperty("description")
	private String message;

	public ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}

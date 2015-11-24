package org.cloudfoundry.community.servicebroker.exception;

public class ServiceBrokerAsyncRequiredException extends Exception {

	private static final long serialVersionUID = 1L;
	private String description;

	public ServiceBrokerAsyncRequiredException(String description) { 
		this.setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	}
	
}

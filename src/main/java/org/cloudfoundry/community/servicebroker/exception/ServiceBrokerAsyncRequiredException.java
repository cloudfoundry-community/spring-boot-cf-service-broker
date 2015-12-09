package org.cloudfoundry.community.servicebroker.exception;

public class ServiceBrokerAsyncRequiredException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServiceBrokerAsyncRequiredException(String message) {
		super(message);
	}
}

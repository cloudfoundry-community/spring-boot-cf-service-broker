package org.cloudfoundry.community.servicebroker.exception;


public class ServiceBrokerApiVersionException extends Exception {

	private static final long serialVersionUID = -6792404679608443775L;

	public ServiceBrokerApiVersionException(String expectedVersion, String providedVersion) {
		super("The provided service broker API version is not supported: "
				+ "Expected Version = " + expectedVersion + ", "
				+ "Provided Version = " + providedVersion);
	}
	
}

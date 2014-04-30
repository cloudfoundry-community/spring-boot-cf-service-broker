package org.cloudfoundry.community.servicebroker.exception;


public class ServiceBrokerApiVersionException extends Exception {

	private static final long serialVersionUID = -6792404679608443775L;

	private String expectedVersion;
	private String providedVersion;
	
	public ServiceBrokerApiVersionException(String expectedVersion, String providedVersion) {
		this.expectedVersion = expectedVersion;
		this.providedVersion = providedVersion;
	}
	
	public String getMessage() {
		return "The provided service broker api version is not supported: "
				+ "Expected Version = " + expectedVersion + ", "
				+ "Provided Version = " + providedVersion;
	}
	
}

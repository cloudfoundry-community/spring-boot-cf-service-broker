package org.cloudfoundry.community.servicebroker.exception;

/**
 * Thrown to indicate that a duplicate service instance creation request is received.
 * 
 * @author sgreenberg@gopivotal.com
 */
public class ServiceInstanceExistsException extends RuntimeException {

	private static final long serialVersionUID = -914571358227517785L;
	
	public ServiceInstanceExistsException(String serviceInstanceId, String serviceDefinitionId) {
		super("Service instance with the given ID already exists: " +
				"serviceInstanceId=" + serviceInstanceId +
				", serviceDefinitionId=" + serviceDefinitionId);
	}

}

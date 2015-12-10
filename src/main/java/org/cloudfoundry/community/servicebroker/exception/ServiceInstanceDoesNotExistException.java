package org.cloudfoundry.community.servicebroker.exception;

/**
 * Thrown to indicate that a request is received for an unknown service instance ID.
 * 
 * @author sgreenberg@gopivotal.com
 */
public class ServiceInstanceDoesNotExistException extends RuntimeException {
	
	private static final long serialVersionUID = -1879753092397657116L;
	
	public ServiceInstanceDoesNotExistException(String serviceInstanceId) {
		super("Service instance does not exist: id=" + serviceInstanceId);
	}

}

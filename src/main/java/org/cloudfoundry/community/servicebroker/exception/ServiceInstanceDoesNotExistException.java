package org.cloudfoundry.community.servicebroker.exception;

/**
 * Thrown when a request is received for an unknown ServiceInstance.
 * 
 * @author sgreenberg@gopivotal.com
 *
 */
public class ServiceInstanceDoesNotExistException extends Exception {
	
	private static final long serialVersionUID = -1879753092397657116L;
	
	public ServiceInstanceDoesNotExistException(String serviceInstanceId) {
		super("ServiceInstance does not exist: id = " + serviceInstanceId);
	}

}

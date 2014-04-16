package com.pivotal.cf.broker.exception;

/**
 * Thrown when a request is received for an unknown ServiceInstance.
 * 
 * @author sgreenberg@gopivotal.com
 *
 */
public class ServiceInstanceDoesNotExistException extends Exception {
	
	private static final long serialVersionUID = -1879753092397657116L;
	
	private String serviceInstanceId;
	
	public ServiceInstanceDoesNotExistException(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}
	
	public String getMessage() {
		return "ServiceInstance does not exist: id = " + serviceInstanceId;
	}
	
}

package org.cloudfoundry.community.servicebroker.exception;

/**
 * Thrown when a duplicate request to bind to a service instance is received.
 * 
 * @author sgreenberg@gopivotal.com
 */
public class ServiceInstanceBindingExistsException extends Exception {

	private static final long serialVersionUID = -914571358227517785L;
	
	public ServiceInstanceBindingExistsException(String serviceInstanceId, String bindingId) {
		super("Service instance binding already exists: "
				+ "serviceInstanceId=" + serviceInstanceId
				+ ", bindingId=" + bindingId);
	}

}

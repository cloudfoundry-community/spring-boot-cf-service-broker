package org.cloudfoundry.community.servicebroker.exception;

/**
 * Thrown when a request is received for an unknown service instance binding.
 * 
 */
public class ServiceInstanceBindingDoesNotExistException extends Exception {

	private static final long serialVersionUID = -1879753092397657116L;

	public ServiceInstanceBindingDoesNotExistException(String bindingId) {
		super("Service binding does not exist: id=" + bindingId);
	}

}

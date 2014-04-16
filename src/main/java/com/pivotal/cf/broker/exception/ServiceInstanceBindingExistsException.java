package com.pivotal.cf.broker.exception;

import com.pivotal.cf.broker.model.ServiceInstanceBinding;

/**
 * Thrown when a duplicate request to bind to a service instance is 
 * received.
 * 
 * @author sgreenberg@gopivotal.com
 */
public class ServiceInstanceBindingExistsException extends Exception {

	private static final long serialVersionUID = -914571358227517785L;
	
	private ServiceInstanceBinding binding;
	
	public ServiceInstanceBindingExistsException(ServiceInstanceBinding binding) {
		this.binding = binding;
	}
	
	public String getMessage() {
		return "ServiceInstanceBinding already exists: serviceInstanceBinding.id = " 
				+ binding.getId()
				+ ", serviceInstance.id = " + binding.getServiceInstanceId();
	}
}

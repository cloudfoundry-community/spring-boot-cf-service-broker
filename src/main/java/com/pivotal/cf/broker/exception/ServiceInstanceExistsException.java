package com.pivotal.cf.broker.exception;

import com.pivotal.cf.broker.model.ServiceInstance;

/**
 * Thrown when a duplicate service instance creation request is
 * received.
 * 
 * @author sgreenberg@gopivotal.com
 */
public class ServiceInstanceExistsException extends Exception {

	private static final long serialVersionUID = -914571358227517785L;
	
	private ServiceInstance instance;
	
	public ServiceInstanceExistsException(ServiceInstance instance) {
		this.instance = instance;
	}
	
	public String getMessage() {
		return "ServiceInstance with the given id already exists: ServiceInstance.id = " + instance.getId() 
				+ ", Service.id = " + instance.getServiceDefinitionId();
	}
}

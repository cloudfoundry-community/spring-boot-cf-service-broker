package org.cloudfoundry.community.servicebroker.exception;

import org.cloudfoundry.community.servicebroker.model.ServiceInstance;

/**
 * Thrown when a duplicate service instance creation request is
 * received.
 * 
 * @author sgreenberg@gopivotal.com
 */
public class ServiceInstanceExistsException extends Exception {

	private static final long serialVersionUID = -914571358227517785L;
	
	public ServiceInstanceExistsException(ServiceInstance instance) {
		super("ServiceInstance with the given ID already exists: " +
				"ServiceInstance.id = " + instance.getServiceInstanceId() +
				", Service.id = " + instance.getServiceDefinitionId());
	}

}

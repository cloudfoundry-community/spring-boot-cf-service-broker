package com.pivotal.cf.broker.service;

import com.pivotal.cf.broker.exception.ServiceBrokerException;
import com.pivotal.cf.broker.exception.ServiceInstanceBindingExistsException;
import com.pivotal.cf.broker.model.ServiceInstance;
import com.pivotal.cf.broker.model.ServiceInstanceBinding;

/**
 * Handles bindings to service instances.
 * 
 * @author sgreenberg@gopivotal.com
 */
public interface ServiceInstanceBindingService {

	/**
	 * Create a new binding to a service instance.
	 * @param bindingId The id provided by the cloud controller
	 * @param serviceInstance The id of the service instance
	 * @param serviceId The id of the service
	 * @param planId The plan used for this binding
	 * @param appGuid The guid of the app for the binding
	 * @return
	 * @throws ServiceInstanceBindingExistsException if the same binding already exists.  
	 */
	ServiceInstanceBinding createServiceInstanceBinding(
			String bindingId, ServiceInstance serviceInstance, String serviceId, String planId, String appGuid)
			throws ServiceInstanceBindingExistsException, ServiceBrokerException;
	
	/**
	 * @param id
	 * @return The ServiceInstanceBinding or null if one does not exist.
	 */
	ServiceInstanceBinding getServiceInstanceBinding(String id);

	/**
	 * Delete the service instance binding.  If a binding doesn't exist, 
	 * return null.
	 * @param id 
	 * @return The deleted ServiceInstanceBinding or null if one does not exist.
	 */
	ServiceInstanceBinding deleteServiceInstanceBinding(String id) throws ServiceBrokerException;
	
}

package org.cloudfoundry.community.servicebroker.service;

import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.cloudfoundry.community.servicebroker.model.*;

/**
 * Handles bindings to service instances.
 * 
 * @author sgreenberg@gopivotal.com
 */
public interface ServiceInstanceBindingService {

	/**
	 * Create a new binding to a service instance.
	 * @param createServiceInstanceBindingRequest containing parameters sent from Cloud Controller
	 * @return The newly created ServiceInstanceBinding
	 * @throws ServiceInstanceBindingExistsException if the same binding already exists
	 * @throws ServiceBrokerException on internal failure
	 */
	ServiceInstanceBinding createServiceInstanceBinding(
			CreateServiceInstanceBindingRequest createServiceInstanceBindingRequest)
			throws ServiceInstanceBindingExistsException, ServiceBrokerException;

	/**
	 * Delete the service instance binding. If a binding doesn't exist, 
	 * return null.
	 * @param deleteServiceInstanceBindingRequest containing parameters sent from Cloud Controller
     * @return The deleted ServiceInstanceBinding or null if one does not exist
     * @throws ServiceBrokerException on internal failure
	 */
	ServiceInstanceBinding deleteServiceInstanceBinding(
			DeleteServiceInstanceBindingRequest deleteServiceInstanceBindingRequest) 
	        throws ServiceBrokerException;
	
}

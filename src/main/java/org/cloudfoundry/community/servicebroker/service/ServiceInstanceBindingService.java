package org.cloudfoundry.community.servicebroker.service;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;

/**
 * Handles bindings to service instances.
 * 
 * @author sgreenberg@gopivotal.com
 */
public interface ServiceInstanceBindingService {

	/**
	 * Create a new binding to a service instance.
	 *
	 * @param request containing parameters sent from Cloud Controller
	 * @return a CreateServiceInstanceBindingResponse
	 * @throws ServiceInstanceBindingExistsException if the same binding already exists
	 * @throws ServiceInstanceDoesNotExistException if the service instance ID is not valid
	 * @throws ServiceBrokerException on internal failure
	 */
	CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request)
			throws ServiceInstanceBindingExistsException, ServiceInstanceDoesNotExistException, ServiceBrokerException;

	/**
	 * Delete the service instance binding.
	 *
	 * @param request containing parameters sent from Cloud Controller
     * @throws ServiceBrokerException on internal failure
	 * @throws ServiceInstanceDoesNotExistException if the service instance ID is not valid
	 * @throws ServiceInstanceBindingDoesNotExistException if the service instance binding ID is not valid
	 */
	void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request)
			throws ServiceInstanceDoesNotExistException, ServiceInstanceBindingDoesNotExistException, ServiceBrokerException;
}

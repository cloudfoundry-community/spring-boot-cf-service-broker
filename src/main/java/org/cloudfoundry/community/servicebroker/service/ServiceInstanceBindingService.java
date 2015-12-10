package org.cloudfoundry.community.servicebroker.service;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;

/**
 * This interface is implemented by service brokers to process requests to create and delete service instance bindings.
 * 
 * @author sgreenberg@pivotal.io
 * @author Scott Frederick
 */
public interface ServiceInstanceBindingService {

	/**
	 * Create a new binding to a service instance.
	 *
	 * @param request containing parameters sent from Cloud Controller
	 * @return a CreateServiceInstanceBindingResponse
	 * @throws ServiceInstanceBindingExistsException if a binding with the requested ID already exists
	 * @throws ServiceInstanceDoesNotExistException if a service instance with the requested ID does not exist
	 * @throws ServiceBrokerException on internal failure
	 */
	CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request);

	/**
	 * Delete a service instance binding.
	 *
	 * @param request containing parameters sent from Cloud Controller
	 * @throws ServiceInstanceDoesNotExistException if a service instance with the requested ID does not exist
	 * @throws ServiceInstanceBindingDoesNotExistException if a binding with the requested ID does not exist
	 */
	void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request);
}

package org.cloudfoundry.community.servicebroker.service;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;

/**
 * Handles instances of service definitions.
 * 
 * @author sgreenberg@gopivotal.com
 */
public interface ServiceInstanceService {

	/**
	 * Create a new instance of a service
	 *
	 * @param request containing the parameters from Cloud Controller
	 * @return a CreateServiceInstanceResponse
	 * @throws ServiceInstanceExistsException if the service instance already exists
	 * @throws ServiceBrokerException if something goes wrong internally
	 * @throws ServiceBrokerAsyncRequiredException if we must use an async comparable Cloud Controller
	 */
	CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request)
			throws ServiceInstanceExistsException, ServiceBrokerException, ServiceBrokerAsyncRequiredException;

	/**
	 * @param request containing the parameters from Cloud Controller
	 * @return The ServiceInstance with the given id or null if one does not exist
	 */
	GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request)
			throws ServiceInstanceDoesNotExistException;

	/**
	 * Delete and return the instance if it exists.
	 *
	 * @param request containing pertinent information for deleting the service.
	 * @return a DeleteServiceInstanceResponse
	 * @throws ServiceBrokerException is something goes wrong internally
	 * @throws ServiceBrokerAsyncRequiredException if we must use an async comparable Cloud Controller
	 * 
	 */
	DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest request)
			throws ServiceBrokerException, ServiceInstanceDoesNotExistException, ServiceBrokerAsyncRequiredException;

	/**
	 * Update a service instance. Only modification of service plan is supported.
	 *
	 * @param request detailing the request parameters
	 * 
	 * @return an UpdateServiceInstanceResponse
	 * @throws ServiceInstanceUpdateNotSupportedException if particular plan change is not supported
	 *         or if the request can not currently be fulfilled due to the state of the instance
	 * @throws ServiceInstanceDoesNotExistException if the service instance does not exist
	 * @throws ServiceBrokerException if something goes wrong internally
	 * @throws ServiceBrokerAsyncRequiredException if we must use an async comparable Cloud Controller
	 * 
	 */
	UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest request)
			throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException,
			ServiceInstanceDoesNotExistException, ServiceBrokerAsyncRequiredException;
}

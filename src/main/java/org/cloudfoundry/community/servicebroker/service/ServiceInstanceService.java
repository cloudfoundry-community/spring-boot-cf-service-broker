package org.cloudfoundry.community.servicebroker.service;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;

/**
 * This interface is implemented by service brokers to process requests related to provisioning, updating,
 * and deprovisioning service instances.
 * 
 * @author sgreenberg@pivotal.io
 * @author Scott Frederick
 */
public interface ServiceInstanceService {

	/**
	 * Create a new service instance.
	 *
	 * @param request containing the parameters from Cloud Controller
	 * @return a CreateServiceInstanceResponse
	 * @throws ServiceInstanceExistsException if a service instance with the requested ID already exists
	 * @throws ServiceBrokerAsyncRequiredException if the broker requires an async request
	 */
	CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request);

	/**
	 * Get the status of the last requested operation for a service instance.
	 *
	 * @param request containing the parameters from Cloud Controller
	 * @return The ServiceInstance with the given id or null if one does not exist
	 * @throws ServiceInstanceDoesNotExistException if a service instance with the requested ID does not exist
	 */
	GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request);

	/**
	 * Delete a service instance.
	 *
	 * @param request containing the parameters from Cloud Controller
	 * @return a DeleteServiceInstanceResponse
	 * @throws ServiceInstanceDoesNotExistException if a service instance with the requested ID does not exist
	 * @throws ServiceBrokerAsyncRequiredException if the broker requires an async request
	 */
	DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest request);

	/**
	 * Update a service instance. Only modification of the service plan is supported.
	 *
	 * @param request containing the parameters from Cloud Controller
	 * @return an UpdateServiceInstanceResponse
	 * @throws ServiceInstanceUpdateNotSupportedException if particular plan change is not supported
	 *         or if the request can not currently be fulfilled due to the state of the instance
	 * @throws ServiceInstanceDoesNotExistException if a service instance with the requested ID does not exist
	 * @throws ServiceBrokerAsyncRequiredException if the broker requires an async request
	 */
	UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest request);
}

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
	 * Create (provision) a new service instance.
	 *
	 * @param request containing the details of the request
	 * @return the details of the completed request
	 * @throws ServiceInstanceExistsException if a service instance with the given ID is already known to the broker
	 * @throws ServiceBrokerAsyncRequiredException if the broker requires asynchronous processing of the request
	 */
	CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request);

	/**
	 * Get the status of the last requested operation for a service instance.
	 *
	 * @param request containing the details of the request
	 * @return the details of the completed request
	 * @throws ServiceInstanceDoesNotExistException if a service instance with the given ID is not known to the broker
	 */
	GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request);

	/**
	 * Delete (deprovision) a service instance.
	 *
	 * @param request containing the details of the request
	 * @return the details of the completed request
	 * @throws ServiceInstanceDoesNotExistException if a service instance with the given ID is not known to the broker
	 * @throws ServiceBrokerAsyncRequiredException if the broker requires asynchronous processing of the request
	 */
	DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest request);

	/**
	 * Update a service instance. Only modification of the service plan is supported.
	 *
	 * @param request containing the details of the request
	 * @return the details of the completed request
	 * @throws ServiceInstanceUpdateNotSupportedException if particular plan change is not supported
	 *         or if the request can not currently be fulfilled due to the state of the instance
	 * @throws ServiceInstanceDoesNotExistException if a service instance with the given ID is not known to the broker
	 * @throws ServiceBrokerAsyncRequiredException if the broker requires asynchronous processing of the request
	 */
	UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest request);
}

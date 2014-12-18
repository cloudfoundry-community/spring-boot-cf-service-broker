package org.cloudfoundry.community.servicebroker.service;

import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceExistsException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;
import org.cloudfoundry.community.servicebroker.model.ServiceInstance;

/**
 * Handles instances of service definitions.
 * 
 * @author sgreenberg@gopivotal.com
 */
public interface ServiceInstanceService {

	/**
	 * Create a new instance of a service
	 * @param service The service definition of the instance to create
	 * @param serviceInstanceId The id of the instance provided by the CloudController
	 * @param planId The id of the plan for this instance
	 * @param organizationGuid The guid of the org this instance belongs to
	 * @param spaceGuid The guid of the space this instance belongs to
	 * @return The newly created ServiceInstance
	 * @throws ServiceInstanceExistsException if the service instance already exists.
	 * @throws ServiceBrokerException if something goes wrong internally
	 */
	ServiceInstance createServiceInstance(ServiceDefinition service, String serviceInstanceId, String planId,
			String organizationGuid, String spaceGuid) 
			throws ServiceInstanceExistsException, ServiceBrokerException;
	
	/**
	 * @param id The id of the serviceInstance
	 * @return The ServiceInstance with the given id or null if one does not exist
	 */
	ServiceInstance getServiceInstance(String id);
	
	/**
	 * Delete and return the instance if it exists.
	 * @param id The id of the serviceInstance
	 * @param serviceId The id of the service
     * @param planId The plan used for this binding
	 * @return The delete ServiceInstance or null if one did not exist.
	 * @throws ServiceBrokerException is something goes wrong internally
	 */
	ServiceInstance deleteServiceInstance(String id, String serviceId, String planId) throws ServiceBrokerException;

	/**
	 * Update a service instance. Only modification of service plan is supported.
	 * 
	 * @param instanceId
	 * @param planId
	 * @return null if modification succeeded
	 * @throws ServiceInstanceUpdateNotSupportedException if particular plan change is not supported
	 *         or if the request can not currently be fulfilled due to the state of the instance.
	 * @throws ServiceInstanceDoesNotExistException if the service instance does not exist
	 * @throws ServiceBrokerException if something goes wrong internally
	 * 
	 */
	ServiceInstance updateServiceInstance(String instanceId, String planId)
			throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException,
			ServiceInstanceDoesNotExistException;

}

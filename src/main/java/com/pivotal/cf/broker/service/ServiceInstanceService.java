package com.pivotal.cf.broker.service;

import java.util.List;

import com.pivotal.cf.broker.exception.ServiceBrokerException;
import com.pivotal.cf.broker.exception.ServiceInstanceExistsException;
import com.pivotal.cf.broker.model.ServiceDefinition;
import com.pivotal.cf.broker.model.ServiceInstance;

/**
 * Handles instances of service definitions.
 * 
 * @author sgreenberg@gopivotal.com
 */
public interface ServiceInstanceService {

	/**
	 * @return All known ServiceInstances
	 */
	List<ServiceInstance> getAllServiceInstances();
	
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
	 * @param id
	 * @return The ServiceInstance with the given id or null if one does not exist
	 */
	ServiceInstance getServiceInstance(String id);
	
	/**
	 * Delete and return the instance if it exists.
	 * @param id
	 * @return The delete ServiceInstance or null if one did not exist.
	 * @throws ServiceBrokerException is something goes wrong internally
	 */
	ServiceInstance deleteServiceInstance(String id) throws ServiceBrokerException;
	
}

package org.cloudfoundry.community.servicebroker.service;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;

/**
 * Handles instances of service definitions. If your broker requires async
 * provisioning, deprovisioning or updates you must call
 * <code>isAsync(true)</code> on the returned <code>ServiceInstance</code>
 * objects from those methods.
 * 
 * @author sgreenberg@gopivotal.com
 */
public interface ServiceInstanceService {

	/**
	 * Create a new instance of a service.
	 * 
	 * @param createServiceInstanceRequest
	 *            containing the parameters from CloudController, if the cloud
	 *            controller is capable of async provisioning
	 *            <code>request.hasAsyncClient()</code> will return true.
	 * @return The newly created ServiceInstance. If your broker requires an
	 *         asynchronous provisioning call <code>isAsync(true)</code> on the service
	 *         instance, otherwise synchronous provisioning is assumed.
	 * @throws ServiceInstanceExistsException
	 *             if the service instance already exists.
	 * @throws ServiceBrokerException
	 *             if something goes wrong internally
	 * @throws ServiceBrokerAsyncRequiredException
	 *             if async provisioning is required but the incoming
	 *             <code>request</code> object did not indicate an async
	 *             CloudController
	 * 
	 * @see org.cloudfoundry.community.servicebroker.model.ServiceInstance
	 * @see org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceRequest
	 */
	ServiceInstance createServiceInstance(CreateServiceInstanceRequest createServiceInstanceRequest) 
			throws ServiceInstanceExistsException, ServiceBrokerException, ServiceBrokerAsyncRequiredException;
	
	/**
	 * @param serviceInstanceId
	 *            The id of the serviceInstance
	 * @return The ServiceInstance with the given id or null if one does not
	 *         exist. During async provisioning and deprovision it is expected
	 *         that the returned <code>ServiceInstance</code> will have an up to
	 *         date <code>ServiceInstanceLastOperation</code>. This is the
	 *         method that the CloudController uses to determine if async
	 *         provisioning is complete. During an async deprovision, it is
	 *         expected this method will return null after a successful
	 *         deprovision.
	 * 
	 * @see org.cloudfoundry.community.servicebroker.model.ServiceInstance
	 * @see org.cloudfoundry.community.servicebroker.model.ServiceInstanceLastOperation
	 */
	ServiceInstance getServiceInstance(String serviceInstanceId);
	
	/**
	 * Delete and return the instance if it exists.
	 * 
	 * @param deleteServiceInstanceRequest
	 *            containing pertinent information for deleting the service.
	 * @return The deleted ServiceInstance or null if one did not exist. If your
	 *         broker requires an asynchronous deprovisioning call
	 *         <code>isAsync(true)</code> on the service instance, otherwise
	 *         synchronous provisioning is assumed.
	 * @throws ServiceBrokerException
	 *             is something goes wrong internally
	 * @throws ServiceBrokerAsyncRequiredException
	 *             if we must use an async comparable cloud controller
	 * 
	 */
	ServiceInstance deleteServiceInstance(DeleteServiceInstanceRequest deleteServiceInstanceRequest) 
			throws ServiceBrokerException, ServiceBrokerAsyncRequiredException;

	/**
	 * Update a service instance. Only modification of service plan is
	 * supported.
	 * 
	 * @param updateServiceInstanceRequest
	 *            detailing the request parameters
	 * 
	 * @return The updated serviceInstance. If your broker requires an
	 *         asynchronous updates call <code>isAsync(true)</code> on the
	 *         service instance, otherwise synchronous provisioning is assumed.
	 * @throws ServiceInstanceUpdateNotSupportedException
	 *             if particular plan change is not supported or if the request
	 *             can not currently be fulfilled due to the state of the
	 *             instance.
	 * @throws ServiceInstanceDoesNotExistException
	 *             if the service instance does not exist
	 * @throws ServiceBrokerException
	 *             if something goes wrong internally
	 * @throws ServiceBrokerAsyncRequiredException
	 *             if we must use an async comparable cloud controller
	 * 
	 */
	ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest updateServiceInstanceRequest)
			throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException,
			ServiceInstanceDoesNotExistException, ServiceBrokerAsyncRequiredException;

}

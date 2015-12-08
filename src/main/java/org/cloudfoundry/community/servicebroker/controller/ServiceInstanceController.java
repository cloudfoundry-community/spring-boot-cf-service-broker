package org.cloudfoundry.community.servicebroker.controller;

import javax.validation.Valid;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;
import org.cloudfoundry.community.servicebroker.service.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * See: http://docs.cloudfoundry.org/services/api.html
 * 
 * @author sgreenberg@gopivotal.com
 */
@Controller
@RequestMapping("/v2/service_instances")
public class ServiceInstanceController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceInstanceController.class);
	
	private ServiceInstanceService service;
	private CatalogService catalogService;
	
	@Autowired
 	public ServiceInstanceController(ServiceInstanceService service, CatalogService catalogService) {
 		this.service = service;
 		this.catalogService = catalogService;
 	}

	@RequestMapping(value = "/{instanceId}", method = RequestMethod.PUT)
	public ResponseEntity<?> createServiceInstance(
			@PathVariable("instanceId") String serviceInstanceId,
			@Valid @RequestBody CreateServiceInstanceRequest request) throws
			ServiceDefinitionDoesNotExistException,
			ServiceInstanceExistsException,
			ServiceBrokerException,
			ServiceBrokerAsyncRequiredException {
		logger.debug("createServiceInstance(): serviceInstanceId=" + serviceInstanceId);

		ServiceDefinition serviceDefinition = getServiceDefinition(request.getServiceDefinitionId());

		request.withServiceDefinition(serviceDefinition)
				.withServiceInstanceId(serviceInstanceId);

		CreateServiceInstanceResponse response = service.createServiceInstance(request);

		logger.debug("createServiceInstance(): succeeded: serviceInstanceId=" + serviceInstanceId);

		return new ResponseEntity<>(response, response.isAsync() ? HttpStatus.ACCEPTED : HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{instanceId}/last_operation", method = RequestMethod.GET)
	public ResponseEntity<?> getServiceInstanceLastOperation(@PathVariable("instanceId") String instanceId)
			throws ServiceInstanceDoesNotExistException {

		logger.debug("getServiceInstanceLastOperation(): serviceInstanceId=" + instanceId);

		GetLastServiceOperationRequest request = new GetLastServiceOperationRequest(instanceId);

		GetLastServiceOperationResponse response = service.getLastOperation(request);

		logger.debug("getServiceInstanceLastOperation(): succeeded: serviceInstanceId=" + instanceId
				+ ", state=" + response.getState()
				+ ", description=" + response.getDescription());

		return new ResponseEntity<>(response, response.isDeletionComplete() ? HttpStatus.GONE : HttpStatus.OK);
	}

	@RequestMapping(value = "/{instanceId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteServiceInstance(
			@PathVariable("instanceId") String instanceId,
			@RequestParam("service_id") String serviceId,
			@RequestParam("plan_id") String planId,
			@RequestParam(value="accepts_incomplete", required=false) boolean acceptsIncomplete)
					throws ServiceBrokerException, ServiceBrokerAsyncRequiredException {
		logger.debug("deleteServiceInstance(): "
				+ "serviceInstanceId=" + instanceId
				+ ", serviceId=" + serviceId
				+ ", planId=" + planId
				+ ", acceptsIncomplete=" + acceptsIncomplete);

		try {
			DeleteServiceInstanceRequest request =
					new DeleteServiceInstanceRequest(instanceId, serviceId, planId, acceptsIncomplete);

			DeleteServiceInstanceResponse response = service.deleteServiceInstance(request);

			logger.debug("deleteServiceInstance(): succeeded: "
					+ "serviceInstanceId=" + instanceId);

			return new ResponseEntity<>("{}", response.isAsync() ? HttpStatus.ACCEPTED : HttpStatus.OK);
		} catch (ServiceInstanceDoesNotExistException e) {
			logger.debug("deleteServiceInstance(): error: "
					+ "serviceInstanceId=" + instanceId
					+ ", exception=" + e.getMessage());

			return new ResponseEntity<>("{}", HttpStatus.GONE);
		}
	}

	@RequestMapping(value = "/{instanceId}", method = RequestMethod.PATCH)
	public ResponseEntity<String> updateServiceInstance(
			@PathVariable("instanceId") String instanceId,
			@Valid @RequestBody UpdateServiceInstanceRequest request) throws
			ServiceInstanceUpdateNotSupportedException,
			ServiceInstanceDoesNotExistException,
			ServiceDefinitionDoesNotExistException,
			ServiceBrokerException,
			ServiceBrokerAsyncRequiredException {
		logger.debug("updateServiceInstance(): "
				+ "instanceId = " + instanceId
				+ ", planId = " + request.getPlanId());

		ServiceDefinition serviceDefinition = getServiceDefinition(request.getServiceDefinitionId());

		request.withServiceDefinition(serviceDefinition).withServiceInstanceId(instanceId);

		UpdateServiceInstanceResponse response = service.updateServiceInstance(request);

		logger.debug("updateServiceInstance(): succeeded: "
				+ "serviceInstanceId=" + instanceId);

		return new ResponseEntity<>("{}", response.isAsync() ? HttpStatus.ACCEPTED : HttpStatus.OK);
	}

	private ServiceDefinition getServiceDefinition(String serviceDefinitionId)
			throws ServiceDefinitionDoesNotExistException {
		ServiceDefinition serviceDefinition = catalogService.getServiceDefinition(serviceDefinitionId);
		if (serviceDefinition == null) {
			throw new ServiceDefinitionDoesNotExistException(serviceDefinitionId);
		}
		return serviceDefinition;
	}

	@ExceptionHandler(ServiceInstanceDoesNotExistException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(ServiceInstanceDoesNotExistException ex) {
		return getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(ServiceDefinitionDoesNotExistException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(ServiceDefinitionDoesNotExistException ex) {
		return getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(ServiceInstanceExistsException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(ServiceInstanceExistsException ex) {
		return getErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ServiceInstanceUpdateNotSupportedException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(ServiceInstanceUpdateNotSupportedException ex) {
		return getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}
}

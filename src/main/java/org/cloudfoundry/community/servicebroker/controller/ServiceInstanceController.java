package org.cloudfoundry.community.servicebroker.controller;

import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerAsyncRequiredException;
import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerException;
import org.cloudfoundry.community.servicebroker.exception.ServiceDefinitionDoesNotExistException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceExistsException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceResponse;
import org.cloudfoundry.community.servicebroker.model.DeleteServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.DeleteServiceInstanceResponse;
import org.cloudfoundry.community.servicebroker.model.ErrorMessage;
import org.cloudfoundry.community.servicebroker.model.GetLastServiceOperationRequest;
import org.cloudfoundry.community.servicebroker.model.GetLastServiceOperationResponse;
import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;
import org.cloudfoundry.community.servicebroker.model.UpdateServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.UpdateServiceInstanceResponse;
import org.cloudfoundry.community.servicebroker.service.CatalogService;
import org.cloudfoundry.community.servicebroker.service.ServiceInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * See: http://docs.cloudfoundry.org/services/api.html
 * 
 * @author sgreenberg@gopivotal.com
 */
@RestController
@RequestMapping("/v2/service_instances/{instanceId}")
public class ServiceInstanceController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceInstanceController.class);
	
	private ServiceInstanceService service;

	@Autowired
 	public ServiceInstanceController(CatalogService catalogService, ServiceInstanceService serviceInstanceService) {
		super(catalogService);
		this.service = serviceInstanceService;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> createServiceInstance(@PathVariable("instanceId") String serviceInstanceId,
												   @Valid @RequestBody CreateServiceInstanceRequest request) {
		logger.debug("createServiceInstance(): serviceInstanceId=" + serviceInstanceId);

		ServiceDefinition serviceDefinition = getServiceDefinition(request.getServiceDefinitionId());

		request.withServiceDefinition(serviceDefinition)
				.withServiceInstanceId(serviceInstanceId);

		CreateServiceInstanceResponse response = service.createServiceInstance(request);

		logger.debug("createServiceInstance(): succeeded: serviceInstanceId=" + serviceInstanceId);

		return new ResponseEntity<>(response, response.isAsync() ? HttpStatus.ACCEPTED : HttpStatus.CREATED);
	}

	@RequestMapping(value = "/last_operation", method = RequestMethod.GET)
	public ResponseEntity<?> getServiceInstanceLastOperation(@PathVariable("instanceId") String serviceInstanceId) {

		logger.debug("getServiceInstanceLastOperation(): serviceInstanceId=" + serviceInstanceId);

		GetLastServiceOperationRequest request = new GetLastServiceOperationRequest(serviceInstanceId);

		GetLastServiceOperationResponse response = service.getLastOperation(request);

		logger.debug("getServiceInstanceLastOperation(): succeeded: serviceInstanceId=" + serviceInstanceId
				+ ", state=" + response.getState()
				+ ", description=" + response.getDescription());

		return new ResponseEntity<>(response, response.isDeletionComplete() ? HttpStatus.GONE : HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteServiceInstance(@PathVariable("instanceId") String serviceInstanceId,
												   @RequestParam("service_id") String serviceDefinitionId,
												   @RequestParam("plan_id") String planId,
												   @RequestParam(value = "accepts_incomplete", required = false) boolean acceptsIncomplete) {
		logger.debug("deleteServiceInstance(): "
				+ "serviceInstanceId=" + serviceInstanceId
				+ ", serviceDefinitionId=" + serviceDefinitionId
				+ ", planId=" + planId
				+ ", acceptsIncomplete=" + acceptsIncomplete);

		try {
			DeleteServiceInstanceRequest request =
					new DeleteServiceInstanceRequest(serviceInstanceId, serviceDefinitionId, planId, getServiceDefinition(serviceDefinitionId),
							acceptsIncomplete);

			DeleteServiceInstanceResponse response = service.deleteServiceInstance(request);

			logger.debug("deleteServiceInstance(): succeeded: "
					+ "serviceInstanceId=" + serviceInstanceId);

			return new ResponseEntity<>("{}", response.isAsync() ? HttpStatus.ACCEPTED : HttpStatus.OK);
		} catch (ServiceInstanceDoesNotExistException e) {
			logger.debug("deleteServiceInstance(): error: "
					+ "serviceInstanceId=" + serviceInstanceId
					+ ", exception=" + e.getMessage());

			return new ResponseEntity<>("{}", HttpStatus.GONE);
		}
	}

	@RequestMapping(method = RequestMethod.PATCH)
	public ResponseEntity<String> updateServiceInstance(@PathVariable("instanceId") String serviceInstanceId,
														@Valid @RequestBody UpdateServiceInstanceRequest request) {
		logger.debug("updateServiceInstance(): "
				+ "serviceInstanceId = " + serviceInstanceId
				+ ", planId = " + request.getPlanId());

		ServiceDefinition serviceDefinition = getServiceDefinition(request.getServiceDefinitionId());

		request.withServiceDefinition(serviceDefinition).withServiceInstanceId(serviceInstanceId);

		UpdateServiceInstanceResponse response = service.updateServiceInstance(request);

		logger.debug("updateServiceInstance(): succeeded: "
				+ "serviceInstanceId=" + serviceInstanceId);

		return new ResponseEntity<>("{}", response.isAsync() ? HttpStatus.ACCEPTED : HttpStatus.OK);
	}

	@ExceptionHandler(ServiceInstanceExistsException.class)
	public ResponseEntity<ErrorMessage> handleException(ServiceInstanceExistsException ex) {
		return getErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ServiceInstanceUpdateNotSupportedException.class)
	public ResponseEntity<ErrorMessage> handleException(ServiceInstanceUpdateNotSupportedException ex) {
		return getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}
}

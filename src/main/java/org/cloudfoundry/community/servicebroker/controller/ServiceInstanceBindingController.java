package org.cloudfoundry.community.servicebroker.controller;

import javax.validation.Valid;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;
import org.cloudfoundry.community.servicebroker.service.CatalogService;
import org.cloudfoundry.community.servicebroker.service.ServiceInstanceBindingService;
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

/**
 * See: Source: http://docs.cloudfoundry.com/docs/running/architecture/services/writing-service.html
 * 
 * @author sgreenberg@gopivotal.com
 */
@RestController
@RequestMapping("/v2/service_instances/{instanceId}/service_bindings/{bindingId}")
public class ServiceInstanceBindingController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceInstanceBindingController.class);
	
	private ServiceInstanceBindingService serviceInstanceBindingService;

	@Autowired
	public ServiceInstanceBindingController(CatalogService catalogService,
											ServiceInstanceBindingService serviceInstanceBindingService) {
		super(catalogService);
		this.serviceInstanceBindingService = serviceInstanceBindingService;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> createServiceInstanceBinding(
			@PathVariable("instanceId") String serviceInstanceId,
			@PathVariable("bindingId") String bindingId,
			@Valid @RequestBody CreateServiceInstanceBindingRequest request)
			throws ServiceInstanceDoesNotExistException, ServiceInstanceBindingExistsException,
			ServiceBrokerException, ServiceDefinitionDoesNotExistException {
		logger.debug("createServiceInstanceBinding(): "
				+ "serviceInstanceId=" + serviceInstanceId
				+ ", bindingId=" + bindingId);

		request.withServiceInstanceId(serviceInstanceId)
				.withBindingId(bindingId)
				.withServiceDefinition(getServiceDefinition(request.getServiceDefinitionId()));

		CreateServiceInstanceBindingResponse response = serviceInstanceBindingService.createServiceInstanceBinding(request);

		logger.debug("createServiceInstanceBinding(): succeeded: "
				+ "serviceInstanceId=" + serviceInstanceId
				+ "bindingId=" + bindingId);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteServiceInstanceBinding(
			@PathVariable("instanceId") String serviceInstanceId,
			@PathVariable("bindingId") String bindingId,
			@RequestParam("service_id") String serviceDefinitionId,
			@RequestParam("plan_id") String planId)
			throws ServiceBrokerException, ServiceInstanceDoesNotExistException,
			ServiceBrokerAsyncRequiredException, ServiceDefinitionDoesNotExistException {
		logger.debug("deleteServiceInstanceBinding(): "
				+ "serviceInstanceId=" + serviceInstanceId
				+ ", bindingId=" + bindingId
				+ ", serviceDefinitionId=" + serviceDefinitionId
				+ ", planId=" + planId);

		try {
			DeleteServiceInstanceBindingRequest request =
					new DeleteServiceInstanceBindingRequest(serviceInstanceId, bindingId, serviceDefinitionId, planId,
							getServiceDefinition(serviceDefinitionId));

			serviceInstanceBindingService.deleteServiceInstanceBinding(request);
		} catch (ServiceInstanceBindingDoesNotExistException e) {
			return new ResponseEntity<>("{}", HttpStatus.GONE);
		}

		logger.debug("deleteServiceInstanceBinding(): succeeded: bindingId=" + bindingId);

		return new ResponseEntity<>("{}", HttpStatus.OK);
	}

	@ExceptionHandler(ServiceInstanceBindingExistsException.class)
	public ResponseEntity<ErrorMessage> handleException(ServiceInstanceBindingExistsException ex) {
		return getErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
	}
}

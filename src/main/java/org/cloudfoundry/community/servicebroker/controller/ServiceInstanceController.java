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
 * See: http://docs.cloudfoundry.com/docs/running/architecture/services/writing-service.html
 * 
 * @author sgreenberg@gopivotal.com
 */
@Controller
public class ServiceInstanceController extends BaseController {

	public static final String BASE_PATH = "/v2/service_instances";
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceInstanceController.class);
	
	private ServiceInstanceService service;
	private CatalogService catalogService;
	
	@Autowired
 	public ServiceInstanceController(ServiceInstanceService service, CatalogService catalogService) {
 		this.service = service;
 		this.catalogService = catalogService;
 	}
	
	@RequestMapping(value = BASE_PATH + "/{instanceId}", method = RequestMethod.PUT)
	public ResponseEntity<ServiceInstance> createServiceInstance(
			@PathVariable("instanceId") String serviceInstanceId, 
			@RequestParam(value="accepts_incomplete", required=false) boolean acceptsIncomplete,
			@Valid @RequestBody CreateServiceInstanceRequest request) throws
			ServiceDefinitionDoesNotExistException,
			ServiceInstanceExistsException,
			ServiceBrokerException, ServiceBrokerAsyncRequiredException {
		logger.debug("PUT: " + BASE_PATH + "/{instanceId}?accepts_incomplete=" + acceptsIncomplete 
				+ ", createServiceInstance(), serviceInstanceId = " + serviceInstanceId);
		ServiceDefinition svc = catalogService.getServiceDefinition(request.getServiceDefinitionId());
		if (svc == null) {
			throw new ServiceDefinitionDoesNotExistException(request.getServiceDefinitionId());
		}
		ServiceInstance instance = service.createServiceInstance(
				request.withServiceDefinition(svc).and().withServiceInstanceId(serviceInstanceId)
					.and().withAcceptsIncomplete(acceptsIncomplete));
		logger.debug("ServiceInstance Created: " + instance.getServiceInstanceId());

		return new ResponseEntity<>(
				instance, instance.isAsync() ? HttpStatus.ACCEPTED : HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = BASE_PATH + "/{instanceId}/last_operation", method = RequestMethod.GET)
	public ResponseEntity<?> getServiceInstanceLastOperation(
			@PathVariable("instanceId") String instanceId) {

		logger.debug("GET: " + BASE_PATH + "/{instanceId}/last_operation"
				+ ", getServiceInstance(), serviceInstanceId = " + instanceId);

		ServiceInstance instance = service.getServiceInstance(instanceId);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (null == instance) {
			return new ResponseEntity<>("{}", headers, HttpStatus.GONE);
		}
		ServiceInstanceLastOperation lastOperation = instance.getServiceInstanceLastOperation();
		logger.debug("ServiceInstance: " + instance.getServiceInstanceId() + "is in " + lastOperation.getState() + " state. Details : " +lastOperation.getDescription());
		return new ResponseEntity<>(lastOperation, headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = BASE_PATH + "/{instanceId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteServiceInstance(
			@PathVariable("instanceId") String instanceId, 
			@RequestParam("service_id") String serviceId,
			@RequestParam("plan_id") String planId,
			@RequestParam(value="accepts_incomplete", required=false) boolean acceptsIncomplete) 
					throws ServiceBrokerException, ServiceBrokerAsyncRequiredException {
		logger.debug( "DELETE: " + BASE_PATH + "/{instanceId}?accepts_incomplete=" + acceptsIncomplete 
				+ ", deleteServiceInstanceBinding(), serviceInstanceId = " + instanceId 
				+ ", serviceId = " + serviceId
				+ ", planId = " + planId);
		ServiceInstance instance = service.deleteServiceInstance(
				new DeleteServiceInstanceRequest(instanceId, serviceId, planId, acceptsIncomplete));
		
		if (instance == null) {
			return new ResponseEntity<>("{}", HttpStatus.GONE);
		}
		
		logger.debug("ServiceInstance Deleted: " + instance.getServiceInstanceId());
		return new ResponseEntity<>(instance,
				instance.isAsync() ? HttpStatus.ACCEPTED : HttpStatus.OK);
	}
	
	@RequestMapping(value = BASE_PATH + "/{instanceId}", method = RequestMethod.PATCH)
	public ResponseEntity<String> updateServiceInstance(
			@PathVariable("instanceId") String instanceId,
			@RequestParam(value="accepts_incomplete", required=false) boolean acceptsIncomplete,
			@Valid @RequestBody UpdateServiceInstanceRequest request) throws 
			ServiceInstanceUpdateNotSupportedException,
			ServiceInstanceDoesNotExistException, 
			ServiceBrokerException, ServiceBrokerAsyncRequiredException {
		logger.debug("UPDATE: " + BASE_PATH + "/{instanceId}?accepts_incomplete=" + acceptsIncomplete
				+ ", updateServiceInstanceBinding(), serviceInstanceId = "
				+ instanceId + ", instanceId = " + instanceId + ", planId = "
				+ request.getPlanId());
		ServiceInstance instance = service.updateServiceInstance(
				request.withInstanceId(instanceId).withAcceptsIncomplete(acceptsIncomplete));
		logger.debug("ServiceInstance updated: " + instance.getServiceInstanceId());
		HttpStatus status = instance.isAsync() ? HttpStatus.ACCEPTED : HttpStatus.OK; 
		return new ResponseEntity<>("{}", status);
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

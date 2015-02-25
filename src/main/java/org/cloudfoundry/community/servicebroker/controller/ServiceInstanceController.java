package org.cloudfoundry.community.servicebroker.controller;

import javax.servlet.http.HttpServletResponse;
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
	public ResponseEntity<CreateServiceInstanceResponse> createServiceInstance(
			@PathVariable("instanceId") String serviceInstanceId, 
			@Valid @RequestBody CreateServiceInstanceRequest request) throws
			ServiceDefinitionDoesNotExistException,
			ServiceInstanceExistsException,
			ServiceBrokerException {
		logger.debug("PUT: " + BASE_PATH + "/{instanceId}" 
				+ ", createServiceInstance(), serviceInstanceId = " + serviceInstanceId);
		ServiceDefinition svc = catalogService.getServiceDefinition(request.getServiceDefinitionId());
		if (svc == null) {
			throw new ServiceDefinitionDoesNotExistException(request.getServiceDefinitionId());
		}
		ServiceInstance instance = service.createServiceInstance(
				request.withServiceDefinition(svc).and().withServiceInstanceId(serviceInstanceId));
		logger.debug("ServiceInstance Created: " + instance.getServiceInstanceId());
        return new ResponseEntity<CreateServiceInstanceResponse>(
        		new CreateServiceInstanceResponse(instance), 
        		HttpStatus.CREATED);
	}
	
	@RequestMapping(value = BASE_PATH + "/{instanceId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteServiceInstance(
			@PathVariable("instanceId") String instanceId, 
			@RequestParam("service_id") String serviceId,
			@RequestParam("plan_id") String planId) throws ServiceBrokerException {
		logger.debug( "DELETE: " + BASE_PATH + "/{instanceId}" 
				+ ", deleteServiceInstanceBinding(), serviceInstanceId = " + instanceId 
				+ ", serviceId = " + serviceId
				+ ", planId = " + planId);
		ServiceInstance instance = service.deleteServiceInstance(
				new DeleteServiceInstanceRequest(instanceId, serviceId, planId));
		if (instance == null) {
			return new ResponseEntity<String>("{}", HttpStatus.GONE);
		}
		logger.debug("ServiceInstance Deleted: " + instance.getServiceInstanceId());
        return new ResponseEntity<String>("{}", HttpStatus.OK);
	}
	
	@RequestMapping(value = BASE_PATH + "/{instanceId}", method = RequestMethod.PATCH)
	public ResponseEntity<String> updateServiceInstance(
			@PathVariable("instanceId") String instanceId,
			@Valid @RequestBody UpdateServiceInstanceRequest request) throws 
			ServiceInstanceUpdateNotSupportedException,
			ServiceInstanceDoesNotExistException, 
			ServiceBrokerException {
		logger.debug("UPDATE: " + BASE_PATH + "/{instanceId}"
				+ ", updateServiceInstanceBinding(), serviceInstanceId = "
				+ instanceId + ", instanceId = " + instanceId + ", planId = "
				+ request.getPlanId());
		ServiceInstance instance = service.updateServiceInstance(request.withInstanceId(instanceId));
		logger.debug("ServiceInstance updated: " + instance.getServiceInstanceId());
		return new ResponseEntity<String>("{}", HttpStatus.OK);
	}

	
	@ExceptionHandler(ServiceDefinitionDoesNotExistException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(
			ServiceDefinitionDoesNotExistException ex, 
			HttpServletResponse response) {
	    return getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(ServiceInstanceExistsException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(
			ServiceInstanceExistsException ex, 
			HttpServletResponse response) {
	    return getErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ServiceInstanceUpdateNotSupportedException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(
			ServiceInstanceUpdateNotSupportedException ex,
			HttpServletResponse response) {
		return getErrorResponse(ex.getMessage(),
				HttpStatus.UNPROCESSABLE_ENTITY);
	}

}

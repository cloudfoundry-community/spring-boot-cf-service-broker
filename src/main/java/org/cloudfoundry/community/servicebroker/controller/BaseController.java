package org.cloudfoundry.community.servicebroker.controller;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;
import org.cloudfoundry.community.servicebroker.service.CatalogService;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

/**
 * Base controller.
 *
 * @author sgreenberg@gopivotal.com
 */
public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	protected CatalogService catalogService;

	public BaseController(CatalogService catalogService) {
		this.catalogService = catalogService;
	}

	protected ServiceDefinition getServiceDefinition(String serviceDefinitionId)
			throws ServiceDefinitionDoesNotExistException {
		ServiceDefinition serviceDefinition = catalogService.getServiceDefinition(serviceDefinitionId);
		if (serviceDefinition == null) {
			throw new ServiceDefinitionDoesNotExistException(serviceDefinitionId);
		}
		return serviceDefinition;
	}

	@ExceptionHandler(ServiceBrokerApiVersionException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(ServiceBrokerApiVersionException ex) {
		return getErrorResponse(ex.getMessage(), HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(HttpMessageNotReadableException ex) {
		return getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		String message = "Missing required fields:";
		for (FieldError error : result.getFieldErrors()) {
			message += " " + error.getField();
		}
		return getErrorResponse(message, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(ServiceBrokerAsyncRequiredException.class)
	@ResponseBody
	public ResponseEntity<AsyncRequiredErrorMessage> handleException(ServiceBrokerAsyncRequiredException ex) {
		return new ResponseEntity<>(
				new AsyncRequiredErrorMessage(ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(Exception ex) {
		logger.warn("Exception", ex);
		return getErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<ErrorMessage> getErrorResponse(String message, HttpStatus status) {
		return new ResponseEntity<>(new ErrorMessage(message), status);
	}
}

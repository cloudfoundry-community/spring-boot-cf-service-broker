package org.cloudfoundry.community.servicebroker.controller;

import lombok.extern.slf4j.Slf4j;
import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerApiVersionException;
import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerAsyncRequiredException;
import org.cloudfoundry.community.servicebroker.exception.ServiceDefinitionDoesNotExistException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.cloudfoundry.community.servicebroker.model.AsyncRequiredErrorMessage;
import org.cloudfoundry.community.servicebroker.model.ErrorMessage;
import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;
import org.cloudfoundry.community.servicebroker.service.CatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Base controller.
 *
 * @author sgreenberg@pivotal.io
 * @author Scott Frederick
 */
@Slf4j
public class BaseController {

	protected CatalogService catalogService;

	public BaseController(CatalogService catalogService) {
		this.catalogService = catalogService;
	}

	protected ServiceDefinition getServiceDefinition(String serviceDefinitionId) {
		ServiceDefinition serviceDefinition = catalogService.getServiceDefinition(serviceDefinitionId);
		if (serviceDefinition == null) {
			throw new ServiceDefinitionDoesNotExistException(serviceDefinitionId);
		}
		return serviceDefinition;
	}

	@ExceptionHandler(ServiceBrokerApiVersionException.class)
	public ResponseEntity<ErrorMessage> handleException(ServiceBrokerApiVersionException ex) {
		log.debug("Unsupported service broker API version: ", ex);
		return getErrorResponse(ex.getMessage(), HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(ServiceInstanceDoesNotExistException.class)
	public ResponseEntity<ErrorMessage> handleException(ServiceInstanceDoesNotExistException ex) {
		log.debug("Service instance does not exist: ", ex);
		return getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(ServiceDefinitionDoesNotExistException.class)
	public ResponseEntity<ErrorMessage> handleException(ServiceDefinitionDoesNotExistException ex) {
		log.debug("Service definition does not exist: ", ex);
		return getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorMessage> handleException(HttpMessageNotReadableException ex) {
		log.debug("Unprocessable request received: ", ex);
		return getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> handleException(MethodArgumentNotValidException ex) {
		log.debug("Unprocessable request received: ", ex);
		BindingResult result = ex.getBindingResult();
		String message = "Missing required fields:";
		for (FieldError error : result.getFieldErrors()) {
			message += " " + error.getField();
		}
		return getErrorResponse(message, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(ServiceBrokerAsyncRequiredException.class)
	public ResponseEntity<AsyncRequiredErrorMessage> handleException(ServiceBrokerAsyncRequiredException ex) {
		log.debug("Broker requires async support: ", ex);
		return new ResponseEntity<>(
				new AsyncRequiredErrorMessage(ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleException(Exception ex) {
		log.debug("Unknown exception handled: ", ex);
		return getErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<ErrorMessage> getErrorResponse(String message, HttpStatus status) {
		return new ResponseEntity<>(new ErrorMessage(message), status);
	}
}

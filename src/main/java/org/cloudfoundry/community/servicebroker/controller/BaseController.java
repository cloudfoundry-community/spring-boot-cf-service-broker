package org.cloudfoundry.community.servicebroker.controller;

import javax.servlet.http.HttpServletResponse;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;
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
 *
 */
public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);
		
	@ExceptionHandler(ServiceBrokerApiVersionException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(
			ServiceBrokerApiVersionException ex, 
			HttpServletResponse response) {
	    return getErrorResponse(ex.getMessage(), HttpStatus.PRECONDITION_FAILED);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(
			HttpMessageNotReadableException ex, 
			HttpServletResponse response) {
	    return getErrorResponse(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(
			MethodArgumentNotValidException ex, 
			HttpServletResponse response) {
	    BindingResult result = ex.getBindingResult();
	    String message = "Missing required fields:";
	    for (FieldError error: result.getFieldErrors()) {
	    	message += " " + error.getField();
	    }
		return getErrorResponse(message, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<ErrorMessage> handleException(
			Exception ex, 
			HttpServletResponse response) {
		logger.warn("Exception", ex);
	    return getErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ServiceBrokerAsyncRequiredException.class)
	public ResponseEntity<AsyncRequiredErrorMessage> handleException(
			ServiceBrokerAsyncRequiredException ex, 
			HttpServletResponse response) {
		return new ResponseEntity<AsyncRequiredErrorMessage>(
				new AsyncRequiredErrorMessage(ex.getDescription()), HttpStatus.UNPROCESSABLE_ENTITY);
		
	}
	
	public ResponseEntity<ErrorMessage> getErrorResponse(String message, HttpStatus status) {
		return new ResponseEntity<ErrorMessage>(new ErrorMessage(message), 
				status);
	}
	
}

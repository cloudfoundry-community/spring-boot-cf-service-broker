package com.pivotal.cf.broker.exception;

/**
 * General exception for underlying broker errors (like connectivity to the service
 * being brokered).
 * 
 * @author sgreenberg@gopivotal.com
 *
 */
public class ServiceBrokerException extends Exception {
	
	private static final long serialVersionUID = -5544859893499349135L;
	private String message;
	
	public ServiceBrokerException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
}
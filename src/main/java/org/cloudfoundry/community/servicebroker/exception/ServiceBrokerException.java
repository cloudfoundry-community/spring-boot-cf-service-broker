package org.cloudfoundry.community.servicebroker.exception;

/**
 * General exception for underlying broker errors (like connectivity to the service
 * being brokered).
 * 
 * @author sgreenberg@gopivotal.com
 *
 */
public class ServiceBrokerException extends Exception {

	private static final long serialVersionUID = -5544859893499349135L;

	public ServiceBrokerException(String message) {
		super(message);
	}

	public ServiceBrokerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceBrokerException(Throwable cause) {
		super(cause);
	}

}
package org.cloudfoundry.community.servicebroker.exception;

/**
 * May be thrown if the particular plan change requested is not supported or if the request 
 * can not currently be fulfilled due to the state of the instance
 */
public class ServiceInstanceUpdateNotSupportedException extends Exception {

	private static final long serialVersionUID = 4719676639792071582L;

	public ServiceInstanceUpdateNotSupportedException(String message) {
		super(message);
	}

	public ServiceInstanceUpdateNotSupportedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceInstanceUpdateNotSupportedException(Throwable cause) {
		super(cause);
	}

}

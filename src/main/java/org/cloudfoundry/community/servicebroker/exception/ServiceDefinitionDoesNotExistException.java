package org.cloudfoundry.community.servicebroker.exception;

/**
 * Thrown to indicate that a request includes a service definition ID does not exist.
 * 
 * @author sgreenberg@gopivotal.com
 */
public class ServiceDefinitionDoesNotExistException extends RuntimeException {
	
	private static final long serialVersionUID = -62090827040416788L;

	public ServiceDefinitionDoesNotExistException(String serviceDefinitionId) {
		super("Service definition does not exist: id=" + serviceDefinitionId);
	}
	
}
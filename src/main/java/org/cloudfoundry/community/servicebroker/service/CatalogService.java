package org.cloudfoundry.community.servicebroker.service;

import org.cloudfoundry.community.servicebroker.model.Catalog;
import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;

/**
 * Handles the catalog of services made available by this 
 * broker.
 * 
 * @author sgreenberg@gopivotal.com
 */
public interface CatalogService {

	/**
	 * @return The catalog of services provided by this broker.
	 */
	Catalog getCatalog();

	/**
	 * @param serviceId  The id of the service in the catalog
	 * @return The service definition or null if it doesn't exist
	 */
	ServiceDefinition getServiceDefinition(String serviceId);
	
}

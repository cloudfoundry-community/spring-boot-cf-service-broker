package org.cloudfoundry.community.servicebroker.service;

import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.community.servicebroker.model.Catalog;
import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * An implementation of the CatalogService that gets the catalog injected (ie configure 
 * in spring config)
 * 
 * @author sgreenberg@gopivotal.com
 *
 */
public class BeanCatalogService implements CatalogService {

	private Catalog catalog;
	private Map<String,ServiceDefinition> serviceDefs = new HashMap<String,ServiceDefinition>();
	
	@Autowired
	public BeanCatalogService(Catalog catalog) {
		this.catalog = catalog;
		initializeMap();
	}
	
	private void initializeMap() {
		for (ServiceDefinition def: catalog.getServiceDefinitions()) {
			serviceDefs.put(def.getId(), def);
		}
	}
	
	@Override
	public Catalog getCatalog() {
		return catalog;
	}

	@Override
	public ServiceDefinition getServiceDefinition(String serviceId) {
		return serviceDefs.get(serviceId);
	}

}

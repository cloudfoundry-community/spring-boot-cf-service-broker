package org.cloudfoundry.community.servicebroker.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The catalog of services offered by this broker.
 * 
 * @author sgreenberg@gopivotal.com
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Catalog {

	@NotEmpty
	@JsonSerialize
	@JsonProperty("services")
	private List<ServiceDefinition> serviceDefinitions = new ArrayList<ServiceDefinition>();

	public Catalog() {
	}

	public Catalog(List<ServiceDefinition> serviceDefinitions) {
		this.setServiceDefinitions(serviceDefinitions); 
	}
	
	public List<ServiceDefinition> getServiceDefinitions() {
		return serviceDefinitions;
	}

	private void setServiceDefinitions(List<ServiceDefinition> serviceDefinitions) {
		if ( serviceDefinitions == null ) {
			// ensure serialization as an empty array, not null
			this.serviceDefinitions = new ArrayList<ServiceDefinition>();
		} else {
			this.serviceDefinitions = serviceDefinitions;
		} 
	}
	
}

package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.*;

public abstract class ServiceInstanceRequest {

	@JsonIgnore
	protected boolean acceptsIncomplete;
	
	public ServiceInstanceRequest(boolean acceptsIncomplete) {
		this.acceptsIncomplete = acceptsIncomplete;
	}

	public boolean hasAsyncClient() { 
		return acceptsIncomplete;
	}
	
}

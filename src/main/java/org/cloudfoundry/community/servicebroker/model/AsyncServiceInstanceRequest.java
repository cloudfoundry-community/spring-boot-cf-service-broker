package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public abstract class AsyncServiceInstanceRequest {

	@JsonSerialize
	@JsonProperty("accepts_incomplete")
	protected boolean async;
	
	public AsyncServiceInstanceRequest(boolean async) {
		this.async = async;
	}

	public boolean isAsync() {
		return async;
	}
}

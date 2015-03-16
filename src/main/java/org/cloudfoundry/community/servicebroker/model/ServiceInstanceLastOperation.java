package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonAutoDetect
public class ServiceInstanceLastOperation {
	
	@JsonSerialize
	@JsonProperty("dashboard_url")
	private String dashboarUrl; 
	
	@JsonSerialize
	private String description;
	
	private OperationState state; 
	
	public ServiceInstanceLastOperation(
			final String description, 
			final OperationState operationState)  { 
		setDescription(description); 
		setState(operationState);
	}
	
	public String getDescription() {
		return description;
	}
	
	private void setDescription(String description) {
		this.description = description;
	}
	
	@JsonSerialize
	public String getState() {
		switch (state) { 
		case IN_PROGRESS: 
			return "in progress";
		case SUCCEDED: 
			return "succeeded"; 
		case FAILED: 
			return "failed"; 
		};
		assert(false);
		return "internal error";
	}

	private void setState(OperationState state) {
		this.state = state;
	} 	
}

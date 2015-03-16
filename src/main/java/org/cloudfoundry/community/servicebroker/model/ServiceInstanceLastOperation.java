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
		String s = null;
		switch (state) { 
		case IN_PROGRESS: 
			s = "in progress";
			break;
		case SUCCEDED: 
			s = "succeded"; 
			break; 
		case FAILED: 
			s = "failed"; 
			break; 
		default: 
			assert(false);
			break;
		};
		return s;
	}

	private void setState(OperationState state) {
		this.state = state;
	} 	
}

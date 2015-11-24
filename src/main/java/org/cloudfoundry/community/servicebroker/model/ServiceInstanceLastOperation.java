package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonAutoDetect
public class ServiceInstanceLastOperation {
	
	@JsonSerialize
	private String description;
	
	private OperationState state; 
	
	public ServiceInstanceLastOperation() {
	}
	
	public ServiceInstanceLastOperation(
			final String description, 
			final OperationState operationState)  { 
		setDescription(description); 
		this.state = operationState;
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
		case SUCCEEDED:
			return "succeeded"; 
		case FAILED: 
			return "failed";
		};
		assert(false);
		return "internal error";
	}
	
	@JsonSerialize
	private void setState(String state) { 
		switch(state) { 
		case "in progress": 
			this.state = OperationState.IN_PROGRESS;
			break;
		case "succeeded": 
			this.state = OperationState.SUCCEEDED; 
			break; 
		case "failed":
			this.state = OperationState.FAILED;
			break; 
		default:
			assert(false);
			break;
		}
	}
}

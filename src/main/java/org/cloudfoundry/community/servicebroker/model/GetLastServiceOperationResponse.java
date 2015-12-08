package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonAutoDetect
public class GetLastServiceOperationResponse {
	@JsonSerialize
	private OperationState state;

	@JsonSerialize
	private String description;

	private boolean deletionComplete;

	public GetLastServiceOperationResponse() {
	}
	
	public GetLastServiceOperationResponse(final OperationState operationState, final String description, boolean deletionComplete)  {
		this.state = operationState;
		this.description = description;
		this.deletionComplete = deletionComplete;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getState() {
		return state.getValue();
	}

	public boolean isDeletionComplete() {
		return deletionComplete;
	}
}

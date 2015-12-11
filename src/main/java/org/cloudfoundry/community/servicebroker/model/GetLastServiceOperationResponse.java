package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Details of a response to a request to get the state of the last operation on a service instance.
 *
 * @author Scott Frederick
 */
@Getter
@ToString
@EqualsAndHashCode
@JsonAutoDetect
public class GetLastServiceOperationResponse {
	@JsonSerialize
	private final OperationState state;

	@JsonSerialize
	private final String description;

	private final boolean deletionComplete;

	public GetLastServiceOperationResponse(final OperationState operationState, final String description, boolean deletionComplete)  {
		this.state = operationState;
		this.description = description;
		this.deletionComplete = deletionComplete;
	}
	
	public String getState() {
		return state.getValue();
	}
}

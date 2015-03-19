package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * This class is used to indicate the status of the last action CloudController
 * asked the broker to perform for a given service instance. The cloud
 * controller understands three states: failed, in progress, and succeeded. the
 * <code>description</code> is user facing.
 * 
 * @author jkruck
 *
 */
@JsonAutoDetect
public class ServiceInstanceLastOperation {

	@JsonSerialize
	@JsonProperty("dashboard_url")
	private String dashboarUrl;

	@JsonSerialize
	private String description;

	private OperationState state;

	public ServiceInstanceLastOperation() {
	}

	/**
	 * Create a new last operation state
	 * 
	 * @param description
	 *            user facing text describing the operation
	 * @param operationState
	 *            reflecting the current phase of the operation life cycle.
	 */
	public ServiceInstanceLastOperation(final String description,
			final OperationState operationState) {
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
		}
		;
		assert (false);
		return "internal error";
	}

	@JsonSerialize
	private void setState(String state) {
		switch (state) {
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
			assert (false);
			break;
		}
	}
}

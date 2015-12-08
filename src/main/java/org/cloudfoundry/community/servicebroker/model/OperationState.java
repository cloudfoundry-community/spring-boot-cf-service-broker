package org.cloudfoundry.community.servicebroker.model;

public enum OperationState {
	IN_PROGRESS("in progress"),
	SUCCEEDED("succeeded"),
	FAILED("failed");

	private final String state;

	OperationState(String state) {
		this.state = state;
	}

	public String getValue() {
		return state;
	}
}

package org.cloudfoundry.community.servicebroker.model;

public abstract class AsyncServiceInstanceResponse {
	protected boolean async;

	public AsyncServiceInstanceResponse(boolean async) {
		this.async = async;
	}

	public boolean isAsync() {
		return async;
	}
}

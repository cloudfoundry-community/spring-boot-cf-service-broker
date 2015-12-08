package org.cloudfoundry.community.servicebroker.model;

import java.util.Objects;

public class GetLastServiceOperationRequest {
	private final String serviceInstanceId;

	public GetLastServiceOperationRequest(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GetLastServiceOperationRequest that = (GetLastServiceOperationRequest) o;
		return Objects.equals(serviceInstanceId, that.serviceInstanceId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceInstanceId);
	}
}

package org.cloudfoundry.community.servicebroker.model;

import java.util.Objects;

/**
 *  A request sent by the cloud controller to remove a binding of a service.
 *
 * @author krujos
 */
public class DeleteServiceInstanceBindingRequest {

	private final String serviceInstanceId;
	private final String bindingId;
	private final String serviceDefinitionId;
	private final String planId;
	private final ServiceDefinition serviceDefinition;

	public DeleteServiceInstanceBindingRequest(String serviceInstanceId, String bindingId,
											   String serviceDefinitionId, String planId,
											   ServiceDefinition serviceDefinition) {
		this.serviceInstanceId = serviceInstanceId;
		this.bindingId = bindingId;
		this.serviceDefinitionId = serviceDefinitionId;
		this.planId = planId;
		this.serviceDefinition = serviceDefinition;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public String getBindingId() {
		return bindingId;
	}

	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}

	public String getPlanId() {
		return planId;
	}

	public ServiceDefinition getServiceDefinition() {
		return serviceDefinition;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DeleteServiceInstanceBindingRequest that = (DeleteServiceInstanceBindingRequest) o;
		return Objects.equals(serviceInstanceId, that.serviceInstanceId) &&
				Objects.equals(bindingId, that.bindingId) &&
				Objects.equals(serviceDefinitionId, that.serviceDefinitionId) &&
				Objects.equals(planId, that.planId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceInstanceId, bindingId, serviceDefinitionId, planId);
	}
}

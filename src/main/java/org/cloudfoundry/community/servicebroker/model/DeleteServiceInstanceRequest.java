package org.cloudfoundry.community.servicebroker.model;

import java.util.Objects;

/**
 * A request sent by the cloud controller to remove a service.
 *
 * @author krujos
 */
public class DeleteServiceInstanceRequest extends AsyncServiceInstanceRequest {

	private final String serviceInstanceId;
	private final String serviceDefinitionId;
	private final String planId;
	private final ServiceDefinition serviceDefinition;

	public DeleteServiceInstanceRequest(String instanceId, String serviceId,
										String planId, ServiceDefinition serviceDefinition,
										boolean async) {
		super(async);
		this.serviceInstanceId = instanceId;
		this.serviceDefinitionId = serviceId;
		this.planId = planId;
		this.serviceDefinition = serviceDefinition;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
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
		DeleteServiceInstanceRequest that = (DeleteServiceInstanceRequest) o;
		return Objects.equals(serviceInstanceId, that.serviceInstanceId) &&
				Objects.equals(serviceDefinitionId, that.serviceDefinitionId) &&
				Objects.equals(planId, that.planId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceInstanceId, serviceDefinitionId, planId);
	}
}

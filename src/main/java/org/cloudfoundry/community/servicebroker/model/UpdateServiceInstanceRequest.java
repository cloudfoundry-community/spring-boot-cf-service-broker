package org.cloudfoundry.community.servicebroker.model;

import java.util.Map;
import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A request sent by the cloud controller to update an instance of a service.
 * 
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateServiceInstanceRequest  extends AsyncParameterizedServiceInstanceRequest {

	@NotEmpty
	@JsonSerialize
	@JsonProperty("service_id")
	private String serviceDefinitionId;

	@NotEmpty
	@JsonSerialize
	@JsonProperty("plan_id")
	private String planId;

	@JsonIgnore
	private ServiceDefinition serviceDefinition;

	@JsonIgnore
	private String serviceInstanceId;

	public UpdateServiceInstanceRequest() {
		super(false);
	} 
	
	public UpdateServiceInstanceRequest(String serviceDefinitionId, String planId,
										Map<String, Object> parameters, boolean acceptsIncomplete) {
		super(acceptsIncomplete);
		this.serviceDefinitionId = serviceDefinitionId;
		this.planId = planId;
		this.parameters = parameters;
	}

	public UpdateServiceInstanceRequest withServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
		return this;
	}

	public UpdateServiceInstanceRequest withServiceDefinition(ServiceDefinition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
		return this;
	}

	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}

	public ServiceDefinition getServiceDefinition() {
		return serviceDefinition;
	}

	public String getPlanId() {
		return planId;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UpdateServiceInstanceRequest that = (UpdateServiceInstanceRequest) o;
		return Objects.equals(planId, that.planId) &&
				Objects.equals(async, that.async) &&
				Objects.equals(parameters, that.parameters);
	}

	@Override
	public int hashCode() {
		return Objects.hash(planId, async, parameters);
	}
}

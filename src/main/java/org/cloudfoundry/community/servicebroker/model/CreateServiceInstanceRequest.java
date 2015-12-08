package org.cloudfoundry.community.servicebroker.model;

import java.util.Map;
import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A request sent by the cloud controller to create a new instance
 * of a service.
 * 
 * @author sgreenberg@gopivotal.com
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CreateServiceInstanceRequest extends AsyncParameterizedServiceInstanceRequest {

	@NotEmpty
	@JsonSerialize
	@JsonProperty("service_id")
	private String serviceDefinitionId;
	
	@NotEmpty
	@JsonSerialize
	@JsonProperty("plan_id")
	private String planId;
	
	@NotEmpty
	@JsonSerialize
	@JsonProperty("organization_guid")
	private String organizationGuid;
	
	@NotEmpty
	@JsonSerialize
	@JsonProperty("space_guid")
	private String spaceGuid;

	// Cloud Controller doesn't send the definition, it's populated later
	@JsonIgnore
	private ServiceDefinition serviceDefinition;

	// Cloud Controller doesn't send instanceId in the body
	@JsonIgnore
	private String serviceInstanceId;
	
	public CreateServiceInstanceRequest() {
		super(false);
	}
	
	public CreateServiceInstanceRequest(String serviceDefinitionId, String planId,
										String organizationGuid, String spaceGuid,
										Map<String, Object> parameters, boolean acceptsIncomplete) {
		super(acceptsIncomplete);
		this.serviceDefinitionId = serviceDefinitionId;
		this.planId = planId;
		this.organizationGuid = organizationGuid;
		this.spaceGuid = spaceGuid;
		this.parameters = parameters;
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

	public String getOrganizationGuid() {
		return organizationGuid;
	}

	public String getSpaceGuid() {
		return spaceGuid;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public CreateServiceInstanceRequest withServiceDefinition(ServiceDefinition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
		return this;
	}

	public CreateServiceInstanceRequest withServiceInstanceId(final String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CreateServiceInstanceRequest that = (CreateServiceInstanceRequest) o;
		return Objects.equals(serviceInstanceId, that.serviceInstanceId) &&
				Objects.equals(serviceDefinitionId, that.serviceDefinitionId) &&
				Objects.equals(planId, that.planId) &&
				Objects.equals(organizationGuid, that.organizationGuid) &&
				Objects.equals(spaceGuid, that.spaceGuid) &&
				Objects.equals(async, that.async) &&
				Objects.equals(parameters, that.parameters);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceInstanceId, serviceDefinitionId, planId, organizationGuid, spaceGuid, async, parameters);
	}
}

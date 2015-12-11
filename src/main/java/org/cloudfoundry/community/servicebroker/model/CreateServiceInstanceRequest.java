package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Map;

/**
 * Details of a request to create a new service instance.
 * 
 * @author sgreenberg@gopivotal.com
 * @author Scott Frederick
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CreateServiceInstanceRequest extends AsyncParameterizedServiceInstanceRequest {

	@NotEmpty
	@JsonSerialize
	@JsonProperty("service_id")
	private final String serviceDefinitionId;
	
	@NotEmpty
	@JsonSerialize
	@JsonProperty("plan_id")
	private final String planId;
	
	@NotEmpty
	@JsonSerialize
	@JsonProperty("organization_guid")
	private final String organizationGuid;
	
	@NotEmpty
	@JsonSerialize
	@JsonProperty("space_guid")
	private final String spaceGuid;

	// Cloud Controller doesn't send the definition, it's populated later
	@JsonIgnore
	private transient ServiceDefinition serviceDefinition;

	// Cloud Controller doesn't send instanceId in the body
	@JsonIgnore
	private transient String serviceInstanceId;
	
	public CreateServiceInstanceRequest() {
		super(null, false);
		this.serviceDefinitionId = null;
		this.planId = null;
		this.organizationGuid = null;
		this.spaceGuid = null;
	}
	
	public CreateServiceInstanceRequest(String serviceDefinitionId, String planId,
										String organizationGuid, String spaceGuid,
										Map<String, Object> parameters, boolean acceptsIncomplete) {
		super(parameters, acceptsIncomplete);
		this.serviceDefinitionId = serviceDefinitionId;
		this.planId = planId;
		this.organizationGuid = organizationGuid;
		this.spaceGuid = spaceGuid;
	}

	public CreateServiceInstanceRequest withServiceDefinition(ServiceDefinition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
		return this;
	}

	public CreateServiceInstanceRequest withServiceInstanceId(final String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
		return this;
	}
}

package org.cloudfoundry.community.servicebroker.model;

import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Details of a request to update a service instance.
 *
 * @author Scott Frederick
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateServiceInstanceRequest  extends AsyncParameterizedServiceInstanceRequest {

	@NotEmpty
	@JsonSerialize
	@JsonProperty("service_id")
	private final String serviceDefinitionId;

	@NotEmpty
	@JsonSerialize
	@JsonProperty("plan_id")
	private final String planId;

	@JsonIgnore
	private transient ServiceDefinition serviceDefinition;

	@JsonIgnore
	private transient String serviceInstanceId;

	public UpdateServiceInstanceRequest() {
		super(null, false);
		this.serviceDefinitionId = null;
		this.planId = null;
	}
	
	public UpdateServiceInstanceRequest(String serviceDefinitionId, String planId,
										Map<String, Object> parameters, boolean acceptsIncomplete) {
		super(parameters, acceptsIncomplete);
		this.serviceDefinitionId = serviceDefinitionId;
		this.planId = planId;
	}

	public UpdateServiceInstanceRequest withServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
		return this;
	}

	public UpdateServiceInstanceRequest withServiceDefinition(ServiceDefinition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
		return this;
	}
}

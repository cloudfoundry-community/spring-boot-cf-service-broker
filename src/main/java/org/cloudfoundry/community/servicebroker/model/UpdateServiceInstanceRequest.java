package org.cloudfoundry.community.servicebroker.model;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A request sent by the cloud controller to update an instance of a service.
 * 
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateServiceInstanceRequest {

	@NotEmpty
	@JsonSerialize
	@JsonProperty("plan_id")
	private String planId;
	
	@JsonIgnore
	private String serviceInstanceId;

	public UpdateServiceInstanceRequest() {} 
	
	public UpdateServiceInstanceRequest(String planId) {
		this.planId = planId;
	}

	public String getPlanId() {
		return planId;
	}
	
	public String getServiceInstanceId() { 
		return serviceInstanceId;
	}

	public UpdateServiceInstanceRequest withInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId; 
		return this;
	}
}

package org.cloudfoundry.community.servicebroker.model;

import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	
    @JsonSerialize
    @JsonProperty("parameters")
    private Object parameters;
	
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
	
    public Map<?, ?> getParameters() {
        return getParameters(Map.class);
    }

    public <T> T getParameters(Class<T> cls) throws IllegalArgumentException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(parameters, cls);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

	public UpdateServiceInstanceRequest withInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId; 
		return this;
	}
}

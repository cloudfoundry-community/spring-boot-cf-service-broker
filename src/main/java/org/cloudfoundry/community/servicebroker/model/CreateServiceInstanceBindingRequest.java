package org.cloudfoundry.community.servicebroker.model;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Request sent from the cloud controller to bind to a service 
 * instance.
 * 
 * @author sgreenberg@gopivotal.com
 *
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CreateServiceInstanceBindingRequest {

	@NotEmpty
	@JsonSerialize
	@JsonProperty("service_id")
	private String serviceDefinitionId;
	
	@NotEmpty
	@JsonSerialize
	@JsonProperty("plan_id")
	private String planId;

	@JsonSerialize
	@JsonProperty("app_guid")
	private String appGuid;

	@JsonSerialize
	@JsonProperty("parameters")
	private Map<String, Object> parameters;

	@JsonIgnore
	private String serviceInstanceId;

	@JsonIgnore
	private String bindingId;
	
	public CreateServiceInstanceBindingRequest() {
	}
	
	public CreateServiceInstanceBindingRequest(String serviceDefinitionId, String planId, String appGuid, Map<String, Object> parameters) {
		this.serviceDefinitionId = serviceDefinitionId;
		this.planId = planId;
		this.appGuid = appGuid;
		this.parameters = parameters;
	}

	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}
	
	public String getPlanId() {
		return planId;
	}
	
	public String getAppGuid() {
		return appGuid;
	}

	public String getBindingId() {
		return bindingId;
	}
	
	public String getServiceInstanceId() { 
		return serviceInstanceId;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public <T> T getParameters(Class<T> cls) {
		try {
			T bean = cls.newInstance();
			BeanUtils.populate(bean, parameters);
			return bean;
		} catch (Exception e) {
			throw new IllegalArgumentException("Error mapping parameters to class of type " + cls.getName());
		}
	}

	public CreateServiceInstanceBindingRequest withServiceInstanceId(final String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
		return this;
	}

	public CreateServiceInstanceBindingRequest withBindingId(final String bindingId) {
		this.bindingId = bindingId;
		return this;
	}
	
	public CreateServiceInstanceBindingRequest and() {
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CreateServiceInstanceBindingRequest that = (CreateServiceInstanceBindingRequest) o;
		return Objects.equals(serviceDefinitionId, that.serviceDefinitionId) &&
				Objects.equals(planId, that.planId) &&
				Objects.equals(appGuid, that.appGuid) &&
				Objects.equals(parameters, that.parameters);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceDefinitionId, planId, appGuid, parameters);
	}
}

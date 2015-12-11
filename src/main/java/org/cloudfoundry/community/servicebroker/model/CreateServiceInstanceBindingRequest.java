package org.cloudfoundry.community.servicebroker.model;

import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Details of a request to bind to a service instance binding.
 * 
 * @author sgreenberg@gopivotal.com
 * @author Scott Frederick
 */
@Getter
@ToString
@EqualsAndHashCode
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CreateServiceInstanceBindingRequest {

	@NotEmpty
	@JsonSerialize
	@JsonProperty("service_id")
	private final String serviceDefinitionId;
	
	@NotEmpty
	@JsonSerialize
	@JsonProperty("plan_id")
	private final String planId;

	@JsonSerialize
	@JsonProperty("app_guid")
	private final String appGuid;

	@JsonSerialize
	@JsonProperty("parameters")
	private final Map<String, Object> parameters;

	@JsonIgnore
	private transient String serviceInstanceId;

	@JsonIgnore
	private transient String bindingId;

	@JsonIgnore
	private transient ServiceDefinition serviceDefinition;
	
	public CreateServiceInstanceBindingRequest() {
		serviceDefinitionId = null;
		planId = null;
		appGuid = null;
		parameters = null;
	}
	
	public CreateServiceInstanceBindingRequest(String serviceDefinitionId, String planId,
											   String appGuid, Map<String, Object> parameters) {
		this.serviceDefinitionId = serviceDefinitionId;
		this.planId = planId;
		this.appGuid = appGuid;
		this.parameters = parameters;
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

	public CreateServiceInstanceBindingRequest withServiceDefinition(final ServiceDefinition serviceDefinition) {
		this.serviceDefinition = serviceDefinition;
		return this;
	}

	public CreateServiceInstanceBindingRequest withServiceInstanceId(final String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
		return this;
	}

	public CreateServiceInstanceBindingRequest withBindingId(final String bindingId) {
		this.bindingId = bindingId;
		return this;
	}
}

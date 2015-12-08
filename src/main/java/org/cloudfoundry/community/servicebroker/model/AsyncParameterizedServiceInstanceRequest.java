package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

public abstract class AsyncParameterizedServiceInstanceRequest extends AsyncServiceInstanceRequest {
	@JsonSerialize
	@JsonProperty("parameters")
	protected Map<String, Object> parameters;

	public AsyncParameterizedServiceInstanceRequest(boolean async) {
		super(async);
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
}

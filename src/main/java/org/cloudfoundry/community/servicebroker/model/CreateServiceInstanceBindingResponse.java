package org.cloudfoundry.community.servicebroker.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Details of a response to a request to create a new service instance binding.
 * 
 * @author sgreenberg@pivotal.io
 * @author <A href="mailto:josh@joshlong.com">Josh Long</A>
 * @author Scott Frederick
 */
@Getter
@ToString
@EqualsAndHashCode
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateServiceInstanceBindingResponse {

	/**
	 * A free-form hash of credentials that the bound application can use to access the service.
	 */
	@NotEmpty
	@JsonSerialize
	@JsonProperty("credentials")
	private final Map<String, Object> credentials;

	/**
	 * The URL to which Cloud Foundry should drain logs for the bound application. Can be <code>null</code> to
	 * indicate that the service binding does not support syslog drains.
	 */
	@JsonSerialize
	@JsonProperty("syslog_drain_url")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final String syslogDrainUrl;

	public CreateServiceInstanceBindingResponse(Map<String, Object> credentials, String syslogDrainUrl) {
		this.credentials = credentials;
		this.syslogDrainUrl = syslogDrainUrl;
	}

	public CreateServiceInstanceBindingResponse(Map<String, Object> credentials) {
		this(credentials, null);
	}
}

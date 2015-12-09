package org.cloudfoundry.community.servicebroker.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The response sent to the cloud controller when a bind
 * request is successful.
 * 
 * @author sgreenberg@gopivotal.com
 * @author <A href="mailto:josh@joshlong.com">Josh Long</A>
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateServiceInstanceBindingResponse {

	@NotEmpty
	@JsonSerialize
	@JsonProperty("credentials")
	private Map<String, Object> credentials;

	@JsonSerialize
	@JsonProperty("syslog_drain_url")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String syslogDrainUrl;

	public CreateServiceInstanceBindingResponse() {
	}
	
	public CreateServiceInstanceBindingResponse(Map<String, Object> credentials, String syslogDrainUrl) {
		this.credentials = credentials;
		this.syslogDrainUrl = syslogDrainUrl;
	}

	public Map<String, Object> getCredentials() {
		return credentials;
	}

	public String getSyslogDrainUrl() {
		return syslogDrainUrl;
	}
}

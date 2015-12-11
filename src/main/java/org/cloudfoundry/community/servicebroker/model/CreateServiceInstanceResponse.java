package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Details of a response to a request to create a new service instance.
 *
 * @author Scott Frederick
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CreateServiceInstanceResponse extends AsyncServiceInstanceResponse {
	@JsonSerialize
	@JsonProperty("dashboard_url")
	private String dashboardUrl;

	public CreateServiceInstanceResponse(String dashboardUrl, boolean async) {
		super(async);
		this.dashboardUrl = dashboardUrl;
	}
}

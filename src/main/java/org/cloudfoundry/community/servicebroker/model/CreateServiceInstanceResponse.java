package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CreateServiceInstanceResponse extends AsyncServiceInstanceResponse {
	@JsonSerialize
	@JsonProperty("dashboard_url")
	private String dashboardUrl;

	public CreateServiceInstanceResponse(String dashboardUrl, boolean async) {
		super(async);
		this.dashboardUrl = dashboardUrl;
	}

	public String getDashboardUrl() {
		return dashboardUrl;
	}
}

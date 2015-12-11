package org.cloudfoundry.community.servicebroker.model;

import java.util.List;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A service offered by this broker.
 *
 * @author sgreenberg@gopivotal.com
 * @author Scott Frederick
 */
@Getter
@ToString
@EqualsAndHashCode
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDefinition {

	@NotEmpty
	@JsonSerialize
	@JsonProperty("id")
	private String id;

	@NotEmpty
	@JsonSerialize
	@JsonProperty("name")
	private String name;

	@NotEmpty
	@JsonSerialize
	@JsonProperty("description")
	private String description;

	@JsonSerialize
	@JsonProperty("bindable")
	private boolean bindable;

	@JsonSerialize
	@JsonProperty("plan_updateable")
	private boolean planUpdateable;

	@NotEmpty
	@JsonSerialize(nullsUsing = EmptyListSerializer.class)
	@JsonProperty("plans")
	private List<Plan> plans;

	@JsonSerialize(nullsUsing = EmptyListSerializer.class)
	@JsonProperty("tags")
	private List<String> tags;

	@JsonSerialize(nullsUsing = EmptyMapSerializer.class)
	@JsonProperty("metadata")
	private Map<String, Object> metadata;

	@JsonSerialize(nullsUsing = EmptyListSerializer.class)
	@JsonProperty("requires")
	private List<String> requires;

	@JsonSerialize
	@JsonProperty("dashboard_client")
	private DashboardClient dashboardClient;

	public ServiceDefinition() {
	}

	public ServiceDefinition(String id, String name, String description, boolean bindable, List<Plan> plans) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.bindable = bindable;
		this.plans = plans;
	}

	public ServiceDefinition(String id, String name, String description, boolean bindable, boolean planUpdateable,
							 List<Plan> plans, List<String> tags, Map<String, Object> metadata, List<String> requires,
							 DashboardClient dashboardClient) {
		this(id, name, description, bindable, plans);
		this.tags = tags;
		this.metadata = metadata;
		this.requires = requires;
		this.planUpdateable = planUpdateable;
		this.dashboardClient = dashboardClient;
	}
}

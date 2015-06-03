package org.cloudfoundry.community.servicebroker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A service offered by this broker.
 * 
 * @author sgreenberg@gopivotal.com
 *
 */
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
	@JsonSerialize
	@JsonProperty("plans")
	private List<Plan> plans = new ArrayList<Plan>();
	
	@JsonSerialize
	@JsonProperty("tags")
	private List<String> tags = new ArrayList<String>();
	
	@JsonSerialize
	@JsonProperty("metadata")
	private Map<String,Object> metadata = new HashMap<String,Object>();
	
	@JsonSerialize
	@JsonProperty("requires")
	private List<String> requires = new ArrayList<String>();
	
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
		this.setPlans(plans);
	}

	public ServiceDefinition(String id, String name, String description, boolean bindable, boolean planUpdateable, 
			List<Plan> plans, List<String> tags, Map<String,Object> metadata, List<String> requires, 
			DashboardClient dashboardClient) {
		this(id, name, description, bindable, plans);
		setTags(tags);
		setMetadata(metadata);
		setRequires(requires);
		setPlanUpdateable(planUpdateable);
		this.dashboardClient = dashboardClient;
	}
	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isBindable() {
		return bindable;
	}

	public boolean isPlanUpdateable() {
		return planUpdateable;
	}

	public void setPlanUpdateable(boolean planUpdateable) {
		this.planUpdateable = planUpdateable;
	}

	public List<Plan> getPlans() {
		return plans;
	}

	private void setPlans(List<Plan> plans) {
		if ( plans == null ) {
			// ensure serialization as an empty array and not null
			this.plans = new ArrayList<Plan>();
		} else {
			this.plans = plans;
		}
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		if (tags == null) {
			this.tags = new ArrayList<String>();
		} else {
			this.tags = tags;
		}
	}

	public List<String> getRequires() {
		return requires;
	}

	public void setRequires(List<String> requires) {
		if (requires == null) {
			this.requires = new ArrayList<String>();
		} else {
			this.requires = requires;
		}
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, Object> metadata) {
		if (metadata == null) {
			this.metadata = new HashMap<String,Object>();
		} else {
			this.metadata = metadata;
		}
	}
	
	public DashboardClient getDashboardClient() {
		return dashboardClient;
	}

}

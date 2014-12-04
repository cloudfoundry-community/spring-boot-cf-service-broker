package org.cloudfoundry.community.servicebroker.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A service plan available for a ServiceDefinition
 * 
 * @author sgreenberg@gopivotal.com
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Plan {

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
	@JsonProperty("metadata")
	private Map<String,Object> metadata = new HashMap<String,Object>();
	
	@JsonSerialize
	@JsonProperty("free")
	private boolean free;

	public Plan() {
	}

	public Plan(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Plan(String id, String name, String description, Map<String,Object> metadata) {
		this(id, name, description);
		setMetadata(metadata);
	}
	
	public Plan(String id, String name, String description, Map<String,Object> metadata, boolean free) {
		this(id, name, description, metadata);
		this.free = free;
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

	public Map<String, Object> getMetadata() {
		return metadata;
	}
	
	private void setMetadata(Map<String, Object> metadata) {
		if (metadata == null) {
			this.metadata = new HashMap<String,Object>();
		} else {
			this.metadata = metadata;
		}
	}
	
	public boolean isFree() {
		return free;
	}
	
}

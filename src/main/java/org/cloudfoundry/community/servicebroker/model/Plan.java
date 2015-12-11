package org.cloudfoundry.community.servicebroker.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A service plan available for a ServiceDefinition
 *
 * @author sgreenberg@gopivotal.com
 * @author Scott Frederick
 */
@Getter
@ToString
@EqualsAndHashCode
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

	@JsonSerialize(nullsUsing = EmptyMapSerializer.class)
	@JsonProperty("metadata")
	private Map<String, Object> metadata;

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

	public Plan(String id, String name, String description, Map<String, Object> metadata) {
		this(id, name, description);
		this.metadata = metadata;
	}

	public Plan(String id, String name, String description, Map<String, Object> metadata, boolean free) {
		this(id, name, description, metadata);
		this.free = free;
	}
}

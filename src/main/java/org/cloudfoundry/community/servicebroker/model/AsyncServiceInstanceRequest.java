package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Details of a request that supports asynchronous behavior.
 *
 * @author Scott Frederick
 */
@Getter
@ToString
@EqualsAndHashCode
public abstract class AsyncServiceInstanceRequest {

	@JsonSerialize
	@JsonProperty("accepts_incomplete")
	protected final boolean async;
	
	public AsyncServiceInstanceRequest(boolean async) {
		this.async = async;
	}
}

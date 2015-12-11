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

	/**
	 * Indicates whether clients of the service broker allow the broker to complete the request asynchronously. A
	 * <code>false</code> value indicates that clients do not allow asynchronous processing of requests, a
	 * <code>true</code> value indicates that clients do allow asynchronous processing.
	 */
	@JsonSerialize
	@JsonProperty("accepts_incomplete")
	protected final boolean asyncAccepted;
	
	public AsyncServiceInstanceRequest(boolean asyncAccepted) {
		this.asyncAccepted = asyncAccepted;
	}
}

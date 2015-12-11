package org.cloudfoundry.community.servicebroker.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Details of a response that support asynchronous behavior.
 *
 * @author Scott Frederick
 */
@Getter
@ToString
@EqualsAndHashCode
public abstract class AsyncServiceInstanceResponse {
	protected final boolean async;

	public AsyncServiceInstanceResponse(boolean async) {
		this.async = async;
	}
}

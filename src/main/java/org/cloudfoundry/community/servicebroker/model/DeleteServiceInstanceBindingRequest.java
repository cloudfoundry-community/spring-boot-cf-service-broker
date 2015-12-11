package org.cloudfoundry.community.servicebroker.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 *  Details of a request to delete a service instance binding.
 *
 * @author krujos
 * @author Scott Frederick
 */
@Getter
@ToString
@EqualsAndHashCode
public class DeleteServiceInstanceBindingRequest {

	private final String serviceInstanceId;
	private final String bindingId;
	private final String serviceDefinitionId;
	private final String planId;
	private transient final ServiceDefinition serviceDefinition;

	public DeleteServiceInstanceBindingRequest(String serviceInstanceId, String bindingId,
											   String serviceDefinitionId, String planId,
											   ServiceDefinition serviceDefinition) {
		this.serviceInstanceId = serviceInstanceId;
		this.bindingId = bindingId;
		this.serviceDefinitionId = serviceDefinitionId;
		this.planId = planId;
		this.serviceDefinition = serviceDefinition;
	}
}

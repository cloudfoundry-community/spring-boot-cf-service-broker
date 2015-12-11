package org.cloudfoundry.community.servicebroker.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Details of a request to delete a service instance.
 *
 * @author krujos
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DeleteServiceInstanceRequest extends AsyncServiceInstanceRequest {
	private final String serviceInstanceId;
	private final String serviceDefinitionId;
	private final String planId;

	private transient final ServiceDefinition serviceDefinition;

	public DeleteServiceInstanceRequest(String instanceId, String serviceId,
										String planId, ServiceDefinition serviceDefinition,
										boolean async) {
		super(async);
		this.serviceInstanceId = instanceId;
		this.serviceDefinitionId = serviceId;
		this.planId = planId;
		this.serviceDefinition = serviceDefinition;
	}
}

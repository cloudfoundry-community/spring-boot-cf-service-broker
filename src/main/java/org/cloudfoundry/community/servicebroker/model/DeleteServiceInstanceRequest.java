package org.cloudfoundry.community.servicebroker.model;

/**
 * A request sent by the cloud controller to remove a service.
 * @author krujos
 *
 */
public class DeleteServiceInstanceRequest extends ServiceInstanceRequest {

	private final String serviceInstanceId;
	private final String serviceId;
	private final String planId;

	public DeleteServiceInstanceRequest(String instanceId, String serviceId,
			String planId, boolean async) {
		super(async);
		this.serviceInstanceId = instanceId; 
		this.serviceId = serviceId;
		this.planId = planId;
	}

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public String getPlanId() {
		return planId;
	}
}

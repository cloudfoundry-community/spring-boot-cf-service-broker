package org.cloudfoundry.community.servicebroker.model;

/**
 * A request sent by the cloud controller to remove a service.
 * @author krujos
 *
 */
public class DeleteServiceInstanceRequest {

	private final String serviceInstanceId;
	private final String serviceId;
	private final String planId;
	private boolean asyncClient;

	public DeleteServiceInstanceRequest(String instanceId, String serviceId,
			String planId, boolean acceptsIncomplete) {
		this.serviceInstanceId = instanceId; 
		this.serviceId = serviceId;
		this.planId = planId;
		this.asyncClient = acceptsIncomplete;
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

	public boolean hasAsyncClient() {
		return asyncClient;
	}
}

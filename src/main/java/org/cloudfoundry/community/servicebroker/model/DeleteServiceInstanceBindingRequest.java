package org.cloudfoundry.community.servicebroker.model;

/**
 *  A request sent by the cloud controller to remove a binding
 *  of a service.
 * @author krujos
 *
 */
public class DeleteServiceInstanceBindingRequest {

	private final String bindingId;
	private final String serviceId;
	private final ServiceInstance instance;
	private final String planId;

	public DeleteServiceInstanceBindingRequest(String bindingId,
			ServiceInstance instance, String serviceId, String planId) {
		this.bindingId = bindingId;
		this.instance = instance;
		this.serviceId = serviceId; 
		this.planId = planId;
	}

	public String getBindingId() {
		return bindingId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public ServiceInstance getInstance() {
		return instance;
	}

	public String getPlanId() {
		return planId;
	}

}

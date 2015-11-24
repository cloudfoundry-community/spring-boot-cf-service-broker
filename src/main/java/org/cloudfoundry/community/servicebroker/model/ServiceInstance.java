package org.cloudfoundry.community.servicebroker.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * An instance of a ServiceDefinition.
 * 
 * @author sgreenberg@gopivotal.com
 *
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class ServiceInstance {

	@JsonSerialize
	@JsonProperty("service_instance_id")
	private String serviceInstanceId;
	
	@JsonSerialize
	@JsonProperty("service_id")
	private String serviceDefinitionId;
	
	@JsonSerialize
	@JsonProperty("plan_id")
	private String planId;
	
	@JsonSerialize
	@JsonProperty("organization_guid")
	private String organizationGuid;
	
	@JsonSerialize
	@JsonProperty("space_guid")
	private String spaceGuid;
	
	@JsonSerialize
	@JsonProperty("dashboard_url")
	private String dashboardUrl;

	@JsonSerialize
	@JsonProperty("last_operation")
	private ServiceInstanceLastOperation lastOperation; 
	
	@JsonIgnore
	private boolean async;
	
	@SuppressWarnings("unused")
	private ServiceInstance() {}
	
	/**
	 * Create a ServiceInstance from a create request. If fields 
	 * are not present in the request they will remain null in the  
	 * ServiceInstance.
	 * @param request containing details of ServiceInstance
	 */
	public ServiceInstance(CreateServiceInstanceRequest request) {
		this.serviceDefinitionId = request.getServiceDefinitionId();
		this.planId = request.getPlanId();
		this.organizationGuid = request.getOrganizationGuid();
		this.spaceGuid = request.getSpaceGuid();
		this.serviceInstanceId = request.getServiceInstanceId();
		this.lastOperation = new ServiceInstanceLastOperation("Provisioning", OperationState.IN_PROGRESS);
	}
	
	/**
	 * Create a ServiceInstance from a delete request. If fields 
	 * are not present in the request they will remain null in the 
	 * ServiceInstance.
	 * @param request containing details of ServiceInstance
	 */
	public ServiceInstance(DeleteServiceInstanceRequest request) {
		this.serviceInstanceId = request.getServiceInstanceId();
		this.planId = request.getPlanId();
		this.serviceDefinitionId = request.getServiceId();
		this.lastOperation = new ServiceInstanceLastOperation("Deprovisioning", OperationState.IN_PROGRESS);

	}
	
	/**
	 * Create a service instance from an update request. If fields
	 * are not present in the request they will remain null in the 
	 * ServiceInstance.
	 * @param request containing details of ServiceInstance
	 */
	public ServiceInstance(UpdateServiceInstanceRequest request) {
		this.serviceInstanceId = request.getServiceInstanceId();
		this.planId = request.getPlanId();
		this.lastOperation = new ServiceInstanceLastOperation("Updating", OperationState.IN_PROGRESS);
	}
	
	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public String getServiceDefinitionId() {
		return serviceDefinitionId;
	}

	public String getPlanId() {
		return planId;
	}

	public String getOrganizationGuid() {
		return organizationGuid;
	}

	public String getSpaceGuid() {
		return spaceGuid;
	}

	public String getDashboardUrl() {
		return dashboardUrl;
	}

	public boolean isAsync() {
		return async;
	}

	public ServiceInstance and() {
		return this;
	}

	public ServiceInstance withLastOperation(ServiceInstanceLastOperation lastOperation) {
		this.lastOperation = lastOperation;
		return this;
	}

	public ServiceInstance withDashboardUrl(String dashboardUrl) {
		this.dashboardUrl = dashboardUrl;
		return this;
	}

	public ServiceInstance withAsync(boolean async) {
		this.async = async;
		return this;
	}

	public ServiceInstanceLastOperation getServiceInstanceLastOperation() {
		return lastOperation;
	}
	
}

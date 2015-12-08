package org.cloudfoundry.community.servicebroker.model.fixture;

import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceResponse;
import org.cloudfoundry.community.servicebroker.model.DeleteServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.DeleteServiceInstanceResponse;
import org.cloudfoundry.community.servicebroker.model.GetLastServiceOperationRequest;
import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;
import org.cloudfoundry.community.servicebroker.model.ServiceInstance;
import org.cloudfoundry.community.servicebroker.model.UpdateServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.UpdateServiceInstanceResponse;

public class ServiceInstanceFixture {

	public static ServiceInstance getServiceInstance() {
		return new ServiceInstance(new CreateServiceInstanceRequest(
				"service-one-id",
				"plan-one-id",
				DataFixture.getOrgOneGuid(),
				DataFixture.getSpaceOneGuid(),
				ParametersFixture.getParameters(), false)
				.withServiceInstanceId("service-instance-one-id"))
				.withDashboardUrl("dashboard_url");
	}

	public static CreateServiceInstanceRequest buildCreateServiceInstanceRequest(boolean acceptsIncomplete) {
		ServiceDefinition service = ServiceFixture.getService();
		return new CreateServiceInstanceRequest(
				service.getId(),
				service.getPlans().get(0).getId(),
				DataFixture.getOrgOneGuid(),
				DataFixture.getSpaceOneGuid(),
				ParametersFixture.getParameters(),
				acceptsIncomplete)
				.withServiceInstanceId("service-instance-id");
	}

	public static CreateServiceInstanceResponse buildCreateServiceInstanceResponse(boolean async) {
		return new CreateServiceInstanceResponse("https://dashboard_url.example.com", async);
	}

	public static DeleteServiceInstanceRequest buildDeleteServiceInstanceRequest(boolean acceptsIncomplete) {
		ServiceDefinition service = ServiceFixture.getService();
		return new DeleteServiceInstanceRequest("service-instance-id",
				service.getId(),
				service.getPlans().get(0).getId(),
				acceptsIncomplete);
	}

	public static DeleteServiceInstanceResponse buildDeleteServiceInstanceResponse(boolean async) {
		return new DeleteServiceInstanceResponse(async);
	}

	public static UpdateServiceInstanceRequest buildUpdateServiceInstanceRequest(boolean acceptsIncomplete) {
		ServiceDefinition service = ServiceFixture.getService();
		return new UpdateServiceInstanceRequest(
				service.getId(),
				service.getPlans().get(0).getId(),
				ParametersFixture.getParameters(),
				acceptsIncomplete)
				.withServiceInstanceId("service-instance-id");
	}

	public static UpdateServiceInstanceResponse buildUpdateServiceInstanceResponse(boolean async) {
		return new UpdateServiceInstanceResponse(async);
	}

	public static GetLastServiceOperationRequest buildGetLastOperationRequest() {
		return new GetLastServiceOperationRequest("service-instance-id");
	}
}

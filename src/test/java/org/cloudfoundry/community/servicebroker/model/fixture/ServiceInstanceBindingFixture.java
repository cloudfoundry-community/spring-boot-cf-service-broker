package org.cloudfoundry.community.servicebroker.model.fixture;

import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.community.servicebroker.model.*;

public class ServiceInstanceBindingFixture {

	public static CreateServiceInstanceBindingRequest buildCreateServiceInstanceBindingRequest() {
		return new CreateServiceInstanceBindingRequest(
				ServiceFixture.getService().getId(),
				PlanFixture.getPlanOne().getId(),
				getAppGuid(),
				ParametersFixture.getParameters())
				.withBindingId(getServiceInstanceBindingId())
				.withServiceInstanceId("service-instance-one-id");
	}

	public static CreateServiceInstanceBindingResponse buildCreateServiceInstanceBindingResponse() {
		return new CreateServiceInstanceBindingResponse(getCredentials(), getSysLogDrainUrl());
	}

	public static CreateServiceInstanceBindingResponse buildCreateServiceInstanceBindingResponseWithoutSyslog() {
		return new CreateServiceInstanceBindingResponse(getCredentials(), null);
	}

	public static DeleteServiceInstanceBindingRequest buildDeleteServiceInstanceBindingRequest() {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		ServiceDefinition service = ServiceFixture.getService();

		return new DeleteServiceInstanceBindingRequest(instance.getServiceInstanceId(), getServiceInstanceBindingId(),
				service.getId(), service.getPlans().get(0).getId(), service);
	}

	public static String getServiceInstanceBindingId() {
		return "service_instance_binding_id";
	}

	private static Map<String,Object> getCredentials() {
		Map<String,Object> credentials = new HashMap<>();
		credentials.put("uri","http://uri.example.com");
		credentials.put("username", "user1");
		credentials.put("password", "pwd1");
		return credentials;
	}

	private static String getSysLogDrainUrl() {
		return "http://syslog.example.com";
	}

	private static String getAppGuid() {
		return "app_guid";
	}
}

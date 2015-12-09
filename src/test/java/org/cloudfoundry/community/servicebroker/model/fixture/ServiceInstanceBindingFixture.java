package org.cloudfoundry.community.servicebroker.model.fixture;

import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.community.servicebroker.model.*;

public class ServiceInstanceBindingFixture {

	public static final String SERVICE_INSTANCE_BINDING_ID = "service_instance_binding_id";
	public static final String SERVICE_INSTANCE_ID = "service-instance-one-id";
	public static final String SYSLOG_DRAIN_URL = "http://syslog.example.com";
	public static final String APP_GUID = "app_guid";

	public static CreateServiceInstanceBindingRequest buildCreateServiceInstanceBindingRequest() {
		return new CreateServiceInstanceBindingRequest(
				ServiceFixture.getService().getId(),
				PlanFixture.getPlanOne().getId(),
				APP_GUID,
				ParametersFixture.getParameters())
				.withBindingId(SERVICE_INSTANCE_BINDING_ID)
				.withServiceInstanceId(SERVICE_INSTANCE_ID);
	}

	public static CreateServiceInstanceBindingResponse buildCreateServiceInstanceBindingResponse() {
		return new CreateServiceInstanceBindingResponse(getCredentials(), SYSLOG_DRAIN_URL);
	}

	public static CreateServiceInstanceBindingResponse buildCreateServiceInstanceBindingResponseWithoutSyslog() {
		return new CreateServiceInstanceBindingResponse(getCredentials(), null);
	}

	public static DeleteServiceInstanceBindingRequest buildDeleteServiceInstanceBindingRequest() {
		ServiceDefinition service = ServiceFixture.getService();

		return new DeleteServiceInstanceBindingRequest(SERVICE_INSTANCE_ID, SERVICE_INSTANCE_BINDING_ID,
				service.getId(), service.getPlans().get(0).getId(), service);
	}

	private static Map<String,Object> getCredentials() {
		Map<String,Object> credentials = new HashMap<>();
		credentials.put("uri","http://uri.example.com");
		credentials.put("username", "user1");
		credentials.put("password", "pwd1");
		return credentials;
	}
}

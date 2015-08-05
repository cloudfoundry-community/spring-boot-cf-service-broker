package org.cloudfoundry.community.servicebroker.model.fixture;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.community.servicebroker.model.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceInstanceBindingFixture {

	public static ServiceInstanceBinding getServiceInstanceBinding() {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		return new ServiceInstanceBinding(
				getServiceInstanceBindingId(),
				instance.getServiceInstanceId(),
				getCredentials(),
				getSysLogDrainUrl(),
				getAppGuid()
		);
	}

	public static String getServiceInstanceBindingId() {
		return "service_instance_binding_id";
	}
	
	public static Map<String,Object> getCredentials() {
		Map<String,Object> credentials = new HashMap<String,Object>();
		credentials.put("uri","uri");
		credentials.put("username", "username");
		credentials.put("password", "password");
		return credentials;
	}
	
	public static String getSysLogDrainUrl() {
		return "syslog_drain_url";
	}
	
	public static String getAppGuid() {
		return "app_guid";
	}
	
	public static CreateServiceInstanceBindingRequest getServiceInstanceBindingRequest() {
		return new CreateServiceInstanceBindingRequest(
				ServiceFixture.getService().getId(), 
				PlanFixture.getPlanOne().getId(),
				getAppGuid(),
				ParametersFixture.getParameters()
		); 	
	}
	
	public static String getServiceInstanceBindingRequestJson() throws IOException {
		 ObjectMapper mapper = new ObjectMapper();
		 return mapper.writeValueAsString(getServiceInstanceBindingRequest());
	}
	
}

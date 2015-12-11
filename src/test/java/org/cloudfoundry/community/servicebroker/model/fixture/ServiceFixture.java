package org.cloudfoundry.community.servicebroker.model.fixture;

import java.util.Collections;
import java.util.List;

import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;

public class ServiceFixture {

	public static List<ServiceDefinition> getAllServices() {
		return Collections.singletonList(ServiceFixture.getService());
	} 
	
	public static ServiceDefinition getService() {
		return new ServiceDefinition(
				"service-one-id", 
				"Service One", 
				"Description for Service One", 
				true,
				PlanFixture.getAllPlans());
	}
	
}

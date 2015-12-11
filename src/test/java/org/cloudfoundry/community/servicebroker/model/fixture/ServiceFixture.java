package org.cloudfoundry.community.servicebroker.model.fixture;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;
import org.cloudfoundry.community.servicebroker.model.ServiceDefinitionRequires;

public class ServiceFixture {

	public static ServiceDefinition getSimpleService() {
		return new ServiceDefinition(
				"service-one-id", 
				"Service One", 
				"Description for Service One", 
				true,
				PlanFixture.getAllPlans());
	}

	public static ServiceDefinition getServiceWithRequires() {
		return new ServiceDefinition(
				"service-one-id",
				"Service One",
				"Description for Service One",
				true,
				true,
				PlanFixture.getAllPlans(),
				null,
				null,
				Arrays.asList(
						ServiceDefinitionRequires.SERVICE_REQUIRES_SYSLOG_DRAIN.toString(),
						ServiceDefinitionRequires.SERVICE_REQUIRES_ROUTE_FORWARDING.toString()
				),
				null);
	}

}

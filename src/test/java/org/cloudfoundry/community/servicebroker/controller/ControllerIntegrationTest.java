package org.cloudfoundry.community.servicebroker.controller;

import org.cloudfoundry.community.servicebroker.model.fixture.ServiceFixture;
import org.cloudfoundry.community.servicebroker.service.CatalogService;
import org.mockito.Mock;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public abstract class ControllerIntegrationTest {
	@Mock
	protected CatalogService catalogService;

	protected void setupCatalogService(String serviceDefinitionId) {
		when(catalogService.getServiceDefinition(eq(serviceDefinitionId)))
				.thenReturn(ServiceFixture.getSimpleService());
	}
}

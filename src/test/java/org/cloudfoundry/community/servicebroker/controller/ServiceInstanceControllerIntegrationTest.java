package org.cloudfoundry.community.servicebroker.controller;

import static org.cloudfoundry.community.servicebroker.model.fixture.DataFixture.toJson;
import static org.cloudfoundry.community.servicebroker.model.fixture.ServiceInstanceFixture.buildCreateServiceInstanceRequest;
import static org.cloudfoundry.community.servicebroker.model.fixture.ServiceInstanceFixture.buildCreateServiceInstanceResponse;
import static org.cloudfoundry.community.servicebroker.model.fixture.ServiceInstanceFixture.buildDeleteServiceInstanceRequest;
import static org.cloudfoundry.community.servicebroker.model.fixture.ServiceInstanceFixture.buildDeleteServiceInstanceResponse;
import static org.cloudfoundry.community.servicebroker.model.fixture.ServiceInstanceFixture.buildGetLastOperationRequest;
import static org.cloudfoundry.community.servicebroker.model.fixture.ServiceInstanceFixture.buildUpdateServiceInstanceRequest;
import static org.cloudfoundry.community.servicebroker.model.fixture.ServiceInstanceFixture.buildUpdateServiceInstanceResponse;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;
import org.cloudfoundry.community.servicebroker.model.fixture.ServiceFixture;
import org.cloudfoundry.community.servicebroker.service.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ServiceInstanceControllerIntegrationTest {

	private static final String BASE_PATH = "/v2/service_instances/";

	private MockMvc mockMvc;

	@InjectMocks
	private ServiceInstanceController controller;

	@Mock
	private ServiceInstanceService serviceInstanceService;

	@Mock
	private CatalogService catalogService;

	private UriComponentsBuilder uriBuilder;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setMessageConverters(new MappingJackson2HttpMessageConverter())
				.build();

		uriBuilder = UriComponentsBuilder.fromPath(BASE_PATH);
	}

	@Test
	public void createServiceInstanceSucceeds() throws Exception {
		CreateServiceInstanceRequest request = buildCreateServiceInstanceRequest(false);
		CreateServiceInstanceResponse response = buildCreateServiceInstanceResponse(false);

		when(serviceInstanceService.createServiceInstance(eq(request)))
				.thenReturn(response);

		setupCatalogService(request.getServiceDefinitionId());

		mockMvc.perform(put(buildUrl(request))
						.content(toJson(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.dashboard_url", is(response.getDashboardUrl())));
	}

	@Test
	public void createServiceInstanceWithAsyncSucceeds() throws Exception {
		CreateServiceInstanceRequest request = buildCreateServiceInstanceRequest(true);
		CreateServiceInstanceResponse response = buildCreateServiceInstanceResponse(true);

		when(serviceInstanceService.createServiceInstance(eq(request)))
				.thenReturn(response);

		setupCatalogService(request.getServiceDefinitionId());

		mockMvc.perform(put(buildUrl(request))
						.content(toJson(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.dashboard_url", is(response.getDashboardUrl())));
	}

	@Test
	public void createServiceInstanceWithUnknownServiceDefinitionFails() throws Exception {
		CreateServiceInstanceRequest request = buildCreateServiceInstanceRequest(false);

		when(catalogService.getServiceDefinition(eq(request.getServiceDefinitionId())))
				.thenReturn(null);

		mockMvc.perform(put(buildUrl(request))
						.content(toJson(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.description", containsString(request.getServiceDefinitionId())));
	}

	@Test
	public void createDuplicateServiceInstanceFails() throws Exception {
		CreateServiceInstanceRequest request = buildCreateServiceInstanceRequest(false);

		when(serviceInstanceService.createServiceInstance(eq(request)))
				.thenThrow(new ServiceInstanceExistsException(request.getServiceInstanceId(), request.getServiceDefinitionId()));

		setupCatalogService(request.getServiceDefinitionId());

		mockMvc.perform(put(buildUrl(request))
						.content(toJson(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.description", containsString(request.getServiceInstanceId())));
	}

	@Test
	public void createServiceInstanceWithAsyncRequiredFails() throws Exception {
		CreateServiceInstanceRequest request = buildCreateServiceInstanceRequest(false);

		when(serviceInstanceService.createServiceInstance(eq(request)))
				.thenThrow(new ServiceBrokerAsyncRequiredException("msg"));

		setupCatalogService(request.getServiceDefinitionId());

		mockMvc.perform(put(buildUrl(request))
						.content(toJson(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.error", is("AsyncRequired")));
	}

	@Test
	public void createServiceInstanceWithBadJsonFails() throws Exception {
		CreateServiceInstanceRequest request = buildCreateServiceInstanceRequest(false);

		when(serviceInstanceService.createServiceInstance(eq(request)))
				.thenReturn(buildCreateServiceInstanceResponse(false));

		setupCatalogService(request.getServiceDefinitionId());

		String body = toJson(request);
		body = body.replace("service_id", "foo");

		mockMvc.perform(put(buildUrl(request))
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.description", containsString("Missing required fields")));
	}

	@Test
	public void createServiceInstanceWithMissingJsonFieldsFails() throws Exception {
		CreateServiceInstanceRequest request = buildCreateServiceInstanceRequest(false);

		when(serviceInstanceService.createServiceInstance(any(CreateServiceInstanceRequest.class)))
				.thenReturn(buildCreateServiceInstanceResponse(false));

		String body = "{}";

		mockMvc.perform(put(buildUrl(request))
						.content(body)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.description", containsString("serviceDefinitionId")))
				.andExpect(jsonPath("$.description", containsString("planId")))
				.andExpect(jsonPath("$.description", containsString("organizationGuid")))
				.andExpect(jsonPath("$.description", containsString("spaceGuid")));
	}

	@Test
	public void deleteServiceInstanceSucceeds() throws Exception {
		DeleteServiceInstanceRequest request = buildDeleteServiceInstanceRequest(false);

		when(serviceInstanceService.deleteServiceInstance(eq(request)))
				.thenReturn(buildDeleteServiceInstanceResponse(false));

		mockMvc.perform(delete(buildUrl(request))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteServiceInstanceWithAsyncSucceeds() throws Exception {
		DeleteServiceInstanceRequest request = buildDeleteServiceInstanceRequest(true);

		when(serviceInstanceService.deleteServiceInstance(eq(request)))
				.thenReturn(buildDeleteServiceInstanceResponse(true));

		mockMvc.perform(delete(buildUrl(request))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted());
	}

	@Test
	public void deleteServiceInstanceWithUnknownIdFails() throws Exception {
		DeleteServiceInstanceRequest request = buildDeleteServiceInstanceRequest(false);

		when(serviceInstanceService.deleteServiceInstance(eq(request)))
				.thenThrow(new ServiceInstanceDoesNotExistException(request.getServiceInstanceId()));

		mockMvc.perform(delete(buildUrl(request))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isGone())
				.andExpect(jsonPath("$", is("{}")));
	}

	@Test
	public void deleteServiceInstanceWithAsyncRequiredFails() throws Exception {
		DeleteServiceInstanceRequest request = buildDeleteServiceInstanceRequest(false);

		when(serviceInstanceService.deleteServiceInstance(eq(request)))
				.thenThrow(new ServiceBrokerAsyncRequiredException("msg"));

		mockMvc.perform(delete(buildUrl(request))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.error", is("AsyncRequired")));
	}

	@Test
	public void updateServiceInstanceSucceeds() throws Exception {
		UpdateServiceInstanceRequest request = buildUpdateServiceInstanceRequest(false);

		when(serviceInstanceService.updateServiceInstance(eq(request)))
				.thenReturn(buildUpdateServiceInstanceResponse(false));

		setupCatalogService(request.getServiceDefinitionId());

		mockMvc.perform(patch(buildUrl(request))
						.content(toJson(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is("{}")));
	}

	@Test
	public void updateServiceInstanceWithAsyncSucceeds() throws Exception {
		UpdateServiceInstanceRequest request = buildUpdateServiceInstanceRequest(true);

		when(serviceInstanceService.updateServiceInstance(eq(request)))
				.thenReturn(buildUpdateServiceInstanceResponse(true));

		setupCatalogService(request.getServiceDefinitionId());

		mockMvc.perform(patch(buildUrl(request))
						.content(toJson(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$", is("{}")));
	}

	@Test
	public void updateServiceInstanceWithAsyncRequiredFails() throws Exception {
		UpdateServiceInstanceRequest request = buildUpdateServiceInstanceRequest(false);

		when(serviceInstanceService.updateServiceInstance(eq(request)))
				.thenThrow(new ServiceBrokerAsyncRequiredException("msg"));

		setupCatalogService(request.getServiceDefinitionId());

		mockMvc.perform(patch(buildUrl(request))
					.content(toJson(request))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.error", is("AsyncRequired")));
	}

	@Test
	public void updateServiceInstanceWithUnknownIdFails() throws Exception {
		UpdateServiceInstanceRequest request = buildUpdateServiceInstanceRequest(false);

		when(serviceInstanceService.updateServiceInstance(eq(request)))
				.thenThrow(new ServiceInstanceDoesNotExistException(request.getServiceInstanceId()));

		setupCatalogService(request.getServiceDefinitionId());

		mockMvc.perform(patch(buildUrl(request))
						.content(toJson(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.description", containsString(request.getServiceInstanceId())));
	}

	@Test
	public void updateServiceInstanceWithUnknownPlanFails() throws Exception {
		UpdateServiceInstanceRequest request = buildUpdateServiceInstanceRequest(false);

		when(serviceInstanceService.updateServiceInstance(eq(request)))
				.thenThrow(new ServiceInstanceUpdateNotSupportedException("description"));

		setupCatalogService(request.getServiceDefinitionId());

		mockMvc.perform(patch(buildUrl(request))
						.content(toJson(request))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.description", containsString("description")));
	}

	@Test
	public void lastOperationHasInProgressStatus() throws Exception {
		GetLastServiceOperationRequest request = buildGetLastOperationRequest();

		when(serviceInstanceService.getLastOperation(eq(request)))
				.thenReturn(new GetLastServiceOperationResponse(OperationState.IN_PROGRESS, "working on it", false));

		mockMvc.perform(get(buildUrl(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state", is(OperationState.IN_PROGRESS.getValue())))
				.andExpect(jsonPath("$.description", is("working on it")));
	}

	@Test
	public void lastOperationHasSucceededStatus() throws Exception {
		GetLastServiceOperationRequest request = buildGetLastOperationRequest();

		when(serviceInstanceService.getLastOperation(eq(request)))
				.thenReturn(new GetLastServiceOperationResponse(OperationState.SUCCEEDED, "all good", false));

		mockMvc.perform(get(buildUrl(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state", is(OperationState.SUCCEEDED.getValue())))
				.andExpect(jsonPath("$.description", is("all good")));
	}

	@Test
	public void lastOperationHasSucceededStatusWithDeletionComplete() throws Exception {
		GetLastServiceOperationRequest request = buildGetLastOperationRequest();

		when(serviceInstanceService.getLastOperation(eq(request)))
				.thenReturn(new GetLastServiceOperationResponse(OperationState.SUCCEEDED, "all gone", true));

		mockMvc.perform(get(buildUrl(request)))
				.andExpect(status().isGone())
				.andExpect(jsonPath("$.state", is(OperationState.SUCCEEDED.getValue())))
				.andExpect(jsonPath("$.description", is("all gone")));
	}

	@Test
	public void lastOperationHasFailedStatus() throws Exception {
		GetLastServiceOperationRequest request = buildGetLastOperationRequest();

		when(serviceInstanceService.getLastOperation(eq(request)))
				.thenReturn(new GetLastServiceOperationResponse(OperationState.FAILED, "not so good", false));

		mockMvc.perform(get(buildUrl(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state", is(OperationState.FAILED.getValue())))
				.andExpect(jsonPath("$.description", is("not so good")));
	}

	@Test
	public void lastOperationWithUnknownIdFails() throws Exception {
		GetLastServiceOperationRequest request = buildGetLastOperationRequest();

		when(serviceInstanceService.getLastOperation(eq(request)))
				.thenThrow(new ServiceInstanceDoesNotExistException(request.getServiceInstanceId()));

		mockMvc.perform(get(buildUrl(request)))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.description", containsString(request.getServiceInstanceId())));
	}

	private void setupCatalogService(String serviceDefinitionId) {
		when(catalogService.getServiceDefinition(eq(serviceDefinitionId)))
				.thenReturn(ServiceFixture.getService());
	}

	private String buildUrl(CreateServiceInstanceRequest request) {
		return uriBuilder.path(request.getServiceInstanceId()).toUriString();
	}

	private String buildUrl(DeleteServiceInstanceRequest request) {
		return uriBuilder.path(request.getServiceInstanceId())
				.queryParam("service_id", request.getServiceDefinitionId())
				.queryParam("plan_id", request.getPlanId())
				.queryParam("accepts_incomplete", request.isAsync())
				.toUriString();
	}

	private String buildUrl(UpdateServiceInstanceRequest request) {
		return uriBuilder.path(request.getServiceInstanceId()).toUriString();
	}

	private String buildUrl(GetLastServiceOperationRequest request) {
		return uriBuilder.pathSegment(request.getServiceInstanceId(), "last_operation").toUriString();
	}
}

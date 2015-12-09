package org.cloudfoundry.community.servicebroker.controller;

import static org.cloudfoundry.community.servicebroker.model.AsyncRequiredErrorMessage.ASYNC_REQUIRED_ERROR;
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
public class ServiceInstanceControllerIntegrationTest extends ControllerIntegrationTest {

	private MockMvc mockMvc;

	@InjectMocks
	private ServiceInstanceController controller;

	@Mock
	private ServiceInstanceService serviceInstanceService;

	private UriComponentsBuilder uriBuilder;

	private CreateServiceInstanceRequest syncCreateRequest;
	private CreateServiceInstanceRequest asyncCreateRequest;
	private CreateServiceInstanceResponse syncCreateResponse;
	private CreateServiceInstanceResponse asyncCreateResponse;

	private DeleteServiceInstanceRequest syncDeleteRequest;
	private DeleteServiceInstanceRequest asyncDeleteRequest;
	private DeleteServiceInstanceResponse syncDeleteResponse;
	private DeleteServiceInstanceResponse asyncDeleteResponse;

	private UpdateServiceInstanceRequest syncUpdateRequest;
	private UpdateServiceInstanceRequest asyncUpdateRequest;
	private UpdateServiceInstanceResponse syncUpdateResponse;
	private UpdateServiceInstanceResponse asyncUpdateResponse;

	private GetLastServiceOperationRequest lastOperationRequest;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setMessageConverters(new MappingJackson2HttpMessageConverter())
				.build();

		uriBuilder = UriComponentsBuilder.fromPath("/v2/service_instances/");

		syncCreateRequest = buildCreateServiceInstanceRequest(false);
		syncCreateResponse = buildCreateServiceInstanceResponse(false);
		asyncCreateRequest = buildCreateServiceInstanceRequest(true);
		asyncCreateResponse = buildCreateServiceInstanceResponse(true);

		syncDeleteRequest = buildDeleteServiceInstanceRequest(false);
		syncDeleteResponse = buildDeleteServiceInstanceResponse(false);
		asyncDeleteRequest = buildDeleteServiceInstanceRequest(true);
		asyncDeleteResponse = buildDeleteServiceInstanceResponse(true);

		syncUpdateRequest = buildUpdateServiceInstanceRequest(false);
		syncUpdateResponse = buildUpdateServiceInstanceResponse(false);
		asyncUpdateRequest = buildUpdateServiceInstanceRequest(true);
		asyncUpdateResponse = buildUpdateServiceInstanceResponse(true);

		lastOperationRequest = buildGetLastOperationRequest();
	}

	@Test
	public void createServiceInstanceSucceeds() throws Exception {
		when(serviceInstanceService.createServiceInstance(eq(syncCreateRequest)))
				.thenReturn(syncCreateResponse);

		setupCatalogService(syncCreateRequest.getServiceDefinitionId());

		mockMvc.perform(put(buildUrl(syncCreateRequest))
				.content(toJson(syncCreateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.dashboard_url", is(syncCreateResponse.getDashboardUrl())));
	}

	@Test
	public void createServiceInstanceWithAsyncSucceeds() throws Exception {
		when(serviceInstanceService.createServiceInstance(eq(asyncCreateRequest)))
				.thenReturn(asyncCreateResponse);

		setupCatalogService(asyncCreateRequest.getServiceDefinitionId());

		mockMvc.perform(put(buildUrl(asyncCreateRequest))
				.content(toJson(asyncCreateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$.dashboard_url", is(asyncCreateResponse.getDashboardUrl())));
	}

	@Test
	public void createServiceInstanceWithUnknownServiceDefinitionIdFails() throws Exception {
		when(catalogService.getServiceDefinition(eq(syncCreateRequest.getServiceDefinitionId())))
				.thenReturn(null);

		mockMvc.perform(put(buildUrl(syncCreateRequest))
				.content(toJson(syncCreateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.description", containsString(syncCreateRequest.getServiceDefinitionId())));
	}

	@Test
	public void createDuplicateServiceInstanceIdFails() throws Exception {
		when(serviceInstanceService.createServiceInstance(eq(syncCreateRequest)))
				.thenThrow(new ServiceInstanceExistsException(syncCreateRequest.getServiceInstanceId(),
						syncCreateRequest.getServiceDefinitionId()));

		setupCatalogService(syncCreateRequest.getServiceDefinitionId());

		mockMvc.perform(put(buildUrl(syncCreateRequest))
				.content(toJson(syncCreateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.description", containsString(syncCreateRequest.getServiceInstanceId())));
	}

	@Test
	public void createServiceInstanceWithAsyncRequiredFails() throws Exception {
		when(serviceInstanceService.createServiceInstance(eq(syncCreateRequest)))
				.thenThrow(new ServiceBrokerAsyncRequiredException("async required description"));

		setupCatalogService(syncCreateRequest.getServiceDefinitionId());

		mockMvc.perform(put(buildUrl(syncCreateRequest))
				.content(toJson(syncCreateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.error", is(ASYNC_REQUIRED_ERROR)))
				.andExpect(jsonPath("$.description", is("async required description")));
	}

	@Test
	public void createServiceInstanceWithInvalidFieldsFails() throws Exception {
		when(serviceInstanceService.createServiceInstance(eq(syncCreateRequest)))
				.thenReturn(syncCreateResponse);

		setupCatalogService(syncCreateRequest.getServiceDefinitionId());

		String body = toJson(syncCreateRequest);
		body = body.replace("service_id", "foo");

		mockMvc.perform(put(buildUrl(syncCreateRequest))
				.content(body)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.description", containsString("serviceDefinitionId")));
	}

	@Test
	public void createServiceInstanceWithMissingFieldsFails() throws Exception {
		when(serviceInstanceService.createServiceInstance(any(CreateServiceInstanceRequest.class)))
				.thenReturn(syncCreateResponse);

		String body = "{}";

		mockMvc.perform(put(buildUrl(syncCreateRequest))
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
		when(serviceInstanceService.deleteServiceInstance(eq(syncDeleteRequest)))
				.thenReturn(syncDeleteResponse);

		setupCatalogService(syncDeleteRequest.getServiceDefinitionId());

		mockMvc.perform(delete(buildUrl(syncDeleteRequest))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteServiceInstanceWithAsyncSucceeds() throws Exception {
		when(serviceInstanceService.deleteServiceInstance(eq(asyncDeleteRequest)))
				.thenReturn(asyncDeleteResponse);

		setupCatalogService(asyncDeleteRequest.getServiceDefinitionId());

		mockMvc.perform(delete(buildUrl(asyncDeleteRequest))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted());
	}

	@Test
	public void deleteServiceInstanceWithUnknownIdFails() throws Exception {
		when(serviceInstanceService.deleteServiceInstance(eq(syncDeleteRequest)))
				.thenThrow(new ServiceInstanceDoesNotExistException(syncDeleteRequest.getServiceInstanceId()));

		setupCatalogService(syncDeleteRequest.getServiceDefinitionId());

		mockMvc.perform(delete(buildUrl(syncDeleteRequest))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isGone())
				.andExpect(jsonPath("$", is("{}")));
	}

	@Test
	public void deleteServiceInstanceWithUnknownServiceDefinitionIdFails() throws Exception {
		when(serviceInstanceService.deleteServiceInstance(eq(syncDeleteRequest)))
				.thenThrow(new ServiceInstanceDoesNotExistException(syncDeleteRequest.getServiceInstanceId()));

		when(catalogService.getServiceDefinition(eq(syncDeleteRequest.getServiceDefinitionId())))
				.thenReturn(null);

		mockMvc.perform(delete(buildUrl(syncDeleteRequest))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.description", containsString(syncDeleteRequest.getServiceDefinitionId())));
	}

	@Test
	public void deleteServiceInstanceWithAsyncRequiredFails() throws Exception {
		when(serviceInstanceService.deleteServiceInstance(eq(syncDeleteRequest)))
				.thenThrow(new ServiceBrokerAsyncRequiredException("async required description"));

		setupCatalogService(syncDeleteRequest.getServiceDefinitionId());

		mockMvc.perform(delete(buildUrl(syncDeleteRequest))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.error", is(ASYNC_REQUIRED_ERROR)))
				.andExpect(jsonPath("$.description", is("async required description")));
	}

	@Test
	public void updateServiceInstanceSucceeds() throws Exception {
		when(serviceInstanceService.updateServiceInstance(eq(syncUpdateRequest)))
				.thenReturn(syncUpdateResponse);

		setupCatalogService(syncUpdateRequest.getServiceDefinitionId());

		mockMvc.perform(patch(buildUrl(syncUpdateRequest))
				.content(toJson(syncUpdateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is("{}")));
	}

	@Test
	public void updateServiceInstanceWithAsyncSucceeds() throws Exception {
		when(serviceInstanceService.updateServiceInstance(eq(asyncUpdateRequest)))
				.thenReturn(asyncUpdateResponse);

		setupCatalogService(asyncUpdateRequest.getServiceDefinitionId());

		mockMvc.perform(patch(buildUrl(asyncUpdateRequest))
				.content(toJson(asyncUpdateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(jsonPath("$", is("{}")));
	}

	@Test
	public void updateServiceInstanceWithAsyncRequiredFails() throws Exception {
		when(serviceInstanceService.updateServiceInstance(eq(syncUpdateRequest)))
				.thenThrow(new ServiceBrokerAsyncRequiredException("async required description"));

		setupCatalogService(syncUpdateRequest.getServiceDefinitionId());

		mockMvc.perform(patch(buildUrl(syncUpdateRequest))
				.content(toJson(syncUpdateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.error", is(ASYNC_REQUIRED_ERROR)))
				.andExpect(jsonPath("$.description", is("async required description")));
	}

	@Test
	public void updateServiceInstanceWithUnknownIdFails() throws Exception {
		when(serviceInstanceService.updateServiceInstance(eq(syncUpdateRequest)))
				.thenThrow(new ServiceInstanceDoesNotExistException(syncUpdateRequest.getServiceInstanceId()));

		setupCatalogService(syncUpdateRequest.getServiceDefinitionId());

		mockMvc.perform(patch(buildUrl(syncUpdateRequest))
				.content(toJson(syncUpdateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.description", containsString(syncUpdateRequest.getServiceInstanceId())));
	}

	@Test
	public void updateServiceInstanceWithUnknownServiceDefinitionIdFails() throws Exception {
		when(serviceInstanceService.updateServiceInstance(eq(syncUpdateRequest)))
				.thenThrow(new ServiceInstanceUpdateNotSupportedException("description"));

		when(catalogService.getServiceDefinition(eq(syncUpdateRequest.getServiceDefinitionId())))
				.thenReturn(null);

		mockMvc.perform(patch(buildUrl(syncUpdateRequest))
				.content(toJson(syncUpdateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.description", containsString(syncUpdateRequest.getServiceDefinitionId())));
	}

	@Test
	public void updateServiceInstanceWithUnknownPlanIdFails() throws Exception {
		when(serviceInstanceService.updateServiceInstance(eq(syncUpdateRequest)))
				.thenThrow(new ServiceInstanceUpdateNotSupportedException("description"));

		setupCatalogService(syncUpdateRequest.getServiceDefinitionId());

		mockMvc.perform(patch(buildUrl(syncUpdateRequest))
				.content(toJson(syncUpdateRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.description", containsString("description")));
	}

	@Test
	public void lastOperationHasInProgressStatus() throws Exception {
		when(serviceInstanceService.getLastOperation(eq(lastOperationRequest)))
				.thenReturn(new GetLastServiceOperationResponse(OperationState.IN_PROGRESS, "working on it", false));

		mockMvc.perform(get(buildUrl(lastOperationRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state", is(OperationState.IN_PROGRESS.getValue())))
				.andExpect(jsonPath("$.description", is("working on it")));
	}

	@Test
	public void lastOperationHasSucceededStatus() throws Exception {
		when(serviceInstanceService.getLastOperation(eq(lastOperationRequest)))
				.thenReturn(new GetLastServiceOperationResponse(OperationState.SUCCEEDED, "all good", false));

		mockMvc.perform(get(buildUrl(lastOperationRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state", is(OperationState.SUCCEEDED.getValue())))
				.andExpect(jsonPath("$.description", is("all good")));
	}

	@Test
	public void lastOperationHasSucceededStatusWithDeletionComplete() throws Exception {
		when(serviceInstanceService.getLastOperation(eq(lastOperationRequest)))
				.thenReturn(new GetLastServiceOperationResponse(OperationState.SUCCEEDED, "all gone", true));

		mockMvc.perform(get(buildUrl(lastOperationRequest)))
				.andExpect(status().isGone())
				.andExpect(jsonPath("$.state", is(OperationState.SUCCEEDED.getValue())))
				.andExpect(jsonPath("$.description", is("all gone")));
	}

	@Test
	public void lastOperationHasFailedStatus() throws Exception {
		when(serviceInstanceService.getLastOperation(eq(lastOperationRequest)))
				.thenReturn(new GetLastServiceOperationResponse(OperationState.FAILED, "not so good", false));

		mockMvc.perform(get(buildUrl(lastOperationRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.state", is(OperationState.FAILED.getValue())))
				.andExpect(jsonPath("$.description", is("not so good")));
	}

	@Test
	public void lastOperationWithUnknownIdFails() throws Exception {
		when(serviceInstanceService.getLastOperation(eq(lastOperationRequest)))
				.thenThrow(new ServiceInstanceDoesNotExistException(lastOperationRequest.getServiceInstanceId()));

		mockMvc.perform(get(buildUrl(lastOperationRequest)))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.description", containsString(lastOperationRequest.getServiceInstanceId())));
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

package org.cloudfoundry.community.servicebroker.controller;

import static org.cloudfoundry.community.servicebroker.model.matchers.AsyncArgumentMatcher.anyAsyncRequest;
import static org.cloudfoundry.community.servicebroker.model.matchers.SyncArgumentMatcher.anySyncRequest;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;
import org.cloudfoundry.community.servicebroker.model.fixture.ServiceFixture;
import org.cloudfoundry.community.servicebroker.model.fixture.ServiceInstanceFixture;
import org.cloudfoundry.community.servicebroker.service.*;
import org.junit.*;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ServiceInstanceControllerIntegrationTest {
		
	MockMvc mockMvc;

	@InjectMocks
	ServiceInstanceController controller;

	@Mock
	ServiceInstanceService serviceInstanceService;
	
	@Mock
	CatalogService catalogService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
	}
	
	@Test
	public void serviceInstanceIsCreatedCorrectly() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		
		when(serviceInstanceService.createServiceInstance(
				eq(ServiceInstanceFixture.getCreateServiceInstanceRequest())))
			.thenReturn(instance);

		when(catalogService.getServiceDefinition(any(String.class)))
			.thenReturn(ServiceFixture.getService());
		
		String dashboardUrl = ServiceInstanceFixture.getServiceInstance().getDashboardUrl();

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId();
		String body = ServiceInstanceFixture.getCreateServiceInstanceRequestJson();

		mockMvc.perform(
				put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isCreated())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.dashboard_url", is(dashboardUrl)));
	}

	@Test
	public void unknownServiceDefinitionInstanceCreationFails() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();

		when(catalogService.getServiceDefinition(any(String.class)))
			.thenReturn(null);

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId();
		String body = ServiceInstanceFixture.getCreateServiceInstanceRequestJson();

		mockMvc.perform(
				put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.description", containsString(instance.getServiceDefinitionId())));
	}
	
	@Test
	public void duplicateServiceInstanceCreationFails() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();

		when(catalogService.getServiceDefinition(any(String.class)))
			.thenReturn(ServiceFixture.getService());

		when(serviceInstanceService.createServiceInstance(any(CreateServiceInstanceRequest.class)))
			.thenThrow(new ServiceInstanceExistsException(instance));

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId();
		String body = ServiceInstanceFixture.getCreateServiceInstanceRequestJson();

		mockMvc.perform(
				put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.description", containsString(instance.getServiceInstanceId())));
	}
	
	@Test
	public void badJsonServiceInstanceCreationFails() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		
		when(serviceInstanceService.createServiceInstance(any(CreateServiceInstanceRequest.class)))
			.thenReturn(instance);

		when(catalogService.getServiceDefinition(any(String.class)))
			.thenReturn(ServiceFixture.getService());

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId();
		String body = ServiceInstanceFixture.getCreateServiceInstanceRequestJson();
		body = body.replace("service_id", "foo");

		mockMvc.perform(
				put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.description", containsString("Missing required fields")));
	}

	@Test
	public void badJsonServiceInstanceCreationFailsMissingFields() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		
		when(serviceInstanceService.createServiceInstance(any(CreateServiceInstanceRequest.class)))
			.thenReturn(instance);

		when(catalogService.getServiceDefinition(any(String.class)))
			.thenReturn(ServiceFixture.getService());

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId();
		String body = "{}";

		mockMvc.perform(
				put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.description", containsString("serviceDefinitionId")))
			.andExpect(jsonPath("$.description", containsString("planId")))
			.andExpect(jsonPath("$.description", containsString("organizationGuid")))
			.andExpect(jsonPath("$.description", containsString("spaceGuid")));
	}
	
	@Test
	public void serviceInstanceIsDeletedSuccessfully() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();

		when(serviceInstanceService.deleteServiceInstance(any(DeleteServiceInstanceRequest.class)))
			.thenReturn(instance);

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId()
				+ "?service_id=" + instance.getServiceDefinitionId()
				+ "&plan_id=" + instance.getPlanId();

		mockMvc.perform(delete(url)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			//We return the service broker now, as the CC should be ignoring it.
			//.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			//.andExpect(jsonPath("$", is("{}"))
		;
	}

	@Test
	public void deleteUnknownServiceInstanceFailsWithA410() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();

		when(serviceInstanceService.deleteServiceInstance(any(DeleteServiceInstanceRequest.class)))
			.thenReturn(null);

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId()
				+ "?service_id=" + instance.getServiceDefinitionId()
				+ "&plan_id=" + instance.getPlanId();

		mockMvc.perform(delete(url)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isGone())
			.andExpect(jsonPath("$", is("{}")));
	}

	@Test
	public void serviceInstanceIsUpdatedSuccessfully() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();

		when(serviceInstanceService.updateServiceInstance(
				eq(ServiceInstanceFixture.getUpdateServiceInstanceRequest())))
			.thenReturn(instance);

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId();

		String body = ServiceInstanceFixture.getUpdateServiceInstanceRequestJson();

		mockMvc.perform(
				patch(url).contentType(MediaType.APPLICATION_JSON).content(body)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", is("{}")));
	}

	@Test
	public void updateUnsupportedPlanFailsWithA422() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();

		when(serviceInstanceService.updateServiceInstance(any(UpdateServiceInstanceRequest.class)))
		.thenThrow(new ServiceInstanceUpdateNotSupportedException("description"));

		String url =
				ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId() + "?service_id="
						+ instance.getServiceDefinitionId() + "&plan_id=" + instance.getPlanId();
		String body = ServiceInstanceFixture.getUpdateServiceInstanceRequestJson();

		mockMvc.perform(
				patch(url).contentType(MediaType.APPLICATION_JSON).content(body)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.description", containsString("description")));
	}
	
	@Test
	public void createServiceAsyncRequredShoudlFailWith422() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();

		when(catalogService.getServiceDefinition(any(String.class)))
		.thenReturn(ServiceFixture.getService());

		when(serviceInstanceService.createServiceInstance(any(CreateServiceInstanceRequest.class)))
			.thenThrow(new ServiceBrokerAsyncRequiredException("msg"));

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId();
		String body = ServiceInstanceFixture.getCreateServiceInstanceRequestJson();

		mockMvc.perform(
				put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.error", is("AsyncRequired")));
	}
	
	@Test
	public void deleteServiceAsyncRequredShoudlFailWith422() throws Exception{ 

		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();

		when(serviceInstanceService.deleteServiceInstance(any(DeleteServiceInstanceRequest.class)))
			.thenThrow(new ServiceBrokerAsyncRequiredException("Msg"));

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId()
				+ "?service_id=" + instance.getServiceDefinitionId()
				+ "&plan_id=" + instance.getPlanId();

		mockMvc.perform(delete(url)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.error", is("AsyncRequired")));
	}
	
	@Test
	public void updateServiceAsyncRequredShoudlFailWith422() throws Exception { 
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		when(serviceInstanceService.updateServiceInstance(any(UpdateServiceInstanceRequest.class)))
		.thenThrow(new ServiceBrokerAsyncRequiredException("msg"));

		String url =
				ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId() + "?service_id="
						+ instance.getServiceDefinitionId() + "&plan_id=" + instance.getPlanId();
		String body = ServiceInstanceFixture.getUpdateServiceInstanceRequestJson();

		mockMvc.perform(
				patch(url).contentType(MediaType.APPLICATION_JSON).content(body)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.error", is("AsyncRequired")));
	}

	@Test
	public void itShouldPassAnAsyncCreateServiceRequestAndReturn202() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance()
				.withLastOperation(new ServiceInstanceLastOperation("Doing stuff", OperationState.IN_PROGRESS))
				.withAsync(true);

		when(catalogService.getServiceDefinition(any(String.class)))
			.thenReturn(ServiceFixture.getService());

		when(serviceInstanceService.createServiceInstance(any(CreateServiceInstanceRequest.class)))
			.thenReturn(instance);

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId();
		String body = ServiceInstanceFixture.getCreateServiceInstanceRequestJson();

		mockMvc.perform(
				put(url)
				.param("accepts_incomplete", "true")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isAccepted())
			.andExpect(jsonPath("last_operation.state", is("in progress")));
	}
	
	@Test
	public void itShouldNotBeAsyncWhenAcceptsIncompleteParamIsNotPresent() throws Exception { 
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		
		when(serviceInstanceService.createServiceInstance(
				(CreateServiceInstanceRequest) anySyncRequest())).thenReturn(instance);

		when(catalogService.getServiceDefinition(any(String.class))).thenReturn(ServiceFixture.getService());

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId();
		String body = ServiceInstanceFixture.getCreateServiceInstanceRequestJson();

		mockMvc.perform(
				put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
				.accept(MediaType.APPLICATION_JSON)
			).andExpect(status().isCreated());
	}
	
	@Test
	public void itShouldReturnAnInProgressServiceInstance() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId() + "/last_operation";

		when(serviceInstanceService.getServiceInstance(any(String.class))).thenReturn(
				ServiceInstanceFixture.getAsyncServiceInstance().withLastOperation(
						new ServiceInstanceLastOperation("In Progress", OperationState.IN_PROGRESS)));
		mockMvc.perform(
				get(url))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.state", is("in progress")));
	}
	
	@Test
	public void itShouldReturnAFailedServiceInstance() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId() + "/last_operation";

		when(serviceInstanceService.getServiceInstance(any(String.class))).thenReturn(
				ServiceInstanceFixture.getAsyncServiceInstance().withLastOperation(
						new ServiceInstanceLastOperation("no working", OperationState.FAILED)));
		mockMvc.perform(
				get(url))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.state", is("failed")));
	}
	
	@Test
	public void itShouldReturnASucceededServiceInstance() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId() + "/last_operation";

		when(serviceInstanceService.getServiceInstance(any(String.class))).thenReturn(
				ServiceInstanceFixture.getAsyncServiceInstance().withLastOperation(
						new ServiceInstanceLastOperation("mucho working", OperationState.SUCCEEDED)));
		mockMvc.perform(
				get(url))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.state", is("succeeded")));
	}
	

	@Test
	public void itShouldReturnGoneIfTheServiceInstanceDoesNotExist() throws Exception { 
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId() + "/last_operation";

		when(serviceInstanceService.getServiceInstance(any(String.class))).thenReturn(null);
		mockMvc.perform(
				get(url))
				.andExpect(status().isGone())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", is("{}")));
	}
	
	@Test
	public void itShouldReturn202ForUpdatedInstanceWithAsync() throws Exception { 
		ServiceInstance instance = ServiceInstanceFixture.getAsyncServiceInstance()
				.withAsync(true);

		when(serviceInstanceService.updateServiceInstance(any(UpdateServiceInstanceRequest.class)))
			.thenReturn(instance);

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId();

		String body = ServiceInstanceFixture.getUpdateServiceInstanceRequestJson();

		mockMvc.perform(
				patch(url).contentType(MediaType.APPLICATION_JSON).content(body)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", is("{}")));
	}

	@Test
	public void itShouldReturn422ForAsyncRequired() throws Exception { 
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId();

		String body = ServiceInstanceFixture.getUpdateServiceInstanceRequestJson();

		when(serviceInstanceService.updateServiceInstance(any(UpdateServiceInstanceRequest.class)))
			.thenThrow(new ServiceBrokerAsyncRequiredException("msg"));
		
		
		mockMvc.perform(
				patch(url).contentType(MediaType.APPLICATION_JSON).content(body)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.error", is("AsyncRequired")))
				.andExpect(jsonPath("$.description", is("msg")));	
	}

	@Test
	public void itShouldReturn202ForAsyncDelete() throws Exception { 
		ServiceInstance instance = ServiceInstanceFixture.getAsyncServiceInstance()
			.withLastOperation(new ServiceInstanceLastOperation("doin stuff", OperationState.IN_PROGRESS))
			.withAsync(true);

		when(serviceInstanceService.deleteServiceInstance(
				(DeleteServiceInstanceRequest) anyAsyncRequest())).thenReturn(instance);

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getServiceInstanceId()
				+ "?service_id=" + instance.getServiceDefinitionId()
				+ "&plan_id=" + instance.getPlanId()
				+ "&accepts_incomplete=true";

		mockMvc.perform(delete(url)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isAccepted())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.last_operation.state", is("in progress"))
		);
	}
}

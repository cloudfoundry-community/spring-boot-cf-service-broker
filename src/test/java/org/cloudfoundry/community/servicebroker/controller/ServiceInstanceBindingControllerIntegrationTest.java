package org.cloudfoundry.community.servicebroker.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;
import org.cloudfoundry.community.servicebroker.model.fixture.*;
import org.cloudfoundry.community.servicebroker.service.*;
import org.junit.*;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ServiceInstanceBindingControllerIntegrationTest {

	private static final String BASE_PATH = "/v2/service_instances/" 
			+ ServiceInstanceFixture.getServiceInstance().getServiceInstanceId()
			+ "/service_bindings";
	
	MockMvc mockMvc;
	
	@InjectMocks
	ServiceInstanceBindingController controller;
	
	@Mock
	ServiceInstanceBindingService serviceInstanceBindingService;
	
	@Mock
	ServiceInstanceService serviceInstanceService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
	}
	
	@Test
	public void serviceInstanceBindingIsCreatedCorrectly() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		ServiceInstanceBinding binding = ServiceInstanceBindingFixture.getServiceInstanceBinding();
		
		when(serviceInstanceService.getServiceInstance(any(String.class)))
			.thenReturn(instance);

		CreateServiceInstanceBindingRequest serviceInstanceBindingRequest = ServiceInstanceBindingFixture.getServiceInstanceBindingRequest();
		when(serviceInstanceBindingService.createServiceInstanceBinding(
				eq(serviceInstanceBindingRequest)))
			.thenReturn(binding);

		String url = BASE_PATH + "/{bindingId}";
		String body = ServiceInstanceBindingFixture.getServiceInstanceBindingRequestJson();

		mockMvc.perform(
				put(url, binding.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
			)
			.andExpect(status().isCreated())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.credentials.uri", is("uri")))
			.andExpect(jsonPath("$.credentials.username", is("username")))
			.andExpect(jsonPath("$.credentials.password", is("password")));
	}
	
	@Test
	public void unknownServiceInstanceFailsBinding() throws Exception {
		ServiceInstanceBinding binding = ServiceInstanceBindingFixture.getServiceInstanceBinding();
		
		when(serviceInstanceService.getServiceInstance(any(String.class)))
			.thenReturn(null);

		String url = BASE_PATH + "/{bindingId}";
		String body = ServiceInstanceBindingFixture.getServiceInstanceBindingRequestJson();

		mockMvc.perform(
				put(url, binding.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
			)
			.andDo(print())
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.description", containsString(binding.getServiceInstanceId())));
	}
	
	@Test
	public void duplicateBindingRequestFailsBinding() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		ServiceInstanceBinding binding = ServiceInstanceBindingFixture.getServiceInstanceBinding();
		
		when(serviceInstanceService.getServiceInstance(any(String.class)))
			.thenReturn(instance);

		when(serviceInstanceBindingService.createServiceInstanceBinding(
				any(CreateServiceInstanceBindingRequest.class)))
			.thenThrow(new ServiceInstanceBindingExistsException(binding));
		
		String url = BASE_PATH + "/{bindingId}";
		String body = ServiceInstanceBindingFixture.getServiceInstanceBindingRequestJson();

		mockMvc.perform(
				put(url, binding.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
			)
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.description", containsString(binding.getId())));
	}

	@Test
	public void deleteBindingIs422WhenAsyncIsRequred() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		ServiceInstanceBinding binding = ServiceInstanceBindingFixture.getServiceInstanceBinding();
		
		when(serviceInstanceService.getServiceInstance(any(String.class)))
			.thenReturn(instance);

		when(serviceInstanceBindingService.deleteServiceInstanceBinding(any(DeleteServiceInstanceBindingRequest.class)))
			.thenThrow(new ServiceBrokerAsyncRequiredException("msg"));

		String url = BASE_PATH + "/" + binding.getId()
				+ "?service_id=" + instance.getServiceDefinitionId()
				+ "&plan_id=" + instance.getPlanId();

		mockMvc.perform(delete(url)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.error", is("AsyncRequired")));
	}
	
	@Test
	public void invalidBindingRequestJson() throws Exception {
		ServiceInstanceBinding binding = ServiceInstanceBindingFixture.getServiceInstanceBinding();
		
		String url = BASE_PATH + "/{bindingId}";
		String body = ServiceInstanceBindingFixture.getServiceInstanceBindingRequestJson();
		body = body.replace("service_id", "foo");
		mockMvc.perform(
				put(url, binding.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
			)
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.description", containsString("Missing required fields")));
	}
	
	@Test
	public void invalidBindingRequestMissingFields() throws Exception {
		ServiceInstanceBinding binding = ServiceInstanceBindingFixture.getServiceInstanceBinding();
		
		String url = BASE_PATH + "/{bindingId}";
		String body = "{}";

		mockMvc.perform(
				put(url, binding.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
			)
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.description", containsString("serviceDefinitionId")))
			.andExpect(jsonPath("$.description", containsString("planId")));
	}
	
	@Test
	public void serviceInstanceBindingIsDeletedSuccessfully() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		ServiceInstanceBinding binding = ServiceInstanceBindingFixture.getServiceInstanceBinding();
		
		when(serviceInstanceService.getServiceInstance(any(String.class)))
			.thenReturn(instance);

		when(serviceInstanceBindingService.deleteServiceInstanceBinding(any(DeleteServiceInstanceBindingRequest.class)))
			.thenReturn(binding);

		String url = BASE_PATH + "/" + binding.getId()
				+ "?service_id=" + instance.getServiceDefinitionId()
				+ "&plan_id=" + instance.getPlanId();

		mockMvc.perform(delete(url)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$", is("{}"))
		);
	}

	@Test
	public void unknownServiceInstanceBindingNotDeletedAndA410IsReturned() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		ServiceInstanceBinding binding = ServiceInstanceBindingFixture.getServiceInstanceBinding();

		when(serviceInstanceService.getServiceInstance(any(String.class)))
			.thenReturn(instance);
		when(serviceInstanceBindingService.deleteServiceInstanceBinding
				(any(DeleteServiceInstanceBindingRequest.class)))
			.thenReturn(null);

		String url = BASE_PATH + "/" + binding.getId()
				+ "?service_id=" + instance.getServiceDefinitionId()
				+ "&plan_id=" + instance.getPlanId();

		mockMvc.perform(delete(url)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isGone())
			.andExpect(jsonPath("$", is("{}")));
	}
	
	@Test
	public void whenAnUnknownServiceInstanceIsProvidedOnABindingDeleteAnHttp422IsReturned() throws Exception {
		ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		ServiceInstanceBinding binding = ServiceInstanceBindingFixture.getServiceInstanceBinding();
		
		when(serviceInstanceService.getServiceInstance(any(String.class)))
			.thenReturn(null);

		String url = BASE_PATH + "/" + binding.getId()
				+ "?service_id=" + instance.getServiceDefinitionId()
				+ "&plan_id=" + instance.getPlanId();

		mockMvc.perform(delete(url)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isUnprocessableEntity());
	}

}

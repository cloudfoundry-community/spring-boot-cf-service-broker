package org.cloudfoundry.community.servicebroker.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceExistsException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.cloudfoundry.community.servicebroker.model.ServiceDefinition;
import org.cloudfoundry.community.servicebroker.model.ServiceInstance;
import org.cloudfoundry.community.servicebroker.model.fixture.ServiceFixture;
import org.cloudfoundry.community.servicebroker.model.fixture.ServiceInstanceFixture;
import org.cloudfoundry.community.servicebroker.service.CatalogService;
import org.cloudfoundry.community.servicebroker.service.ServiceInstanceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
		
		when(serviceInstanceService.createServiceInstance(any(ServiceDefinition.class), any(String.class), any(String.class), any(String.class), any(String.class)))
	    	.thenReturn(instance);
	    
		when(catalogService.getServiceDefinition(any(String.class)))
    	.thenReturn(ServiceFixture.getService());
		
	    String dashboardUrl = ServiceInstanceFixture.getCreateServiceInstanceResponse().getDashboardUrl();
	    
	    String url = ServiceInstanceController.BASE_PATH + "/" + instance.getId();
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
	    
	    String url = ServiceInstanceController.BASE_PATH + "/" + instance.getId();
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
	    
		when(serviceInstanceService.createServiceInstance(any(ServiceDefinition.class), any(String.class), any(String.class), any(String.class), any(String.class)))
	    	.thenThrow(new ServiceInstanceExistsException(instance));
	    
	    String url = ServiceInstanceController.BASE_PATH + "/" + instance.getId();
	    String body = ServiceInstanceFixture.getCreateServiceInstanceRequestJson();
	    
	    mockMvc.perform(
	    		put(url)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(body)
	    		.accept(MediaType.APPLICATION_JSON)
	    	)
	    	.andExpect(status().isConflict())
	    	.andExpect(jsonPath("$.description", containsString(instance.getId())));
 	}
	
	@Test
	public void badJsonServiceInstanceCreationFails() throws Exception {
	    ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		
		when(serviceInstanceService.createServiceInstance(any(ServiceDefinition.class), any(String.class), any(String.class), any(String.class), any(String.class)))
	    	.thenReturn(instance);
	    
		when(catalogService.getServiceDefinition(any(String.class)))
    	.thenReturn(ServiceFixture.getService());
	    
	    String url = ServiceInstanceController.BASE_PATH + "/" + instance.getId();
	    String body = ServiceInstanceFixture.getCreateServiceInstanceRequestJson();
	    body = body.replace("service_id", "foo");
	    
	    mockMvc.perform(
	    		put(url)
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(body)
	    		.accept(MediaType.APPLICATION_JSON)
	    	)
	    	.andExpect(status().isUnprocessableEntity())
	    	.andExpect(jsonPath("$.description", containsString("foo")));
 	}
	
	@Test
	public void badJsonServiceInstanceCreationFailsMissingFields() throws Exception {
	    ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
		
		when(serviceInstanceService.createServiceInstance(any(ServiceDefinition.class), any(String.class), any(String.class), any(String.class), any(String.class)))
	    	.thenReturn(instance);
	    
		when(catalogService.getServiceDefinition(any(String.class)))
    	.thenReturn(ServiceFixture.getService());
	    
	    String url = ServiceInstanceController.BASE_PATH + "/" + instance.getId();
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
			    
		when(serviceInstanceService.deleteServiceInstance(any(String.class),any(String.class),any(String.class)))
    		.thenReturn(instance);
	    
	    String url = ServiceInstanceController.BASE_PATH + "/" + instance.getId() 
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
	public void deleteUnknownServiceInstanceFailsWithA410() throws Exception {
	    ServiceInstance instance = ServiceInstanceFixture.getServiceInstance();
			    
		when(serviceInstanceService.deleteServiceInstance(any(String.class),any(String.class),any(String.class)))
    		.thenReturn(null);
	    
	    String url = ServiceInstanceController.BASE_PATH + "/" + instance.getId() 
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

		when(serviceInstanceService.updateServiceInstance(any(String.class), any(String.class)))
		.thenReturn(instance);

		String url = ServiceInstanceController.BASE_PATH + "/" + instance.getId();

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

		when(serviceInstanceService.updateServiceInstance(any(String.class), any(String.class)))
		.thenThrow(new ServiceInstanceUpdateNotSupportedException("description"));

		String url =
				ServiceInstanceController.BASE_PATH + "/" + instance.getId() + "?service_id="
						+ instance.getServiceDefinitionId() + "&plan_id=" + instance.getPlanId();
		String body = ServiceInstanceFixture.getUpdateServiceInstanceRequestJson();

		mockMvc.perform(
				patch(url).contentType(MediaType.APPLICATION_JSON).content(body)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.description", containsString("description")));
	}

}

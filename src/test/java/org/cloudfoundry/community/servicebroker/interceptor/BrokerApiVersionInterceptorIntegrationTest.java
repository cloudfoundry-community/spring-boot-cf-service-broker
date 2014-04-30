package org.cloudfoundry.community.servicebroker.interceptor;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.cloudfoundry.community.servicebroker.controller.CatalogController;
import org.cloudfoundry.community.servicebroker.interceptor.BrokerApiVersionInterceptor;
import org.cloudfoundry.community.servicebroker.model.BrokerApiVersion;
import org.cloudfoundry.community.servicebroker.service.CatalogService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class BrokerApiVersionInterceptorIntegrationTest {

	MockMvc mockMvc;

	@InjectMocks
	CatalogController controller;

	@Mock
	CatalogService catalogService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

	    this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
	    		.addInterceptors(new BrokerApiVersionInterceptor(new BrokerApiVersion("header","version")))
	            .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
	}
	
	@Test
	public void noHeaderSent() throws Exception {
	    this.mockMvc.perform(get(CatalogController.BASE_PATH)
	        .accept(MediaType.APPLICATION_JSON))
	        .andExpect(status().isPreconditionFailed())
            .andExpect(jsonPath("$.description.", containsString("Expected Version")));
	}
	
	@Test
	public void incorrectHeaderSent() throws Exception {
	    this.mockMvc.perform(get(CatalogController.BASE_PATH)
	    	.header("header", "wrong-version")
	        .accept(MediaType.APPLICATION_JSON))
	        .andExpect(status().isPreconditionFailed())
            .andExpect(jsonPath("$.description.", containsString("Expected Version")));
	}
	
	@Test
	public void correctHeaderSent() throws Exception {
	    this.mockMvc.perform(get(CatalogController.BASE_PATH)
	    	.header("header", "version")
	        .accept(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk());
	}
	
}

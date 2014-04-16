package com.pivotal.cf.broker.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pivotal.cf.broker.model.fixture.CatalogFixture;
import com.pivotal.cf.broker.model.fixture.ServiceFixture;
import com.pivotal.cf.broker.service.CatalogService;

public class CatalogControllerIntegrationTest {
	
	MockMvc mockMvc;

	@InjectMocks
	CatalogController controller;

	@Mock
	CatalogService catalogService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

	    this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
	            .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
	}
	
	@Test
	public void catalogIsRetrievedCorrectly() throws Exception {
	    when(catalogService.getCatalog()).thenReturn(CatalogFixture.getCatalog());
	
	    this.mockMvc.perform(get(CatalogController.BASE_PATH)
	        .accept(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.services.", hasSize(1)))
            .andExpect(jsonPath("$.services[*].id", containsInAnyOrder(ServiceFixture.getService().getId())));
	    
	    // TO DO - check rest of the json including plans
	}

}

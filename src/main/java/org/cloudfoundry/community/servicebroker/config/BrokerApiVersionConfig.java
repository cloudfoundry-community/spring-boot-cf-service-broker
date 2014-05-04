package org.cloudfoundry.community.servicebroker.config;

import org.cloudfoundry.community.servicebroker.interceptor.BrokerApiVersionInterceptor;
import org.cloudfoundry.community.servicebroker.model.BrokerApiVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.cloudfoundry.community.servicebroker")
public class BrokerApiVersionConfig extends WebMvcConfigurerAdapter {

	@Bean
	public BrokerApiVersion brokerApiVersion() {
		return new BrokerApiVersion("X-Broker-Api-Version","2.3");
	}

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BrokerApiVersionInterceptor(brokerApiVersion())).addPathPatterns("/**");;
    }
	
}	
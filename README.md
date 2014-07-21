spring-boot-cf-service-broker
===========================

Spring boot project for creating Cloud Foundry Service brokers.

# Overview

The goal is to provide a spring-boot project (http://projects.spring.io/spring-boot/) to quickly implement new Service Brokers in CloudFoundry.  

## Compatibility

* service broker API: 2.3
* cf-release: 169 or greater
* Pivotal CF: N/A

## Getting Started

A sample project is available here: [Mongo Example](https://github.com/spgreenberg/spring-boot-cf-service-broker-mongo)

Create a new project for your broker and include the following in your build.gradle dependencies (be sure to set the version properties):
	
	compile("org.cloudfoundry:spring-boot-cf-service-broker:${springBootCfServiceBrokerVersion}")
    testCompile("org.cloudfoundry:spring-boot-cf-service-broker-tests:${springBootCfServiceBrokerVersion}")
    testCompile("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")

springBootCfServiceBrokerVersion corresponds to the service broker api you want write to (example 2.3)

### Latest 

springBootCfServiceBrokerVersion : 2.3.01

### Configuring your broker

You will need to customize your broker according to the following:

#### Web & Default Configuration

The configuration is all done through standard Spring mechanisms.  Feel free to configure however you see fit.  However, you need to address the following config elements. See examples: [Mongo Example](https://github.com/spgreenberg/spring-boot-cf-service-broker-mongo/tree/master/src/main/java/com/pivotal/cf/broker/mongodb/config)

* Application.java: The default configuration point.  This also allows you to run the app locally.

* BrokerConfig.java: This is required to ensure the `spring-boot-cf-service-broker` jar file is searched for auto wired dependencies.  You can also explicitly configure the controllers and services if you prefer.

If you would like to disable service broker api version header verification, you can disable it by excluding the BrokerApiVersionConfig class in the @ComponentScan annotation:

	@ComponentScan(
		basePackages = "org.cloudfoundry.community.servicebroker", 
		excludeFilters= { 
			@ComponentScan.Filter(
				type=FilterType.ASSIGNABLE_TYPE, 
				value=BrokerApiVersionConfig.class
			)
		}
	)

You can also create your own filter if you would like behavior other than a simple version equality verification.

* CatalogConfig.java: Configures a `BeanCatalogService`.  This is optional as you can implement `CatalogService` anyway you want.

* WebXml.java: Required to generate your web.xml.

#### Implementation

Minimally, you need to implement:

- org/cloudfoundry/community/servicebroker/service/ServiceInstanceBindingService.java
- org/cloudfoundry/community/servicebroker/service/ServiceInstanceService.java

And configure your catalog: 

Option 1: Use the org/cloudfoundry/community/servicebroker/service/BeanCatalogService.java by providing a bean definition of org/cloudfoundry/community/servicebroker/model/Catalog.java.  Example: [here](https://github.com/spgreenberg/spring-boot-cf-service-broker-mongo/blob/master/src/main/java/org/cloudfoundry/community/servicebroker/mongodb/config/CatalogConfig.java)

Option 2: Implement: org/cloudfoundry/community/servicebroker/service/CatalogService.java

That is it.  Everything else is optional.

#### Security

The project includes the [spring-boot-starter-security](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-starters/spring-boot-starter-security) project.  See the documentation here for configuration options: [Spring boot security](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-security)

The default behavior creates a user called `user` with a  password logged as an INFO message during app startup.  Example:

	2014-04-16T10:08:52.54-0600 [App/0]   OUT Using default password for application endpoints: 7c2969c1-d9c7-47e9-9c9e-2cd94a7b6cf1

If you are deploying your service broker to cloud foundry as an app, please be aware the password is re-generated every time you push the application.  Therefore, you need to run `cf update-service-broker` with the new password. 

Hint: tail the logs before pushing the service broker to CF:

	cf logs <your-broker>

### Deploying your broker

Be sure to follow all the usual steps: [here](http://docs.cloudfoundry.org/services/).

### Model Notes

- The model is for the REST/Controller level.  It can be extended as needed.
- All models explicitly define serialization field names.
- Currently, ServiceInstance is used internally and not exposed by any controllers.





spring-boot-starter-cf-service-broker
===========================

Spring boot project for creating Cloud Foundry Service brokers.

# Overview

The goal is to provide a spring-boot-starter project (http://projects.spring.io/spring-boot/) to quickly implement new Service Brokers in CloudFoundry.  

## Compatibility

* service broker API: 2.1
* cf-release: 152-163
* Pivotal CF: 1.1

## Getting Started

A sample project using this starter is available here: [Mongo Example](https://github.com/spgreenberg/spring-boot-cf-service-broker-mongo)

Currently, the starter project is not published to a maven repository.  Once it is, you will be able to skip the first 4 steps.  To use the project, do the following:

Clone the project

	git clone https://github.com/spgreenberg/spring-boot-cf-service-broker-mongo/tree/master/src/main/java/com/pivotal/cf/broker/mongodb/config

Checkout the branch corresponding to the Service Broker API version you need: example 

	git checkout v2.1

Build: 
		
	./gradlew build


Publish to your local maven repo so that your broker project can include this as a dependency: 

	./gradlew publishToMavenLocal

Create a new project for your broker and include the following in your build.gradle dependencies (be sure to set the version properties):
	
	compile("com.pivotal.cf.broker:spring-boot-starter-cf-service-broker:${springBootStarterCfServiceBrokerVersion}")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat:${springBootVersion}")
    testCompile("com.pivotal.cf.broker:spring-boot-starter-cf-service-broker-tests:${springBootStarterCfServiceBrokerVersion}")
    testCompile("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")

### Configuring your broker

You will need to customize your broker according to the following:

#### Web & Default Configuration

The configuration is all done through standard Spring mechanisms.  Feel free to configure however you see fit.  However, you need to address the following config elements. See examples: [Mongo Example](https://github.com/spgreenberg/spring-boot-cf-service-broker-mongo/tree/master/src/main/java/com/pivotal/cf/broker/mongodb/config)

* Application.java: The default configuration point.  This also allows you to run the app locally.

* BrokerConfig.java: This is required to ensure the `spring-boot-starter-cf-service-broker` jar file is searched for auto wired dependencies.  You can also explicitly configure the controllers and services if you prefer.

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

#### Security

The starter project includes the [spring-boot-starter-security](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-starters/spring-boot-starter-security) project.  See the documentation here for configuration options: [Spring boot security](http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-security)

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





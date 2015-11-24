spring-boot-cf-service-broker
=============================

Spring Boot project for creating Cloud Foundry service brokers.

# Overview

The goal is to provide a [Spring Boot](http://projects.spring.io/spring-boot/) project to quickly implement new [service brokers](http://docs.cloudfoundry.org/services/overview.html) in Cloud Foundry.

## Compatibility

* [service broker API](http://docs.cloudfoundry.org/services/api.html): 2.4
* [cf-release](https://github.com/cloudfoundry/cf-release): 192 or later
* [Pivotal CF](http://www.pivotal.io/platform-as-a-service/pivotal-cf): Expected 1.4

# Getting Started

See the [Spring Boot documentation](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#getting-started-first-application) for getting started building a Spring Boot application.

A sample [Mongo service broker](https://github.com/spgreenberg/spring-boot-cf-service-broker-mongo) project is available.

Create a new project for your broker and include the following in your `build.gradle` dependencies (be sure to set the version properties):

    dependencies {
        ...
        compile("org.cloudfoundry:spring-boot-cf-service-broker:${springBootCfServiceBrokerVersion}")
        testCompile("org.cloudfoundry:spring-boot-cf-service-broker-tests:${springBootCfServiceBrokerVersion}")
        testCompile("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
        ...
    }

The value of `springBootCfServiceBrokerVersion` corresponds to the service broker API you want write to (example 2.3).

## Latest

springBootCfServiceBrokerVersion: 2.5.0

As of version 2.4.1 we've changed the API in anticipation of async brokers. All service instance and binding calls now take request objects. This will allow us to support async and future service broker api changes without having to change method signatures, making this codebase and broker implementations easier to maintain.

As of version 2.4.1 we've changed the API in anticipation of async brokers. All service instance and binding calls now take request objects. This will allow us to support async and future service broker api changes without having to change method signatures, making this
codebase and broker implementations easier to maintain.

As of version 2.4.1 we've changed the API in anticipation of async brokers. All service instance and binding calls now take request objects. This will allow us to support async and future service broker api changes without having to change method signatures, making this
codebase and broker implementations easier to maintain.

# Configuring the broker

`spring-boot-cf-service-broker` provides default implementations of most of the components needed to implement a service broker. In Spring Boot fashion, you can override the default behavior by providing your own implementation of Spring beans, and `spring-boot-cf-service-broker` will back away from its defaults.

To start, use the `@EnableAutoConfiguration` annotation on the broker's main application class:

    @ComponentScan
    @EnableAutoConfiguration
    public class Application {
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    }

This will trigger the inclusion of `spring-boot-cf-service-broker` and its default configuration.

## Service beans

The Cloud Foundry service broker API has three main endpoint groupings: catalog management, service instance provisioning/deprovisioning, and service instance binding/unbinding. The broker will need to provide one Spring bean to provide the necessary functionality for each endpoint grouping.

For catalog management, `spring-boot-cf-service-broker` provides a default implementation that requires the broker to just provide an implementation of a [`Catalog` bean](src/main/java/org/cloudfoundry/community/servicebroker/model/Catalog.java). There is an example of this approach in the [MongoDB sample broker](https://github.com/spgreenberg/spring-boot-cf-service-broker-mongo/blob/master/src/main/java/org/cloudfoundry/community/servicebroker/mongodb/config/CatalogConfig.java). To override this default, provide your own bean that implements the [`CatalogService`](src/main/java/org/cloudfoundry/community/servicebroker/service/CatalogService.java) interface.

For service instance provisioning/deprovisioning, provide a Spring bean that implements the [`ServiceInstanceService`](src/main/java/org/cloudfoundry/community/servicebroker/service/ServiceInstanceService.java) interface. There is no default implementation provided by `spring-boot-cf-service-broker`.

For service instance binding/unbinding, provide a Spring bean that implements the [`ServiceInstanceBindingService`](src/main/java/org/cloudfoundry/community/servicebroker/service/ServiceInstanceBindingService.java) interface. There is no default implementation provided by `spring-boot-cf-service-broker`.

## Security

The project includes the [`spring-boot-starter-security`](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-starters/spring-boot-starter-security) project.  See the [Spring Boot Security documentation](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-security) for configuration options.

The default behavior creates a user called `user` with a generated password that is logged as an `INFO` message during app startup.  For example:

    2014-04-16T10:08:52.54-0600 [App/0]   OUT Using default password for application endpoints: 7c2969c1-d9c7-47e9-9c9e-2cd94a7b6cf1

If you are deploying your service broker to Cloud Foundry as an app, be aware the password is re-generated every time you push the application.  Therefore, you need to run `cf update-service-broker` with the new password after each push.

To see the generated password in the application logs on Cloud Foundry, use one of the following commands:

    $ cf logs <broker-app-name>
    $ cf logs --recent <broker-app-name>

## API version verification

By default, `spring-boot-cf-service-broker` will verify the version of the service broker API for each request it receives. To disable service broker API version header verification, provide a `BrokerApiVersion` bean that accepts any API version:

    @Bean
    public BrokerApiVersion brokerApiVersion() {
        return new BrokerApiVersion();
    }

# Deploying your broker

Follow the [documentation](http://docs.cloudfoundry.org/services/managing-service-brokers.html) to register the broker to Cloud Foundry.

# Model Notes

- The model is for the REST/Controller level.  It can be extended as needed.
- All models explicitly define serialization field names.
- Currently, `ServiceInstance` is used internally and not exposed by any controllers.

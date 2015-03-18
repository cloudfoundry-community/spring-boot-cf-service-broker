package org.cloudfoundry.community.servicebroker.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/** 
 * Default main for spring boot
 * @author sgreenberg
 *
 */
@SpringBootApplication
public class DefaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(DefaultApplication.class, args);
    }
    	
}
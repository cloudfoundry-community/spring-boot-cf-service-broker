package org.cloudfoundry.community.servicebroker.model.matchers;

import org.cloudfoundry.community.servicebroker.model.*;
import org.mockito.ArgumentMatcher;
import static org.mockito.Matchers.argThat;


/**
 * Request matcher which allows verification of Asynchronous requests from Cloud Controller. 
 * 
 * Example Usage:
 * <code>
 * when(serviceInstanceService.createServiceInstance(
 *	    		(CreateServiceInstanceRequest) anyAsyncRequest())).thenReturn(instance);
 *  </code>
 * @author jkruck
 *
 */
public class AsyncArgumentMatcher extends ArgumentMatcher<ServiceInstanceRequest> {
	
	public static ServiceInstanceRequest anyAsyncRequest() { 
		return argThat(new AsyncArgumentMatcher()); 
	}
	
	@Override
	public boolean matches(Object argument) {
        return true == ((ServiceInstanceRequest) argument)
                .hasAsyncClient();
	}

}
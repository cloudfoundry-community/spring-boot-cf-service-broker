package org.cloudfoundry.community.servicebroker.model.matchers;

import org.cloudfoundry.community.servicebroker.model.*;
import org.mockito.ArgumentMatcher;
import static org.mockito.Matchers.argThat;

/**
 * Request matcher which allows verification of Synchronous requests from Cloud Controller. 
 * 
 * Example Usage:
 * <code>
 * when(serviceInstanceService.createServiceInstance(
 *	    		(CreateServiceInstanceRequest) anySyncRequest())).thenReturn(instance);
 *  </code>
 * @author jkruck
 *
 */
public class SyncArgumentMatcher extends ArgumentMatcher<ServiceInstanceRequest> {
	
	public static ServiceInstanceRequest anySyncRequest() { 
		return argThat(new SyncArgumentMatcher()); 
	}
	
	@Override
	public boolean matches(Object argument) {
        return false == ((ServiceInstanceRequest) argument)
                .hasAsyncClient();
	}

}
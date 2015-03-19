package org.cloudfoundry.community.servicebroker.model.fixture;

import org.cloudfoundry.community.servicebroker.model.*;
import org.mockito.ArgumentMatcher;

public class AsyncArgumentMatcher extends ArgumentMatcher<ServiceInstanceRequest> {
	
	static AsyncArgumentMatcher isAsyncRequest() { 
		return new AsyncArgumentMatcher(); 
	}
	
	@Override
	public boolean matches(Object argument) {
		ServiceInstanceRequest request = (ServiceInstanceRequest) argument;
		return request.hasAsyncClient();
	}

}
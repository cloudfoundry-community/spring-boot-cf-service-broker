package org.cloudfoundry.community.servicebroker.model.fixture;

import org.cloudfoundry.community.servicebroker.model.*;
import org.mockito.ArgumentMatcher;

public class SyncArgumentMatcher extends ArgumentMatcher<ServiceInstanceRequest> {
	
	static SyncArgumentMatcher isSyncRequest() { 
		return new SyncArgumentMatcher(); 
	}
	
	@Override
	public boolean matches(Object argument) {
        return false == ((ServiceInstanceRequest) argument)
                .hasAsyncClient();
	}

}
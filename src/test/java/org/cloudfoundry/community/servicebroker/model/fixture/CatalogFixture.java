package org.cloudfoundry.community.servicebroker.model.fixture;

import org.cloudfoundry.community.servicebroker.model.Catalog;

public class CatalogFixture {

	public static Catalog getCatalog() {
		return new Catalog(ServiceFixture.getAllServices());
	}
	
}

package com.pivotal.cf.broker.model.fixture;

import com.pivotal.cf.broker.model.Catalog;

public class CatalogFixture {

	public static Catalog getCatalog() {
		return new Catalog(ServiceFixture.getAllServices());
	}
	
}

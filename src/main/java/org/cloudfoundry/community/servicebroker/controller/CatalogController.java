package org.cloudfoundry.community.servicebroker.controller;

import org.cloudfoundry.community.servicebroker.model.Catalog;
import org.cloudfoundry.community.servicebroker.service.CatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * See: Source: http://docs.cloudfoundry.com/docs/running/architecture/services/writing-service.html
 *
 * @author sgreenberg@gopivotal.com
 */
@RestController
public class CatalogController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(CatalogController.class);

	@Autowired
	public CatalogController(CatalogService service) {
		super(service);
	}

	@RequestMapping(value = "/v2/catalog", method = RequestMethod.GET)
	public Catalog getCatalog() {
		logger.debug("getCatalog()");
		return catalogService.getCatalog();
	}
}

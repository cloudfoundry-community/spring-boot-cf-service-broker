package org.cloudfoundry.community.servicebroker.model;


public class BrokerApiVersion {

	public final static String DEFAULT_API_VERSION_HEADER = "X-Broker-Api-Version";
	public final static String API_VERSION_ANY = "*";

	private String brokerApiVersionHeader;
	private String apiVersion;

	public BrokerApiVersion(String brokerApiVersionHeader, String apiVersion) {
		this.brokerApiVersionHeader = brokerApiVersionHeader;
		this.apiVersion = apiVersion;
	}

	public BrokerApiVersion(String apiVersion) {
		this(DEFAULT_API_VERSION_HEADER, apiVersion);
	}

	public BrokerApiVersion() {
		this(DEFAULT_API_VERSION_HEADER, API_VERSION_ANY);
	}

	public String getBrokerApiVersionHeader() {
		return brokerApiVersionHeader;
	}

	public String getApiVersion() {
		return apiVersion;
	}

}

package org.cloudfoundry.community.servicebroker.model;


public class BrokerApiVersion {

	private String brokerApiVersionHeader;
	private String apiVersion;
	
	public BrokerApiVersion(String brokerApiVersionHeader, String apiVersion) {
		this.brokerApiVersionHeader = brokerApiVersionHeader;
		this.apiVersion = apiVersion;
	}
	
	public String getBrokerApiVersionHeader() {
		return brokerApiVersionHeader;
	}
	
	public String getApiVersion() {
		return apiVersion;
	}
	
}

package org.cloudfoundry.community.servicebroker.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class BrokerApiVersion {

	public final static String DEFAULT_API_VERSION_HEADER = "X-Broker-Api-Version";
	public final static String API_VERSION_ANY = "*";

	private final String brokerApiVersionHeader;
	private final String apiVersion;

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
}

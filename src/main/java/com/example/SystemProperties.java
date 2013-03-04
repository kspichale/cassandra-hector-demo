package com.example;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class SystemProperties {

	private static final String DEFAULT_PROPERTIES_FILE = "default.properties";

	private final Configuration configuration;

	public SystemProperties() {
		this(DEFAULT_PROPERTIES_FILE);
	}

	public SystemProperties(String filename) {
		try {
			this.configuration = new PropertiesConfiguration(filename);
		} catch (ConfigurationException exp) {
			throw new RuntimeException("Could not load properties file.", exp);
		}
	}

	public String getCassandraListenAddress() {
		return getStringProperty("cassandra.listenAddress", "localhost");
	}

	public String getCassandraRpcPort() {
		return getStringProperty("cassandra.rpcPort", "9160");
	}

	public String getClusterName() {
		return getStringProperty("cassandra.clusterName", "Test Cluster");
	}

	public String getKeyspaceName() {
		return getStringProperty("cassandra.keyspaceName", "MyKeyspace");
	}

	public int getReplicationSize() {
		return getIntProperty("cassandra.replicationSize", 1);
	}

	private String getStringProperty(final String propertyName,
			final String defaultValue) {
		return System.getProperty(propertyName,
				configuration.getString(propertyName, defaultValue));
	}

	private int getIntProperty(final String propertyName, final int defaultValue) {
		final String value = getStringProperty(propertyName,
				String.valueOf(defaultValue));
		return Integer.valueOf(value);
	}
}

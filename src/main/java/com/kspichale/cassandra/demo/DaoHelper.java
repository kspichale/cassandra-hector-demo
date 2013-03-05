package com.kspichale.cassandra.demo;

import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoHelper {

	private final SystemProperties properties = new SystemProperties();

	private final Logger logger = LoggerFactory.getLogger(DaoHelper.class);

	public Cluster getCluster() {

		final String connectionString = properties.getCassandraListenAddress()
				+ ":" + properties.getCassandraRpcPort();

		logger.info("Connecting to cluster {}", connectionString);

		return HFactory.getOrCreateCluster(properties.getClusterName(),
				connectionString);
	}

	public Keyspace getKeyspace() {
		final Cluster cluster = getCluster();
		return HFactory.createKeyspace(properties.getKeyspaceName(), cluster);
	}

	public String createId() {
		final UUID timeUUID = TimeUUIDUtils.getTimeUUID(new Date().getTime());
		return timeUUID.toString();
	}
}

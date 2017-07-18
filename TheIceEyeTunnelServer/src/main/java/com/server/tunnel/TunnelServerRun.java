package com.server.tunnel;

import java.net.UnknownHostException;
import java.util.Optional;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * @author bishoysamir
 *
 */
public class TunnelServerRun {

	private static Logger log = Logger.getLogger(TunnelServerRun.class);
	final static String END_PROPERTY = "endpoint_url";
	private static final String INGEST_ENDPOINT_PROPERTY = "ingest_endpoint";
	private static final String ASSIGNMENT_ENDPOINT_PROPERTY = "assignment_endpoint";
	private static final String PORT_PROPERTY = "port";
	public static String ENDPOINT_URL;
	public static final String THANK_YOU = "Thank you for connecting to UnSupported Endpoint \nGoodbye!";
	public static String INGEST_ENDPOINT;
	public static String ASSIGNMENT_ENDPOINT;
	public static String PORT_VALUE;
    // load server port and endpoint from prperties file
	static {
		try {
			Properties properties = new Properties();
			properties.load(TunnelServerRun.class.getClassLoader().getResource("server.properties").openStream());
			INGEST_ENDPOINT = properties.getProperty(INGEST_ENDPOINT_PROPERTY);
			ENDPOINT_URL = properties.getProperty(END_PROPERTY);
			ASSIGNMENT_ENDPOINT = properties.getProperty(ASSIGNMENT_ENDPOINT_PROPERTY);
			PORT_VALUE = properties.getProperty(PORT_PROPERTY);
		} catch (Exception e) {
			log.error("*-*-*-*-*-*-TunnelServerRunProperties.staticInit()", e);
		}

	}

	// JUST START THE SERVER
	public static void main(String[] args) {

		PropertyConfigurator.configure("log4j.properties");
		try {
			Integer port = Integer.parseInt(Optional.ofNullable(PORT_VALUE).orElse("0"));
			Server server = new Server(port.intValue());
			server.startServer();
			log.info("Tunnel Server started");
			System.out.println("Tunnel Server started");
		} catch (UnknownHostException e) {
			log.info(e.getMessage());
		}
	}
}
package com.server.tunnel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dto.AssignmentInfo;

/**
 * 
 * @author bishoysamir
 *
 */
public class Server {


	int default_port = 4020;
	ServerSocket serverSocket;
	private static Logger log = Logger.getLogger(TunnelServerRun.class);

	public Server(int server_port) {
		super();
		this.default_port = (server_port == 0) ? default_port : server_port;
		try {
			serverSocket = new ServerSocket(default_port);
		} catch (IOException e) {
			log.error("Starting Server with Port " + server_port + " Error : " + e.getMessage());
		}
	}

	/*
	 * START SERVER
	 */
	public void startServer() throws UnknownHostException {
		try {
			while (true) {
				log.info("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				log.info("Tunnel Server started");
				System.out.println("Tunnel Server started");
				// TODO USING SSLSOCKET FOR MORE SECURITY BETWEEN CLIENT AND
				// SERVER
				Socket server = serverSocket.accept();
				log.info("Just connected to " + server.getRemoteSocketAddress());

				ObjectOutputStream toClient = new ObjectOutputStream(server.getOutputStream());
				BufferedReader fromClient = new BufferedReader(new InputStreamReader(server.getInputStream()));
				String line = fromClient.readLine();

				if (null != line && !line.isEmpty()) {
					callEndPoint(line, toClient, fromClient);
				}

				System.out.println("Server received: " + line);
				log.info("Server received: " + line);
				log.info("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");
			}
		} catch (IOException | RuntimeException c) {
			log.error("ERROR: " + c.getMessage());
		}
	}

	/*
	 * used to choose which endpoint will be excuted
	 */
	private void callEndPoint(String line, ObjectOutputStream toClient, BufferedReader fromClient) throws IOException {

		if (line.equalsIgnoreCase(TunnelServerRun.ASSIGNMENT_ENDPOINT)) {
			wrtieAssignment(toClient);
		} else if (line.equalsIgnoreCase(TunnelServerRun.INGEST_ENDPOINT)) {
			writeIngest(toClient, fromClient);
		} else {
			toClient.writeUTF(TunnelServerRun.THANK_YOU);
		}

	}

	/*
	 * write ingest endpoint data to client machine
	 */
	private void writeIngest(ObjectOutputStream toClient, BufferedReader fromClient) throws IOException {
		String userTextValue = fromClient.readLine();
		String Url = getIngest(userTextValue);
		toClient.writeObject(Url);
	}

	/*
	 * get ingest endpoint data
	 */
	private String getIngest(String userTextValue) throws IOException {
		String Url = getImageUrl(userTextValue);
		return Url;
	}

	/*
	 * get image path
	 */
	private String getImageUrl(String userTextValue) throws IOException {
		final String dir = System.getProperty("user.dir");
		final String imagePath = dir + "/decode/yourImage.jpg";
		ImageHelper helper = new ImageHelper();
		helper.writeImageToPath(userTextValue, imagePath);
		return imagePath;
	}

	/*
	 * write assignment data to client machine
	 */
	private void wrtieAssignment(ObjectOutputStream toClient) throws IOException {
		List<AssignmentInfo> assignmentList = getAssignmentData();
		toClient.writeObject(assignmentList);
	}

	/*
	 * get assignment data
	 */
	private List<AssignmentInfo> getAssignmentData() {
		String result = getJson(TunnelServerRun.ENDPOINT_URL);
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<AssignmentInfo>>() {
		}.getType();
		List<AssignmentInfo> assignmentList = gson.fromJson(result, listType);
		Collections.reverse(assignmentList);
		return assignmentList;
	}

	/*
	 * get json from url
	 */
	public String getJson(String requestURL) {
		URL wikiRequest;
		String response = "";
		try {
			wikiRequest = new URL(requestURL);
			Scanner scanner = new Scanner(wikiRequest.openStream());
			response = scanner.useDelimiter("\\Z").next();
			scanner.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return response;
	}

}

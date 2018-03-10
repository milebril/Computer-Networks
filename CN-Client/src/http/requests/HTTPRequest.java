package http.requests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import http.responses.HTTPImageResponse;
import http.responses.HTTPResponse;
import http.responses.HTTPTextResponse;

public class HTTPRequest {
	
	protected static Socket clientSocket;
	protected static InputStream inputStream;
	protected static Request request;
	
	protected HTTPResponse httpResponse;
	
	public HTTPRequest() {
		
	}
	
	/**
	 * Depending on what command is given, different Requests will be made
	 * @param request
	 * @throws IOException
	 */
	public HTTPRequest(Request request) throws IOException {
		this.request = request;

		switch (request.getCommand()) {
		case "HEAD": 
			httpResponse = HTTPHeadRequest.getHeadRequest(request);
			break;
		case "GET":
			httpResponse = HTTPImageRequest.getImageRequest(request);
			break;
		case "PUT":
			makePutConnection();
			inputStream = clientSocket.getInputStream();
			httpResponse = new HTTPResponse(inputStream);
			clientSocket.close();
			httpResponse.printHeader();
			break;
		case "POST":
			break;
		}
	}
	
	/**
	 * Create A header to send to the Server
	 * @param type The type of files to Accept
	 * @param request The Information about a request
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	protected static void createHeader(String type, Request request) throws UnknownHostException, IOException {
		clientSocket = new Socket(request.getURI(), request.getPort());
		OutputStream output = clientSocket.getOutputStream();
		PrintWriter writer = new PrintWriter(output, true);
		
		System.out.println(request.getCommand() + request.getPath() + " HTTP/1.1\r");
		
		writer.println(request.getCommand() + " /" + request.getPath() + " HTTP/1.1\r");
		writer.println("Host: " + request.getURI() + ":" + request.getPort() +"\r");
		writer.println("Accept: " + type + "\r");
		writer.println("Connection: close\r");
		writer.println("\r");
	}
	
	public HTTPResponse getResponse() {
		return httpResponse;
	}

	public void makePutConnection() {
		String toSend = HtmlToString(new File("res/put/test.html"));
		System.out.println("Sending " + toSend);
		try {
			clientSocket = new Socket(request.getURI(), request.getPort());
			OutputStream otherOutput = clientSocket.getOutputStream();
	        PrintWriter otherWriter = new PrintWriter(otherOutput, true);
	        //System.out.println(request.getCommand() +" " + request.getPath() + " HTTP/1.1\r");
	        otherWriter.println(request.getCommand() +" " + request.getPath() + " HTTP/1.1\r");
	        otherWriter.println("Host: " + request.getURI() + "\r");
	        otherWriter.println("Connection: Keep-Alive" + "\r");
	        otherWriter.println("Content-type: text/html\r");
	        otherWriter.println("\r");
	        
	        otherWriter.print(toSend);
	        otherWriter.println("\r");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String HtmlToString(File file) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(file));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str + "\r\n");
		    }
		    in.close();
		} catch (IOException e) {
		}
		String content = contentBuilder.toString();
		return content;
	}	
}

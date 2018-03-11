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
		switch (request.getCommand()) {
		case "HEAD": 
			httpResponse = HTTPHeadRequest.getHeadRequest(request);
			break;
		case "GET":
			httpResponse = HTTPGetRequest.getImageRequest(request);
			break;
		case "PUT":
			httpResponse = HTTPPutRequest.getPutRequest(request);
			break;
		case "POST":
			httpResponse = HTTPPostRequest.getPostRequest(request);
			break;
		case "GETCOFFEE":
			httpResponse = HTTPGetCoffeeRequest.getGetCoffeeRequest(request);
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
	protected static Socket createHeader(String type, Request request) throws UnknownHostException, IOException {
		clientSocket = new Socket(request.getURI(), request.getPort());
		OutputStream output = clientSocket.getOutputStream();
		PrintWriter writer = new PrintWriter(output, true);
		
		
		if (request.getPath().startsWith("/")) {
			System.out.println(request.getCommand() + " " + request.getPath() + " HTTP/1.1\r");
			writer.println(request.getCommand() + " " + request.getPath() + " HTTP/1.1\r");
		} else {
			System.out.println(request.getCommand() + " /" + request.getPath() + " HTTP/1.1\r");
			writer.println(request.getCommand() + " /" + request.getPath() + " HTTP/1.1\r");
		}
		writer.println("Host: " + request.getURI() + ":" + request.getPort() +"\r");
		writer.println("Accept: " + type + "\r");
		writer.println("Connection: close\r");
		writer.println("\r");
		
		return clientSocket;
	}
	
	/**
	 * Creating the body of a PUT or POST message, ending with a blank line
	 * @param socket the socket to send data through
	 * @param toSend the string that needs to be the body
	 * @throws IOException
	 */
	protected static void createTextMessageBody(Socket socket, String toSend) throws IOException {
		OutputStream output = clientSocket.getOutputStream();
		PrintWriter writer = new PrintWriter(output, true);
		writer.println(toSend);
		writer.println("\r");
	}
	
	/**
	 * Reads the given HTML file and appends a String such that the string contains the whole file to send
	 * @param file
	 * @return
	 */
	protected static String HtmlToString(File file) {
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
	
	public HTTPResponse getResponse() {
		return httpResponse;
	}
}

package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class HTTPRequest {
	
	private Socket clientSocket;
	private InputStream inputStream;
	private Request request;
	
	private HTTPResponse httpResponse;
	
	public HTTPRequest(Request request) throws IOException {
		this.request = request;
		
		switch (request.getCommand()) {
		case "HEAD": 
			clientSocket = new Socket(request.getURI(), request.getPort());
			OutputStream headOutput = clientSocket.getOutputStream();
			PrintWriter headWriter = new PrintWriter(headOutput, true);
			System.out.println(request.getCommand() + " /" + request.getPath() + " HTTP/1.1\r");
			headWriter.println(request.getCommand() + " /" + request.getPath() + " HTTP/1.1\r");
			headWriter.println("Host: " + request.getURI() + ":" + request.getPort() +"\r");
			headWriter.println("Accept: text/html\r");
			headWriter.println("Connection: close\r");
			headWriter.println("\r");
			inputStream = clientSocket.getInputStream();
			httpResponse = new HTTPResponse(inputStream);
			clientSocket.close();
			httpResponse.printHeader();
			break;
		case "GET":
			if (request.getPath().endsWith("html") || request.getPath().endsWith("css") || request.getPath() == "") {
				makeGETConnection("text");
				inputStream = clientSocket.getInputStream();
				httpResponse = new HTTPTextResponse(request, inputStream);
				clientSocket.close();
			} else if (request.getPath().endsWith("png") || request.getPath().endsWith("jpg")) {
				makeGETConnection("image");
				inputStream = clientSocket.getInputStream();
				httpResponse = new HTTPImageResponse(request, inputStream);
				clientSocket.close();
			} else {
				makeGETConnection("other");
				System.out.println("Not supported extension at request: " + request.getCommand() + " " + 
						request.getURL());
			}
			break;
		case "PUT":
			break;
		case "POST":
			break;
		}
	}
	
	public HTTPResponse getResponse() {
		return httpResponse;
	}

	private void makeGETConnection(String type) {
		try {
			switch(type) {
			case "image":
				clientSocket = new Socket(request.getURI(), request.getPort());
	            OutputStream imageOutput = clientSocket.getOutputStream();
	            PrintWriter imageWriter = new PrintWriter(imageOutput, true);
	            System.out.println(request.getCommand() + " " + request.getPath() + " HTTP/1.1\r");
	            imageWriter.println(request.getCommand() + " " + request.getPath() + " HTTP/1.1\r");
	            imageWriter.println("Host: " + request.getURI() + ":" + request.getPort() +"\r");
	            imageWriter.println("Connection: close\r");
	            imageWriter.println("\r");
	            break;
			case "text":
				clientSocket = new Socket(request.getURI(), request.getPort());
	            OutputStream textOutput = clientSocket.getOutputStream();
	            PrintWriter textWriter = new PrintWriter(textOutput, true);
	            System.out.println(request.getCommand() +" /" + request.getPath() + " HTTP/1.1\r");
	            textWriter.println(request.getCommand() +" /" + request.getPath() + " HTTP/1.1\r");
	            textWriter.println("Host: " + request.getURI() + "\r");
	            textWriter.println("Accept: text/html\r");
	            textWriter.println("Connection: close" + "\r");
	            textWriter.println("\r");
	            break;
			case "other":
				clientSocket = new Socket(request.getURI(), request.getPort());
	            OutputStream otherOutput = clientSocket.getOutputStream();
	            PrintWriter otherWriter = new PrintWriter(otherOutput, true);
	            System.out.println(request.getCommand() +" " + request.getPath() + " HTTP/1.1\r");
	            otherWriter.println(request.getCommand() +" " + request.getPath() + " HTTP/1.1\r");
	            otherWriter.println("Host: " + request.getURI() + "\r");
	            otherWriter.println("Connection: close" + "\r");
	            otherWriter.println("\r");
	            break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class HTTPRequest {
	
	private Socket clientSocket;
	private Request request;
	
	private HTTPResponse httpResponse;
	
	public HTTPRequest(Request request) throws IOException {
		this.request = request;
		
		if (request.getPath().endsWith("html") || request.getPath().endsWith("css") || request.getPath() == "") {
			makeConnection("text");
			InputStream inputStream = clientSocket.getInputStream();
			httpResponse = new HTTPTextResponse(request, inputStream);
			clientSocket.close();
		} else if (request.getPath().endsWith("png") || request.getPath().endsWith("jpg")) {
			makeConnection("image");
			InputStream inputStream = clientSocket.getInputStream();
			httpResponse = new HTTPImageResponse(request, inputStream);
			clientSocket.close();
		} else {
			System.out.println("Not supported extension at request: " + request.getCommand() + " " + 
					request.getURL());
		}
	}
	
	public HTTPResponse getResponse() {
		return httpResponse;
	}

	private void makeConnection(String type) {
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
	            imageWriter.println();
	            break;
			case "text":
				clientSocket = new Socket(request.getURI(), request.getPort());
	            OutputStream textOutput = clientSocket.getOutputStream();
	            PrintWriter textWriter = new PrintWriter(textOutput, true);
	            System.out.println(request.getCommand() +" /" + request.getPath() + " HTTP/1.1\r");
	            textWriter.println(request.getCommand() +" /" + request.getPath() + " HTTP/1.1");
	            textWriter.println("Host: " + request.getURI() + "\r");
	            textWriter.println("Connection: close" + "\r");
	            textWriter.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

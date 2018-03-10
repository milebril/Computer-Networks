package http.requests;

import java.io.IOException;
import java.net.UnknownHostException;

import http.responses.HTTPResponse;

public class HTTPHeadRequest extends HTTPRequest{
	
	public static HTTPResponse getHeadRequest(Request request) throws UnknownHostException, IOException {
		createHeader("text/http", request);
		
		inputStream = clientSocket.getInputStream();
		return new HTTPResponse(inputStream);
	}
	
}

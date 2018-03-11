package http.requests;

import java.io.IOException;
import java.net.UnknownHostException;

import http.responses.HTTPResponse;

public class HTTPDeleteRequest extends HTTPRequest{

	/**
	 * Deletes a file on the server
	 * @param request
	 * @return returns a HTTPResponse-header
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static HTTPResponse getDeleteRequest(Request request) throws UnknownHostException, IOException {
		createHeader("*/*", request);
		inputStream = clientSocket.getInputStream();
		return new HTTPResponse(inputStream);
	}
	
}

package http.requests;

import java.io.IOException;
import java.net.UnknownHostException;

import http.responses.HTTPImageResponse;
import http.responses.HTTPOptionsResponse;
import http.responses.HTTPResponse;

public class HTTPOptionsRequest extends HTTPRequest{
	
	/**
	 * Gets an OPTION request.
	 * @param request
	 * @return returns All the possible commands an what they do
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static HTTPResponse getOptionsRequest(Request request) throws UnknownHostException, IOException {
		createHeader("text/html", request);
		inputStream = clientSocket.getInputStream();
		return new HTTPOptionsResponse(request, inputStream);
	}
	
}

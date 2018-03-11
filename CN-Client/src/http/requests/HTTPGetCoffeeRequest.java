package http.requests;

import java.io.IOException;
import java.net.UnknownHostException;

import http.responses.HTTPImageResponse;
import http.responses.HTTPResponse;

public class HTTPGetCoffeeRequest extends HTTPRequest{
	
	/**
	 * Gets a custom made GETCOFFEE request which returns a funny header and image
	 * @param request
	 * @return returns a HTTPImageResponse because we get an image
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static HTTPResponse getGetCoffeeRequest(Request request) throws UnknownHostException, IOException {
		createHeader("image/png", request);
		inputStream = clientSocket.getInputStream();
		return new HTTPImageResponse(request, inputStream);
	}
	
}

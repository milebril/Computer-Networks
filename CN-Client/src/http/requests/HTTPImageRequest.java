package http.requests;

import java.io.IOException;
import java.net.UnknownHostException;

import http.responses.HTTPImageResponse;
import http.responses.HTTPResponse;
import http.responses.HTTPTextResponse;

public class HTTPImageRequest extends HTTPRequest{
	
	/**
	 * Handels the correct get type
	 * @param request passed from HTTPRequest to avoid NullPointers
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static HTTPResponse getImageRequest(Request request) throws UnknownHostException, IOException {
		if (request.getPath().endsWith("html") || request.getPath() == "") {
			createHeader("text/html", request);
			inputStream = clientSocket.getInputStream();
			return new HTTPTextResponse(request, inputStream);
		} else if (request.getPath().endsWith("css")) {
			createHeader("text/css", request);
			inputStream = clientSocket.getInputStream();
			return new HTTPTextResponse(request, inputStream);
		} else if (request.getPath().endsWith("png")) {
			createHeader("text/png", request);
			inputStream = clientSocket.getInputStream();
			return new HTTPImageResponse(request, inputStream);
		} else if (request.getPath().endsWith("jpg")) {
			createHeader("text/jpg", request);
			inputStream = clientSocket.getInputStream();
			return new HTTPImageResponse(request, inputStream);
		}else {
			//makeGETConnection("other");
			System.out.println("Not supported extension at request: " + request.getCommand() + " " + 
					request.getURL() + request.getPath());
		}
		return null;
	}
	
}

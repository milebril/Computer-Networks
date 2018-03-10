package http.requests;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import http.responses.HTTPResponse;

public class HTTPPutRequest extends HTTPRequest{

	/**
	 * Makes an HTTP PUT request, with the given arguments which are passed in request
	 * @param request
	 * @return Returns a HTTPResponse containing the header of the response from the server
	 */
	public static HTTPResponse getPutRequest(Request request) {
		try {
			String toSend = HtmlToString(new File("res/" + request.getPath()));
			
			Socket socket = createHeader("text/hml", request);
			createTextMessageBody(socket, toSend);
			
			return new HTTPResponse(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

package http.requests;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import http.responses.HTTPResponse;

public class HTTPPostRequest extends HTTPRequest{

	public static HTTPResponse getPostRequest(Request request) {
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

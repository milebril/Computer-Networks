import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import HTTP.CoffeeRequest;
import HTTP.DeleteRequest;
import HTTP.GetRequest;
import HTTP.HeadRequest;
import HTTP.Header;
import HTTP.OptionsRequest;
import HTTP.PostRequest;
import HTTP.PutRequest;

public class WriteToClient {
	
	/**
	 * constructor
	 */
	public WriteToClient() {

	}

	/**
	 * creates the header and in some cases a body and writes these to the client
	 * @param req
	 * @param res
	 * @param clientSocket
	 */
	public boolean CreateHeader(BufferedReader req, BufferedWriter res, Socket clientSocket)
			throws IOException, ParseException, SocketException, Exception {
		
		String requestedString = null;
		requestedString = req.readLine(); //first line in the request
		String requestedHead = requestedString + "\n"; 		
		String path = "";
		String filetype = "";
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date LastModifiedSince = new Date();
		
		if (requestedString == null || requestedString.equals("")) { // close the socket when the request is empty
			clientSocket.close(); // close connection to client when request is empty.
			return false;
		}
		
		path = requestedString.split(" ")[1].substring(0); // path to file
		if (path.equals("/"))
			path = "/index.html"; // change / to /index.html
		if (requestedString.contains(".") && !path.isEmpty())
			filetype = path.substring(path.lastIndexOf(".") + 1); // the type of the file

		String command = requestedString.split(" ")[0]; //  command from the client

		// input from client
		while (!(requestedString = req.readLine()).equals("")) {
//			requestedString = req.readLine();
			if (requestedString.startsWith("If-Modified-Since: ")) {
				LastModifiedSince = sdf.parse(requestedString.substring(requestedString.indexOf(" ") + 1));
			}	
			requestedHead = requestedHead + requestedString + "\n";
		}
		System.out.println(requestedHead);
		Header head = new Header();
		
		
		//responds according to the given command
		switch (command) {
		case "HEAD": //returns the header to the client
			HeadRequest Headrequest = new HeadRequest(path, 0, LastModifiedSince, filetype, head);
			res.write(Headrequest.getHeader().getHeader().toString());
			res.write(Headrequest.getHeader().getHeader().toString());
			break;
		case "PUT": // overwrites or creates a given file
			PutRequest PutRequest = new PutRequest(req, path, 0, LastModifiedSince, filetype, head);
			res.write(PutRequest.getHeader().getHeader().toString());
			break;
		case "POST": // adds data to a given file
			PostRequest PostRequest = new PostRequest(req, path, 0, LastModifiedSince, filetype, head);
			res.write(PostRequest.getHeader().getHeader().toString());
			break;
		case "GET": //returns a file to the client
			GetRequest GetRequest = new GetRequest(req, path, 0, LastModifiedSince, filetype, head, clientSocket);
			if (!filetype.equals("jpg") && !filetype.equals("png")) {
				res.write(GetRequest.getHeader().getHeader().toString());
				if (GetRequest.getBody() != null) {
					res.write(GetRequest.getBody());
				}
			}	
			break;
		case "GETCOFFEE": // Just a fun 418 I'm a teapot
			CoffeeRequest CoffeeRequest = new CoffeeRequest(head, filetype, clientSocket);
			break;
		case "DELETE": // Deletes a given file
			DeleteRequest DeleteRequest = new DeleteRequest(path, 0, LastModifiedSince, filetype, head);
			res.write(DeleteRequest.getHeader().getHeader().toString());
			break;
		case "OPTIONS": // returns the options implemented
			OptionsRequest OptionsRequest = new OptionsRequest(head, 0, command);
			res.write(OptionsRequest.getHeader().getHeader().toString());
			res.write(OptionsRequest.getBody().toString());
			break;	
		default: // in the default case we consider it the fault of the client
			head.setHeader(400, filetype, 0, null);
			res.write(head.getHeader().toString()); //TODO Checken uit request header
			break;
		}
		
		return true;
	}
}

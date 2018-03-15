import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import HTTP.GetRequest;
import HTTP.HeadRequest;
import HTTP.Header;
import HTTP.PostRequest;
import HTTP.PutRequest;

public class WriteToClient {
	public WriteToClient() {

	}

	public void CreateHeader(BufferedReader req, BufferedWriter res, Socket clientSocket)
			throws IOException, ParseException {
		String requestedString;
		requestedString = req.readLine();
		String header = requestedString + "\n";
		String path = "";
		String filetype = "";
		SimpleDateFormat sdf = new SimpleDateFormat(" EEE, dd MMM yyyy HH:mm:ss z");
		Date LastModifiedSince = new Date();
		
		if (requestedString == null) {
			clientSocket.close(); // close connection to client when request is empty.
			return;
		}
		
		path = requestedString.split(" ")[1].substring(0); // path to file
		if (path.equals("/"))
			path = "/index.html"; // change / to /index.html
		if (requestedString.contains(".") && !path.isEmpty())
			filetype = path.substring(path.lastIndexOf(".") + 1); // the type of the file

		String command = requestedString.split(" ")[0]; // GET,PUT,HEAD,POST,DELETE,OPTIONS command

		// input from client
		while (!requestedString.equals("")) {
			requestedString = req.readLine();
			if (requestedString.startsWith("If-Modified-Since: ")) {
				LastModifiedSince = sdf.parse(requestedString.substring(requestedString.lastIndexOf(" ") + 1));
			}	
			header = header + requestedString + "\n";
		}
		System.out.println(header);
		Header head = new Header();
		switch (command) {
		case "HEAD":
			HeadRequest Headrequest = new HeadRequest(path, 0, LastModifiedSince, filetype, head);
			res.write(Headrequest.getHeader().getHeader().toString());
			res.write(Headrequest.getHeader().getHeader().toString());
			break;
		case "PUT":
			PutRequest PutRequest = new PutRequest(req, path, 0, LastModifiedSince, filetype, head);
			res.write(PutRequest.getHeader().getHeader().toString());
			break;
		case "POST":
			PostRequest PostRequest = new PostRequest(req, path, 0, LastModifiedSince, filetype, head);
			res.write(PostRequest.getHeader().getHeader().toString());
			break;
		case "GET":
			GetRequest GetRequest = new GetRequest(req, path, 0, LastModifiedSince, filetype, head, clientSocket);
			System.out.println(GetRequest.getHeader());
			if (!filetype.equals("jpg") && !filetype.equals("png")) {
				res.write(GetRequest.getHeader().getHeader().toString());
				if (GetRequest.getBody() != null)
					res.write(GetRequest.getBody());
			}
			break;
		case "GETCOFFEE":
		case "DELETE":
		case "OPTIONS":
		}
	}
}

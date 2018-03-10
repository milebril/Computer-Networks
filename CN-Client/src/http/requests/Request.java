package http.requests;

import java.net.MalformedURLException;
import java.net.URL;

import http.errors.notEnoughArgumentsException;

public class Request {
	
	//Used to check whether a valid commando is passed
	private String[]  validCommandos = new String[] {
			"GET", "HEAD", "POST", "PUT"
	};
	
	//URL stuff
	private String command;
	private URL URL;
	private String URI;
	private String path;
	
	//Port number (80 if no otherway specified)
	private int port;
	
	/**
	 * Build a reauest which is used to make a request to the server
	 * @param command the commando to use
	 * @param URL url to connect to
	 * @param port port to connect to
	 * @throws notEnoughArgumentsException
	 * @throws MalformedURLException
	 */
	public Request(String command, String URL, int port) throws notEnoughArgumentsException, MalformedURLException {
		if (validCommand(command)) {
			this.command = command;
		} else {
			throw new notEnoughArgumentsException();
		}
		
		this.URL = new URL(URL);
		this.URI = this.URL.getHost();
		this.path = this.URL.getPath();

		if (port < 9999) {
			this.port = port;
		} else {
			throw new notEnoughArgumentsException();
		}
	}

	public Request(String command, String URL) throws notEnoughArgumentsException, MalformedURLException{
		this(command, URL, 80);
	}
	
	public String getCommand() {
		return command;
	}

	public URL getURL() {
		return URL;
	}

	public String getURI() {
		return URI;
	}
	
	public String getPath() {
		return path;
	}

	public int getPort() {
		return port;
	}

	/**
	 * checks whether a valid command is given
	 * @param command
	 * @return
	 */
	private boolean validCommand(String command) {
		for (int i = 0; i < validCommandos.length; i++) {
			if (validCommandos[i].equals(command)) {
				return true;
			}
		}
		
		return false;
	}
	
}

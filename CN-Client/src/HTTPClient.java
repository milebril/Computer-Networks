import java.io.*;
import java.net.*;

public class HTTPClient {
	
	private static String HTTPCommand;
	private static String URI;
	private static int port;
	
	private static final String[] vallidCommands = {"HEAD", "GET", "PUT", "POST"};
	
	//Inputs: HTTPCommand URI Port
	public static void main(String[] args) throws UnknownHostException, IOException {
		HTTPCommand = args[0];
		if (!validCommand(HTTPCommand)) {
			System.err.println("Incorrect HTTPCommand");
			System.exit(1);
		}
		
		URL url = new URL(args[1]);
	    URI = url.getHost();
		System.out.println(URI);
	    
		if (args.length == 2) {
			port = Integer.parseInt(args[2]);
		} else {
			port = 80; //Use Port 80 as standard when no port is given
		}
		
		 //InetAddress addr = InetAddress.getByName("www.google.com");
		
		    Socket clientSocket = new Socket(URI, port);
		    boolean autoflush = true;
		    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), autoflush);
		    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    // send an HTTP request to the web server
		    out.println(HTTPCommand + " / HTTP/1.1");
		    //out.println("Host: www.google.com:80");
		    out.println("Connection: Close");
		    out.println();
		    	
		    File f = new File("res/output.html");
		    BufferedWriter bf = new BufferedWriter(new FileWriter(f));
		    boolean more = true;
		    String input;
		    while (more) {
		      input = in.readLine();
		      if (input == null)
		        more = false;
		      else {
		    	  	System.out.println(input);
		        bf.write(input);
		      }
		    }
		    bf.close();
		    
		    //System.out.println(sb.toString());
		    clientSocket.close();
		  
	}
	
	/**
	 * Checks whether the given HTTPCommand is a valid command.
	 * 
	 * @param command the HTTPCommand to check
	 * @return true if we have a valid HTTPCommand
	 */
	private static boolean validCommand(String command) {
		for (int i = 0; i < vallidCommands.length; i++) {
			if (command.equals(vallidCommands[i])) {
				return true;
			}
		}
		return false;
	}
	
}

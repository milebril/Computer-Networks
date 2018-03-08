import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HTTPclient{

	/**
	 * An arrayList consisting of the valid commands in HTTP.
	 */
	private static final ArrayList<String> listOffCommands = new ArrayList<String>() {{
		add("GET");
		add("PUT");
		add("HEAD");
		add("POST");
	}};
	
	/**
	 * TODO documentation
	 * @param args 
	 * args =  the command line
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
        if (args.length < 2) return; // return when no arguments are given 
        
        String command = " "; 
        
        if (listOffCommands.contains(args[0])) {
        	command = args[0]; // set the request command if it is valid      
        }else {
        	throw new Exception ("command not available");
        }
        
        URL url;
        try { // sets the URl if it is valid
            url = new URL(args[1]);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return;
        }  
        
        int port;     
        if (args.length == 3) { 
			port = Integer.parseInt(args[2]); // sets the port if one is given
		} else {
			port = 80; //Use Port 80 as standard when no port is given
		}
        
        
 
        String hostname = url.getHost(); 

        try {
        	Socket socket = new Socket(hostname, port);
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
 
            writer.println(command +" /" + url.getPath() + " HTTP/1.1");
            writer.println("Host: " + hostname);
           // writer.println("User-Agent: Simple Http Client");
           // writer.println("Accept: text/html");
           // writer.println("Accept-Language: en-US");
            writer.println("Connection: close");
            writer.println();
 
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
            String line;             
            File HTMLfile = new File("../res/output.html");
		    BufferedWriter HTMLwriter = new BufferedWriter(new FileWriter(HTMLfile));

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                HTMLwriter.write(line);
                HTMLwriter.newLine();
            }
            HTMLwriter.close();
            
        } catch (UnknownHostException ex) {
 
            System.out.println("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
        SearchImages();
    }
	
		 public static void SearchImages() throws Exception{
				File input = new File("res/output.html");
				Document doc = Jsoup.parse(input, "UTF-8", " ");	
				
				for (Element e : doc.select("img")) {
				    System.out.println(e.attr("src"));
				}
			}
	
}

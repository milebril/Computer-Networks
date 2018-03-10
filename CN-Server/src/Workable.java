import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Workable implements Runnable {

	Socket clientSocket = null;
	int size;
	
	/**
	 * constructor
	 * @param client
	 */
	public Workable(Socket client) {
		this.clientSocket = client;
	}

	@Override
	public void run() {
		 try {
		 	 	System.out.println(Thread.currentThread().getName());
			 	BufferedReader request = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
				BufferedWriter response = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));								
				String requestedString = request.readLine();
				String header = requestedString + "\n";								
				String path = "";
				String filetype = "";
				
				if(!requestedString.isEmpty()) path = requestedString.split(" ")[1].substring(0);
				if(path.equals("/"))path = "/index.html";  // change / to /index.html								
				if(requestedString.contains(".") && !path.isEmpty()) filetype = path.substring(path.lastIndexOf("." ) + 1);
				
				String command = requestedString.split(" ")[0];				
				while(!requestedString.isEmpty()) {
					requestedString = request.readLine();					
					header = header + requestedString + "\n";					
				}
				System.out.println(header);
				
				//client does something wrong
				if (badRequest()) {
					StringBuilder head = getHeader(400,filetype);
					response.write(head.toString());			
					response.flush();
				}
				//file found
				else if (command.equals("GET") &&  (new File("../res" + path).exists())) {				
					StringBuilder head = getHeader(200,filetype);
					System.out.println(head);
					File HTMLfile = new File("../res" + path);
					System.out.println(HTMLfile);
					this.size = HtmlToString(HTMLfile).length();
					response.write(head.toString());
					response.write(HtmlToString(HTMLfile));				
					response.flush();
				}
				//file not found
				else if (command.equals("GET") &&  (!new File("../res" + path).exists())) {				
					StringBuilder head = getHeader(404,filetype);
					response.write(head.toString());			
					response.flush();
				}
				//When everything else fails, consider it an internal error
				else {
					StringBuilder head = getHeader(500,filetype);
					response.write(head.toString());			
					response.flush();
				}
				
				request.close();
				response.close();
	        } catch (IOException e) {           
	            
	        }
	}
	
	public Boolean badRequest() {
		return false;
	}
	
	/**
	 * changes an html file to a single string
	 * @param file
	 * @return string containing the text of an html file
	 */
	public String HtmlToString(File file) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(file));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str + "\r\n");
		    }
		    in.close();
		} catch (IOException e) {
		}
		String content = contentBuilder.toString();
		return content;
	}	
	
	/**
	 * returns the response header 
	 * @param code
	 * @return response header
	 */
	public StringBuilder getHeader(int code, String type) {
		StringBuilder head = new StringBuilder();
		String filetype = "";
		if(type.equals("png")) filetype = "image/png\r\n";
		else if(type.equals("jpg")) filetype = "image/jpg\r\n";
		else if(type.equals("html")) filetype = "text/html\r\n";
		else if(type.equals("css"))	filetype = "text/css\r\n";
		else if(type.equals("gif"))	filetype = "image/gif\r\n";
		else if(type.equals("js"))	filetype = "text/javascript\r\n";
		else filetype = "text/html\r\n";
		
		switch (code) {
		case 200:
			head.append("HTTP/1.1 200 OK\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("Content-Type: " + filetype);
			if(type.equals("jpg") || type.equals("png")) head.append("Content-Length: " + this.size + "\r\n");
			head.append("Connection: Closed\r\n\r\n");
			break;
		case 404:
			head.append("HTTP/1.1 404 Not Found\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("\r\n");
		case 400:
			head.append("HTTP/1.1 304 Not Modified\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("\r\n");
		case 500:
			head.append("HTTP/1.1 500 Internal Server Error\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("\r\n");
		case 304:
			head.append("HTTP/1.1 400 Bad Request\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("\r\n");
		}return head;
	}

	/**
	 * returns the current date and time 
	 * @return string containing the date and time
	 */
	private static String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
}

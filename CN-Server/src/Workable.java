import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.omg.CORBA.portable.InputStream;

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
		 	 	System.out.println(Thread.currentThread().getName()); //prints out the current thread
			 	BufferedReader request = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
				BufferedWriter response = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
				
				String requestedString = request.readLine(); 
				String header = requestedString + "\n";								
				String path = "";
				String filetype = "";
				
				if(!requestedString.isEmpty()) path = requestedString.split(" ")[1].substring(0); //path to file
				if(path.equals("/"))path = "/index.html";  // change / to /index.html								
				if(requestedString.contains(".") && !path.isEmpty()) filetype = path.substring(path.lastIndexOf("." ) + 1); //the type of the file
				
				String command = requestedString.split(" ")[0];	//GET,PUT,HEAD,POST command	
				
				//input the client
				while(!requestedString.equals("")) {
					requestedString = request.readLine();					
					header = header + requestedString + "\n";					
				}
				System.out.println(header);
				
				
				//file found and HEAD command
				if(command.equals("HEAD") &&  (new File("../res" + path).exists())) {
					StringBuilder head = getHeader(200,filetype);
					response.write(head.toString());
					response.write(head.toString());
					response.flush();
				}
				
				
				
				//POST command
				else if(command.equals("POST")) {
					String tempbody = request.readLine();
					String body = tempbody + "\n";
					while(!tempbody.isEmpty()) {
						tempbody = request.readLine();
						body = body + tempbody + "\n";
					}
					StringBuilder head = getHeader(addToServer(body, path),filetype);
					response.write(head.toString());
					response.flush();
				}
				
				
				
				//PUT command
				else if(command.equals("PUT")) {
					String tempbody = request.readLine();
					String body = tempbody + "\n";				
					while(!tempbody.isEmpty()) {
						tempbody = request.readLine();
						body = body + tempbody + "\n";
					}
					StringBuilder head = getHeader(writeToServer(body, path),"");
					response.write(head.toString());
					response.flush();
				}
				
				
				
				//file found and GET command
				else if (command.equals("GET") &&  (new File("../res" + path).exists())) {				
					StringBuilder head = getHeader(200,filetype);
					System.out.println(head);
					File HTMLfile = new File("../res" + path);
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
				
				
				
				//client did something wrong
				else {
					StringBuilder head = getHeader(400,filetype);
					response.write(head.toString());			
					response.flush();
				}
				
				request.close();
				response.close();
				
				
			//when everything fails consider it an internal error
	        } catch (IOException ex) { 
	        	try {
	        	System.out.println(Thread.currentThread().getName());
				BufferedWriter response = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));								
	        	StringBuilder head = getHeader(500,"");
				response.write(head.toString());			
				response.flush();
				response.close();
	        	}catch(Exception e) {
	        		
	        	}
	        }return;	 
	}
	
	/**
	 * adds text to a file, or creates the file and adds the text and returns the corresponding responsecode
	 * @param body
	 * @param path
	 * @return
	 */
	private int addToServer(String body, String path) {
		File file = new File("../res" + path);
		BufferedWriter bodyWriter;
		try {
			bodyWriter = new BufferedWriter(new FileWriter(file,true));
			bodyWriter.append(body);
			bodyWriter.close();
			return 200;
		} catch (IOException ex) {
			return 304;
		}
		
	}

	/**
	 * creates a file with the given name, adds text and returns the corresponding responsecode
	 * @param body
	 * @param path
	 * @return
	 */
	private int writeToServer(String body, String path) {
		File file = new File("../res" + path);
		BufferedWriter bodyWriter;
		try {
			bodyWriter = new BufferedWriter(new FileWriter(file));
			bodyWriter.write(body);
			bodyWriter.close();
			System.out.println("written to server");
			return 200;
		} catch (IOException ex) {
			return 304;
		}		
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
		case 304:
			head.append("HTTP/1.1 304 Not Modified\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("\r\n");
		case 500:
			head.append("HTTP/1.1 500 Internal Server Error\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("\r\n");
		case 400:
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

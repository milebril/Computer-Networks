import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import HTTP.Header;


public class Workable implements Runnable {

	Socket clientSocket = null;
	private int size = 0;
	
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
				
				String command = requestedString.split(" ")[0];	//GET,PUT,HEAD,POST,DELETE,OPTIONS command	
				
				//input from client
				while(!requestedString.equals("")) {
					requestedString = request.readLine();					
					header = header + requestedString + "\n";					
				}
				System.out.println("--------------------------------------------------------------------------------------------------------------------");
				System.out.println(header);
				System.out.println("--------------------------------------------------------------------------------------------------------------------");
				Header head = new Header();
				
					//HEAD command
					if(command.equals("HEAD")) {
						if (new File("../res" + path).exists()) head.setHeader(200,filetype,this.size); //file found
						else head.setHeader(404,filetype,this.size); // file not found
						response.write(head.getHeader().toString());
						response.write(head.getHeader().toString());
						response.flush();
					}
				
				
					
					//PUT  and POST command
					else if(command.equals("PUT") || command.equals("POST")) {
						String tempbody = request.readLine();
						String body = tempbody + "\n";				
						while(!tempbody.isEmpty()) {
							tempbody = request.readLine();
							body = body + tempbody + "\n";
						}
						if(command.equals("PUT"))head.setHeader(writeToServer(body, path),filetype,this.size); //PUT
						else head.setHeader(addToServer(body, path),filetype,this.size); // POST
						response.write(head.getHeader().toString());
						response.flush();
					}
				
				
				
					//file found and GET command
					else if (command.equals("GET") &&  (new File("../res" + path).exists())) {			
						if(filetype.equals("jpg") || filetype.equals("png")) {
							sendImage(new File("../res" + path), head, filetype, 200);
						}else {
							head.setHeader(200, filetype,this.size);
							response.write(head.getHeader().toString());
							File HTMLfile = new File("../res" + path);		
							response.write(HtmlToString(HTMLfile));
						}	
						
										
						response.flush();
					}
					
					
					
					//file not found
					else if (command.equals("GET") &&  (!new File("../res" + path).exists())) {				
						head.setHeader(404,filetype,this.size);
						response.write(head.getHeader().toString());			
						response.flush();
					}
								
					//DELETE command and file found
					else if (command.equals("DELETE")) {				
						if(new File("../res" + path).exists()) {
							new File("../res" + path).delete();
							head.setHeader(200,filetype,this.size);
						}else{
							head.setHeader(404,filetype,this.size);
						}
						response.write(head.getHeader().toString());			
						response.flush();
						
					}
				
				
				
					//OPTIONS returns the list of request methods supported
					else if(command.equals("OPTIONS")) {
						head.setHeader(201,filetype,this.size);
						response.write(head.getHeader().toString());
						StringBuilder options = new StringBuilder();
						options.append("GET: A client can use the GET request to get a web resource from the server.\r\n");
						options.append("HEAD: A client can use the HEAD request to get the header that a GET request would have obtained.\r\n");
						options.append("POST: Used to post data up to the web server.\r\n");
						options.append("PUT: Ask the server to store the data.\r\n");
						options.append("DELETE: Ask the server to delete the data.\r\n");
						options.append("OPTIONS: Ask the server to return the list of request methods it supports.\r\n");
						response.write(options.toString());
						response.flush();
					}
					
					
					//GETCOFFEE command
					else if(command.equals("GETCOFFEE")) {
						sendImage(new File("../res/img/teapot.png"), head, filetype, 418);
					}
					
				
				
					//Wrong method
					else if (!command.equals("GET") ||  !command.equals("PUT") ||  !command.equals("POST") ||  !command.equals("HEAD") ||  !command.equals("OPTIONS") ||  
							!command.equals("DELETE") || !command.equals("GETCOFFEE")) {				
						head.setHeader(501,filetype,this.size);
						response.write(head.getHeader().toString());			
						response.flush();
					}
					
					
					
					//client did something wrong
					else {
						head.setHeader(400,filetype,this.size);
						response.write(head.getHeader().toString());			
						response.flush();
					}
					
					request.close();
					response.close();
				
				
			//when everything fails consider it an internal server error
	        } catch (IOException ex) { 
	        	try {
	        	Header head = new Header();
	        	System.out.println(Thread.currentThread().getName());
	        	sendImage(new File("../res/img/monkey.jpg"), head, "jpg", 500);
	        	}catch(Exception e) {
	        		
	        	}
	        }return;	 
	}
	
	/**
	 * sends image to client in bytes
	 * @param file
	 * @param head
	 * @param filetype
	 * @param code
	 * @throws IOException
	 */
	public void sendImage(File file, Header head, String filetype, int code) throws IOException {
		this.size = (int) file.length();
		head.setHeader(code, filetype,this.size);
		
		OutputStream out = clientSocket.getOutputStream();
		FileInputStream fis = new FileInputStream(file);
		
		char[] c = head.getHeader().toString().toCharArray();
		for (int i = 0; i < c.length; i ++) {
			out.write((byte) c[i]);
		}
		
		byte[] b = new byte[64 * 1024];
	    int d = 0;
	    do {
	    		d = fis.read(b);
		    if (d != -1) {
		    		out.write(b, 0, d);
	   		}
	    } while(d != -1);
	    
	    out.close();
	    fis.close();
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
			return 304; //when it is not possible to write to the file, file must have been modified
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
			return 200;
		} catch (IOException ex) {
			return 304;	//when it is not possible to write to the file, file must have been modified
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
}

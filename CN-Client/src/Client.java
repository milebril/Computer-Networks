import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import http.Request;


public class Client{
	
	private static Request request;
	
	private static Socket clientSocket;
	
	/**
	 * TODO documentation
	 * @param args 
	 * args =  the command line
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception { 
		if (args.length == 3) { 
			request = new Request(args[0], args[1], Integer.parseInt(args[2]));
		} else {
			request = new Request(args[0], args[1]);
		}
		
        try {
        		System.out.println(request.getPort() );
        		clientSocket = new Socket(request.getURI(), request.getPort());
            OutputStream output = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
 
            writer.println(request.getCommand() +" /" + request.getPath() + " HTTP/1.1");
            writer.println("Host: " + request.getURI());
            writer.println("Connection: close");
            writer.println();
 
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;             
            File HTMLfile = new File("res/output.html");
		    BufferedWriter HTMLwriter = new BufferedWriter(new FileWriter(HTMLfile));

            while ((line = reader.readLine()) != null) {
                HTMLwriter.write(line);
                HTMLwriter.newLine();
            }
            HTMLwriter.close();
            clientSocket.close();
            SearchImages();
            
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
        
    }
	
	/**
	 * 
	 * @param url
	 * @param port
	 * @throws Exception
	 */
		 public static void SearchImages() throws Exception{
				File input = new File("res/output.html");
				Document doc = Jsoup.parse(input, "UTF-8", " ");	
				
				int count = 0;
				for (Element e : doc.select("img")) {
					try {				    	
						Socket clientSocket = new Socket(request.getURI(), request.getPort());
			            OutputStream output = clientSocket.getOutputStream();
			            PrintWriter writer = new PrintWriter(output, true);
			            System.out.println("GET " +"/" + request.getPath() + e.attr("src") + " HTTP/1.1");
			            writer.println("GET " +"/" + request.getPath() + e.attr("src") + " HTTP/1.1\r");
			            writer.println("Host: " + request.getURI() + ":" + request.getPort() +"\r");
			            writer.println("Connection: close\r");
			            writer.println();
			            			            
			            InputStream inputStream = clientSocket.getInputStream();

			            File HTMLfile = new File("res/" + e.attr("src"));	;
			            
					   OutputStream out = new FileOutputStream(HTMLfile);
			            
					   String headerString;
					   
					   do {
						   byte[] headerLine = new byte[1024];
						   byte temp;
						   int counter = 0;
						   
						   while((temp = (byte) inputStream.read()) != '\r') {
							   headerLine[counter++] = temp;
						   }
						   inputStream.read(); //Skip LF
						   
						   headerString = new String(Arrays.copyOfRange(headerLine, 0, counter));
						   //if (headerString.startsWith("Content-Type"))
						   
						   //System.out.println("L:" + headerString.length() + " " + headerString);
						   
					   } while (headerString.length() != 0);
					   
					   byte[] b = new byte[64 * 1024];
					   int d = 0;
					   int totalLen = 0;
					   do {
						   totalLen += d;
						   d = inputStream.read(b);
						   if (d != -1) {
							   out.write(b, 0, d);
					   		}
					   } while(d != -1);
					   
					   System.out.println(totalLen);
					   out.close();
					   clientSocket.close();
			            
					} catch (IOException ex) {
			            System.out.println("I/O error: " + ex.getMessage());
			        }
					
				}
				
				System.out.println("all images written");
			}
}
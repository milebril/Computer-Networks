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

import http.HTTPRequest;
import http.HTTPResponse;
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
		
		HTTPRequest httpRequest = new HTTPRequest(request);
		HTTPResponse response = httpRequest.getResponse();
		
		SearchImages();
		
//        try {
//        		System.out.println(request.getPort() );
//        		clientSocket = new Socket(request.getURI(), request.getPort());
//            OutputStream output = clientSocket.getOutputStream();
//            PrintWriter writer = new PrintWriter(output, true);
// 
//            writer.println(request.getCommand() +" /" + request.getPath() + " HTTP/1.1");
//            writer.println("Host: " + request.getURI());
//            writer.println("Connection: close");
//            writer.println();
//            
//            // HTTPResponse response = new HTTPResponse(clientSocket.getInputStream());
// 
//            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            String line;             
//            File HTMLfile = new File("res/output.html");
//		    BufferedWriter HTMLwriter = new BufferedWriter(new FileWriter(HTMLfile));
//
//            while ((line = reader.readLine()) != null) {
//                HTMLwriter.write(line);
//                HTMLwriter.newLine();
//            }
//            
//            HTMLwriter.close();
//            clientSocket.close();
//            
//            
//        } catch (IOException ex) {
//            System.out.println("I/O error: " + ex.getMessage());
//        }
        
    }
	
	/**
	 * 
	 * @param url
	 * @param port
	 * @throws Exception
	 */
		 public static void SearchImages() throws Exception{
				File input = new File("res/index.html");
				Document doc = Jsoup.parse(input, "UTF-8", " ");	
				for (Element e : doc.select("img")) {
					System.out.println(request.getURL().toString() + "/" + e.attr("src"));
					Request imgRequest = new Request("GET", 
							request.getURL().toString() + "/" + e.attr("src")
							, request.getPort()); //Set up a new Request
					HTTPRequest request = new HTTPRequest(imgRequest); //Make the request
					HTTPResponse response = request.getResponse(); //Here is the response written
				}
				
				System.out.println("all images written");
			}
}
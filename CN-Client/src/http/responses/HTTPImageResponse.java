package http.responses;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import http.requests.Request;

public class HTTPImageResponse extends HTTPResponse {
	
	public HTTPImageResponse(Request request, InputStream inputStream) {
		super(inputStream);
		
		this.inputStream = inputStream;
		this.request = request;
		
		makeBody();
	}

	/**
	 * Creates the body of an Image GET
	 * By Accepting bytes from the inputstream and writing them to a file in the location
	 * the .html file specifies.
	 * 
	 * Important: Header is also read byte-wise with an inputStream!
	 */
	private void makeBody() {
		//Make dirs to put the file in the correct location
		int removeLenght = request.getPath().split("/")[request.getPath().split("/").length-1].length();
		String path = "";
		if (request.getPath().length() > 0) {
			 path = request.getPath().substring(0, request.getPath().length()-1-removeLenght);
		}
		new File("res/" + path).mkdirs();
		
		//Write the Image
		try {
			File f;	
			if (request.getPath().length() > 0) {
				f = new File ("res/" + request.getPath());
				System.out.println("Hier");
			} else {
				f = new File ("res/response.png");
			}
			f.createNewFile();
			
			FileOutputStream out = new FileOutputStream(f);
			byte[] b = new byte[64 * 1024];
			   int d = 0;
			   do {
				   d = inputStream.read(b);
				   if (d != -1) {
					   out.write(b, 0, d);
			   		}
			   } while(d != -1);
			   
			   out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the body of the image request
	 * @return body returned in byte[]
	 */
	public byte[] getBody() {
		try {
			return Files.readAllBytes(Paths.get("res/" + request.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

package http.responses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import http.requests.Request;

public class HTTPTextResponse extends HTTPResponse {

	public HTTPTextResponse(Request request, InputStream inputStream) {
		super(inputStream);
		
		this.inputStream = inputStream;
		this.request = request;
		
		makeBody();
	}

	private void makeBody() {
		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//	        String line;             
//	        File HTMLfile = new File("res/index.html");
//		    BufferedWriter HTMLwriter = new BufferedWriter(new FileWriter(HTMLfile));
//	
//	        while ((line = reader.readLine()) != null) {
//	            HTMLwriter.write(line);
//	            System.out.println(line);
//	            HTMLwriter.newLine();
//	        }
//	        
//	        HTMLwriter.close();
			
			//Write the Image
			File f;
			if (request.getPath().endsWith("css")) {
				int removeLenght = request.getPath().split("/")[request.getPath().split("/").length-1].length();
				String path = request.getPath().substring(0, request.getPath().length()-1-removeLenght);
				new File("res/" + path).mkdirs();
				f = new File ("res/" + request.getPath());
			} else {
				f = new File("res/index.html");
			}
			
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
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}

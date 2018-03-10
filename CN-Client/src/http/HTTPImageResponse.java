package http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HTTPImageResponse extends HTTPResponse {

	public HTTPImageResponse(Request request, InputStream inputStream) {
		super(inputStream);
		
		this.inputStream = inputStream;
		this.request = request;
		
		makeBody();
	}

	private void makeBody() {
		//Make dirs to put the file in the correct location
		int removeLenght = request.getPath().split("/")[request.getPath().split("/").length-1].length();
		String path = request.getPath().substring(0, request.getPath().length()-1-removeLenght);
		new File("res/" + path).mkdirs();
		//Write the Image
		try {
			File f = new File ("res/" + request.getPath());
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
	
	public byte[] getBody() {
		try {
			return Files.readAllBytes(Paths.get("res/" + request.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

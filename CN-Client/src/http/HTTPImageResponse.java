package http;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HTTPImageResponse extends HTTPResponse {

	public HTTPImageResponse(Request request, InputStream inputStream) {
		super(inputStream);
		
		this.inputStream = inputStream;
		this.request = request;
		
		makeBody();
	}

	private void makeBody() {
		try {
			OutputStream out = new FileOutputStream("res/" + request.getPath());
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

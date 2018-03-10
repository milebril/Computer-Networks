package http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HTTPTextResponse extends HTTPResponse {

	public HTTPTextResponse(Request request, InputStream inputStream) {
		super(inputStream);
		
		this.inputStream = inputStream;
		this.request = request;
		
		makeBody();
	}

	private void makeBody() {
		try {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;             
        File HTMLfile = new File("res/index.html");
	    BufferedWriter HTMLwriter = new BufferedWriter(new FileWriter(HTMLfile));

        while ((line = reader.readLine()) != null) {
            HTMLwriter.write(line);
            HTMLwriter.newLine();
        }
        
        HTMLwriter.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}

package http.responses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import http.requests.Request;

public class HTTPOptionsResponse extends HTTPResponse {

	public HTTPOptionsResponse(Request request, InputStream inputStream) throws IOException {
		super(inputStream);
		
		this.inputStream = inputStream;
		this.request = request;
		
		makeBody();	
	}

	private void makeBody() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;             
        File file = new File("res/options.txt");
	    BufferedWriter HTMLwriter = new BufferedWriter(new FileWriter(file));

        while ((line = reader.readLine()) != null) {
            HTMLwriter.write(line);
            System.out.println(line);
            HTMLwriter.newLine();
        }
        
        HTMLwriter.close();
	}

}

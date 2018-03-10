package http;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HTTPResponse {
	
	private List<String> header;
	
	protected Request request;
	protected InputStream inputStream;
	
	public HTTPResponse(InputStream inputStream) {
		header = new ArrayList<>();
		
		separateHeader(inputStream);
	}
	
	public List<String> getHeader() {
		return header;
	}
	
	public void printHeader() {
		for (String s : header) {
			System.out.println(s);
		}
	}
	
	public String checkType() {
		for (String s : header) {
			if (s.startsWith("Content-Type")) {
				return s.split(" ")[1];
			}
		}
		return "none";
	}
	
	private void separateHeader(InputStream inputStream) {
		String headerString;
		try {
			do {
				byte[] headerLine = new byte[2048];
			    byte temp;
			    int counter = 0;
			   
			    while((temp = (byte) inputStream.read()) != '\r') {
			    		headerLine[counter++] = temp;
//			    		System.out.println(counter);
//			    		System.out.println(new String(Arrays.copyOfRange(headerLine, 0, counter)));
			    }
			    inputStream.read(); //Skip LF
			    
			    headerString = new String(Arrays.copyOfRange(headerLine, 0, counter));
			    header.add(headerString);
			} while (headerString.length() != 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
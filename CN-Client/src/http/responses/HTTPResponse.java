package http.responses;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import http.requests.Request;

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
	
	/**
	 * Separates the header by reading bytes till the \r character. Putting these in a byte array and then
	 * making it into a string which we save.
	 * We stop when the string has a length of 0, and thus we have an empty line.
	 * @param inputStream The stream we read from
	 */
	private void separateHeader(InputStream inputStream) {
		String headerString;
		try {
			do {
				byte[] headerLine = new byte[2048];
			    byte temp;
			    int counter = 0;
			   
			    while((temp = (byte) inputStream.read()) != '\r') {
			    		headerLine[counter++] = temp;
			    }
			    inputStream.read(); //Skip LF
			    
			    headerString = new String(Arrays.copyOfRange(headerLine, 0, counter));
			    System.out.println(headerString);
			} while (headerString.length() != 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
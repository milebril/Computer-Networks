package HTTP;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Header {

	StringBuilder header = new StringBuilder();
	
	/**
	 * getter for the header
	 * @return
	 */
	public StringBuilder getHeader() {
		return header;
	}
	
	/**
	 * setter for the header
	 * @return
	 */
	public void setHeader(int code, String type, int size) {
		StringBuilder head = new StringBuilder();
		String filetype = "";
		if(type.equals("png")) filetype = "image/png\r\n";
		else if(type.equals("jpg")) filetype = "image/jpg\r\n";
		else if(type.equals("html")) filetype = "text/html\r\n";
		else if(type.equals("css"))	filetype = "text/css\r\n";
		else if(type.equals("gif"))	filetype = "image/gif\r\n";
		else if(type.equals("js"))	filetype = "text/javascript\r\n";
		else filetype = "text/html\r\n";
		
		switch (code) {
		case 200:
			head.append("HTTP/1.1 200 OK\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("Content-Type: " + filetype);
			if(type.equals("jpg") || type.equals("png")) head.append("Content-Length: " + size + "\r\n");
			head.append("Connection: Closed\r\n\r\n");
			break;
		case 201:
			head.append("HTTP/1.1 200 OK\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("Content-Type: " + filetype);
			if(type.equals("jpg") || type.equals("png")) head.append("Content-Length: " + size + "\r\n");
			head.append("Allow: GET, HEAD, POST, OPTIONS, PUT, DELETE\r\n");
			head.append("Connection: Closed\r\n\r\n");
			break;	
		case 404:
			head.append("HTTP/1.1 404 Not Found\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("\r\n");
		case 304:
			head.append("HTTP/1.1 304 Not Modified\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("\r\n");
		case 500:
			head.append("HTTP/1.1 500 Internal Server Error\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("\r\n");
		case 400:
			head.append("HTTP/1.1 400 Bad Request\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("\r\n");
		case 501:
			head.append("HTTP/1.1 501 Method Not Implemented\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("\r\n");	
		case 418:
			head.append("HTTP/1.1 418 I'm a teapot\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("\r\n");		
		}this.header = head;
	}
	
	
	/**
	 * construct the header
	 * @param code
	 * @return response header
	 */
	public Header() {
		header = new StringBuilder();
	}

	/**
	 * returns the current date and time 
	 * @return string containing the date and time
	 */
	private static String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		String formattedDate = sdf.format(date);
		return formattedDate;
	}
	
}

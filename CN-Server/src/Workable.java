import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Workable implements Runnable {

	Socket clientSocket = null;
	
	public Workable(Socket client) {
		this.clientSocket = client;
	}

	@Override
	public void run() {
		 try {
		 	 	System.out.println(Thread.currentThread().getName());
			 	BufferedReader request = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
				BufferedWriter response = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
				
				
				String requestedString = "1";
				String header = "";
				
				requestedString = request.readLine();
				header = header + requestedString + "\n";
				
				String path = requestedString.split(" ")[1].substring(1);
				String command = requestedString.split(" ")[0];
				
				while(!requestedString.isEmpty()) {
					requestedString = request.readLine();
					header = header + requestedString + "\n";
					;
				}			
				StringBuilder head = getHeader(200);
				File HTMLfile = new File("../res" + path);
				response.write(head.toString());
				response.write(HtmlToString(HTMLfile));				
				response.flush();
				
				request.close();
				response.close();
	        } catch (IOException e) {           
	            System.out.println("error: "); 
	            e.printStackTrace();
	        }
	}
	
	public String HtmlToString(File file) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(file));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str + "\r\n");
		    }
		    in.close();
		} catch (IOException e) {
		}
		String content = contentBuilder.toString();
		return content;
	}
	
	public Boolean fileExists() {
		return true;
	}
	
	public StringBuilder getHeader(int code) {
		StringBuilder head = new StringBuilder();
		switch (code) {
		case 200:
			head.append("HTTP/1.1 200 OK\r\n");
			head.append("Date:" + getTimeStamp() + "\r\n");
			head.append("Server:localhost\r\n");
			head.append("Content-Type: text/html\r\n");
			head.append("Connection: Closed\r\n\r\n");
			break;
		case 404:
		case 400:	
		case 500:
		case 304:	
		}return head;
	}

		private static String getTimeStamp() {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			String formattedDate = sdf.format(date);
			return formattedDate;
		}
}

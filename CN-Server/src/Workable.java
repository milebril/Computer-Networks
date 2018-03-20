import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;

import HTTP.Header;

public class Workable implements Runnable {

	Socket clientSocket = null;

	/**
	 * constructor
	 * 
	 * @param client
	 */
	public Workable(Socket client){
		this.clientSocket = client;
	}

	/**
	 * returns the required information such as a header and body to the client.
	 */
	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		try {
			BufferedReader request = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
			BufferedWriter response = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
			System.out.println(Thread.currentThread().getName()); // prints out the current threadname
			while (System.currentTimeMillis() < startTime + 30000) {				
				WriteToClient wtc = new WriteToClient();
				wtc.CreateHeader(request, response, clientSocket);
			}
			System.out.println("out of time");
			response.close();
			request.close();
		} catch (IOException e) {
			System.out.println("error1");
			try {
				BufferedWriter response = new BufferedWriter(
						new OutputStreamWriter(this.clientSocket.getOutputStream()));
				Header head = new Header();
				head.setHeader(500, "", 0, null);
				response.write(head.getHeader().toString());
			} catch (Exception ex) {
				System.out.println("error2");
				ex.printStackTrace();
			}
		} catch (ParseException exx) {
			exx.printStackTrace();
		}
	}
}

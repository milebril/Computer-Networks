import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import HTTP.Header;

public class Workable implements Runnable {

	Socket clientSocket = null;

	/**
	 * constructor
	 * 
	 * @param client
	 */
	public Workable(Socket client) {
		this.clientSocket = client;
	}

	/**
	 * handles the request from the client.
	 */
	@Override
	public void run() {
			try {			
				BufferedReader request = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				BufferedWriter response = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
//				System.out.println(Thread.currentThread().getName()); // prints out the current threadname
				WriteToClient wtc = new WriteToClient();
				boolean emptyRequest = wtc.CreateHeader(request, response, clientSocket); 
				if (emptyRequest) {
					response.flush();
				}
			} catch (SocketException ex) {
				System.out.println(ex.getMessage());
			} catch (IOException ex) { // In case of an exception consider it an internal server failure
				try { 
					BufferedWriter response = new BufferedWriter(
							new OutputStreamWriter(clientSocket.getOutputStream()));
					Header head = new Header();
					head.setHeader(500, "", 0, null);
					response.write(head.getHeader().toString());
				} 
				catch (Exception exx) {
					System.out.println("We cannot handle this error: " + exx.getMessage());
				}
			} catch (Exception exx) {
				exx.printStackTrace();
			}
		try {
			clientSocket.close(); //close the socket when the request is completed
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

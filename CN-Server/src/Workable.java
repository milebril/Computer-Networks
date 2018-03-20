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
	public Workable(Socket client) {
		this.clientSocket = client;
	}

	/**
	 * returns the required information such as a header and body to the client.
	 */
	@Override
	public void run() {

		try {
			BufferedReader request = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedWriter response = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			System.out.println(Thread.currentThread().getName()); // prints out the current threadname
			WriteToClient wtc = new WriteToClient();
			wtc.CreateHeader(request, response, clientSocket);
			request.close();
			response.close();

		} catch (IOException e) {
			e.printStackTrace();
			try {
				BufferedWriter response = new BufferedWriter(
						new OutputStreamWriter(this.clientSocket.getOutputStream()));
				Header head = new Header();
				head.setHeader(500, "", 0, null);
				response.write(head.getHeader().toString());
				response.close();
			} catch (SocketException exxx) {
				System.out.println("connection to client: " + clientSocket.getInetAddress().getHostName() + " close");
				exxx.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (ParseException exx) {
			exx.printStackTrace();
		} 
	}
}

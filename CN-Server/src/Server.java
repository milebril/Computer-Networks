import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private int serverPort;
	private ServerSocket serverSocket = null;
	
	/**
	 * constructor
	 * @param port
	 */
	public Server(int port){
		this.serverPort = port;
	}
	
	public void start() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		}catch(IOException ex) {
			throw new RuntimeException("Unable to run server on port" + ex);
		}
		
		Socket client = null;
		while(true) {
			try {
				client = this.serverSocket.accept();
				System.out.println("Connected client:" + client.getInetAddress().getCanonicalHostName());
				Thread thread = new Thread(new Workable(client));
				thread.start();
			}catch(IOException ex) {
				System.out.println("server has stopped");
			}
		}
	}
}
	
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedServer {

	private int serverPort;
	private ServerSocket serverSocket = null;
	private ExecutorService threadPool = Executors.newFixedThreadPool(20);
	
	/**
	 * constructor for the server
	 * @param port
	 */
	public MultiThreadedServer(int port){
		this.serverPort = port;
	}
	
	/**
	 * opens serversocket in the given port.
	 * Server listens for a connection to be made to this socket and accepts it.
	 */
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
				System.out.println("Connected client:" + client.getInetAddress());
				Workable thread = new Workable(client);
				this.threadPool.execute(thread);
			}catch(IOException ex) {
				System.out.println("server has stopped");
				break;
			} 
		}
	}
}

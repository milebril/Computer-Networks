import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	private int serverPort;
	private ServerSocket serverSocket = null;
	private ExecutorService threadPool = Executors.newFixedThreadPool(10);
	private boolean running = true;
	
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

								
			}catch(IOException ex) {
				System.out.println("server has stopped");
				break;
			}
			Workable thread = new Workable(client);
			this.threadPool.execute(thread);
		}
	}
}
	
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

	int serverPort;
	ServerSocket serverSocket = null;
	Boolean Running = true;
	Thread thread = null;
	
	/**
	 * constructor
	 * @param port
	 */
	public Server(int port){
		this.serverPort = port;
	}
	
	/**
	 * starts a new socket on the given port.
	 */
	public void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		}catch(IOException ex) {
			throw new RuntimeException("Unable to run port" + ex);
		}
	}
	
	@Override
	public void run() {
		this.thread = Thread.currentThread();
		openServerSocket();
		Socket client = null;
		while(Running) {
			try {
				client = this.serverSocket.accept();
				System.out.println("Connected client:" + client.getInetAddress().getCanonicalHostName());
				new Thread(new Workable(client)).start();;
			}catch(IOException ex) {
				System.out.println("server has stopped");
			}
//			System.out.println("Connected client:" + client.getInetAddress().getCanonicalHostName());
//			new Thread(new Workable(client)).start();;
		}
	}
		
//	public void stop() {
//		this.Running = false;
//        try {
//            this.serverSocket.close();
//        } catch (IOException e) {
//            throw new RuntimeException("Server can not be closed: ", e);
//        }  
//	}
}
	
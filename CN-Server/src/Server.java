import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

	int serverPort;
	ServerSocket serverSocket = null;
	Boolean Running = true;
	Thread thread = null;
	
	public Server(int port){
		this.serverPort = port;
	}
	
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
		while(Running) {
			Socket client = null;
			try {
				client = this.serverSocket.accept();
			}catch(IOException ex) {
				System.out.println("server has stopped");
			}
			System.out.println("Connected client:" + client.getInetAddress().getCanonicalHostName());
			new Thread(new Workable(client)).start();;
		}
	}
		
	public void stop() {
		this.Running = false;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Server can not be closed: ", e);
        }  
	}
}
	
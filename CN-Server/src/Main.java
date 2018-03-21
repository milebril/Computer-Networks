public class Main {

	/**
	 * main method for the HTTPserver
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 8080;
		System.out.println("start server on port: " + port);
		MultiThreadedServer server = new MultiThreadedServer(port);
		server.start();
	}
	
}

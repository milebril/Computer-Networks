public class main {

	public static void main(String[] args) {
		System.out.println("start server");
		Server server = new Server(8080);
		server.start();
	}
	
}

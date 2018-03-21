package HTTP;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class CoffeeRequest {


	public CoffeeRequest(Header head, String filetype, Socket clientSocket) throws IOException {
		sendImage(new File("../res/img/teapot.png"), head, filetype, 418, clientSocket);
	}	
	/**
	 * sends image to client in bytes
	 * 
	 * @param file
	 * @param head
	 * @param filetype
	 * @param code
	 * @throws IOException
	 */
	public void sendImage(File file, Header head, String filetype, int code, Socket clientSocket) throws IOException {
		int size = (int) file.length();
		head.setHeader(code, filetype, size, file);

		OutputStream out = clientSocket.getOutputStream();
		FileInputStream fis = new FileInputStream(file);

		char[] c = head.getHeader().toString().toCharArray();
		for (int i = 0; i < c.length; i++) {
			out.write((byte) c[i]);
		}

		byte[] b = new byte[64 * 1024];
		int d = 0;
		int cd = 0;
		do {
			d = fis.read(b);

			if (d != -1) {
				cd = d;
				out.write(b, 0, d);
			}
		} while (d != -1);

		if (cd != file.length()) {
			System.out.println("Fout image ingelezen!");
		}
		out.close();
		fis.close();
	}

}

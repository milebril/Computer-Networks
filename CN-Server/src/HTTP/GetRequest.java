package HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

public class GetRequest {

	private Header header;
	private String body = null;

	public String getBody() {
		return this.body;
	}

	public Header getHeader() {
		return header;
	}

	public GetRequest(BufferedReader request, String path, int size, Date LastModifiedSince, String filetype,
			Header head, Socket clientSocket) throws IOException {
		if (!new File("../res" + path).exists()) {
			head.setHeader(404, filetype, 0, null);
			this.header = head;
			return;
		}
		 else if (LastModifiedSince != null) {
			 if (LastModifiedSince.before(new Date(new File("../res" + path).lastModified()))) {
				 head.setHeader(304, filetype, size, new File("../res" + path)); // file found but not modified
				 this.header = head;
			 }else 
				 if (filetype.equals("jpg") || filetype.equals("png")) {
				 sendImage(new File("../res" + path), head, filetype, 200, clientSocket);
			 } else {
				 File HTMLfile = new File("../res" + path);
				 int sizes = (int) HTMLfile.length();
				 head.setHeader(200, filetype, sizes, HTMLfile);
				 this.header = head;
				 this.body = HtmlToString(HTMLfile);
			 }
		 }
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

	/**
	 * changes an html file to a single string
	 * 
	 * @param file
	 * @return string containing the text of an html file
	 */
	public String HtmlToString(File file) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			while ((str = in.readLine()) != null) {
				contentBuilder.append(str + "\r\n");
			}
			in.close();
		} catch (IOException e) {
		}
		String content = contentBuilder.toString();
		return content;
	}

}

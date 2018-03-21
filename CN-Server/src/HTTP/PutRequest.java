package HTTP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class PutRequest {

	private Header header;

	public Header getHeader() {
		return header;
	}

	/**
	 * fulfills the request PUT
	 * @param request
	 * @param path
	 * @param size
	 * @param LastModifiedSince
	 * @param filetype
	 * @param head
	 * @throws IOException
	 */
	public PutRequest(BufferedReader request, String path, int size, Date LastModifiedSince, String filetype, Header head) throws IOException {
		String tempbody = request.readLine();
		String body = tempbody + "\n";
		while ((tempbody = request.readLine()) != null) {
			body = body + tempbody + "\n";
		}
		head.setHeader(writeToServer(body, path), filetype, size, null); 
		this.header = head;
	}

	/**
	 * creates a file with the given name, adds text and returns the corresponding
	 * responsecode
	 * 
	 * @param body
	 * @param path
	 * @return
	 */
	private int writeToServer(String body, String path) {
		File file = new File("../res" + path);
		BufferedWriter bodyWriter;
		try {
			bodyWriter = new BufferedWriter(new FileWriter(file));
			bodyWriter.write(body);
			bodyWriter.close();
			return 200;
		} catch (IOException ex) {
			return 404; // when it is not possible to write to the file, file is not found
		}
	}

}

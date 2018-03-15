package HTTP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class PostRequest {

	private Header header;

	public Header getHeader() {
		return header;
	}

	public PostRequest(BufferedReader request, String path, int size, Date LastModifiedSince, String filetype, Header head) throws IOException {
		String tempbody = request.readLine();
		String body = tempbody + "\n";
		while (!tempbody.isEmpty()) {
			tempbody = request.readLine();
			body = body + tempbody + "\n";
		}
		head.setHeader(addToServer(body, path), filetype, size, null); 
		this.header = head;
	}

	/**
	 * adds text to a file, or creates the file and adds the text and returns the
	 * corresponding responsecode
	 * 
	 * @param body
	 * @param path
	 * @return
	 */
	private int addToServer(String body, String path) {
		File file = new File("../res" + path);
		BufferedWriter bodyWriter;
		try {
			bodyWriter = new BufferedWriter(new FileWriter(file, true));
			bodyWriter.append(body);
			bodyWriter.close();
			return 200;
		} catch (IOException ex) {
			return 404; // when it is not possible to write to the file, file is not found
		}

	}

}

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

	/**
	 * fulfills the request POST
	 * @param request
	 * @param path
	 * @param size
	 * @param LastModifiedSince
	 * @param filetype
	 * @param head
	 * @throws IOException
	 */
	public PostRequest(BufferedReader request, String path, int size, Date LastModifiedSince, String filetype, Header head) throws IOException {
        String tempbody = request.readLine();
        String body = tempbody + "";       
        while((tempbody = request.readLine()) != null) {
          body = body + tempbody + "";
        }      
        int code = addToServer(body, path);
        head.setHeader(code, filetype, size, null); 
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
			if (body != null) {
				bodyWriter.write(body);
			}
			bodyWriter.close();
			return 200;
		} catch (IOException ex) {
			return 500; // when it is not possible to write to the file, consider it an internal server error
}

	}

}

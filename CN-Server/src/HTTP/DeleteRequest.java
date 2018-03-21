package HTTP;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class DeleteRequest {

	private Header header;

	public Header getHeader() {
		return header;
	}

	/**
	 * fulfills the request DELETE
	 * @param path
	 * @param size
	 * @param LastModifiedSince
	 * @param filetype
	 * @param head
	 * @throws IOException
	 */
	public DeleteRequest(String path, int size, Date LastModifiedSince, String filetype, Header head) throws IOException {
		if (new File("../res" + path).exists()) {
			new File("../res" + path).delete();
			head.setHeader(200, filetype, size, null);
		} else {
			head.setHeader(404, filetype, size, null);
		}
		header = head;
	}
}

package HTTP;

import java.io.File;
import java.util.Date;

public class HeadRequest {

	private Header header;
	
	public Header getHeader() {
		return header;
	}
	
	public HeadRequest(String path, int size, Date LastModifiedSince, String filetype, Header head) {
		if (new File("../res" + path).exists()) {
			if (LastModifiedSince == null || LastModifiedSince.before(new Date(new File("../res" + path).lastModified()))) {
				head.setHeader(200, filetype,size, new File("../res" + path)); // file found
			}
			else {
				head.setHeader(304, filetype, size, new File("../res" + path)); // file found but not modified
			}																			
		} else {
			head.setHeader(404, filetype, size, new File("../res" + path)); // file not found
		}this.header = head;
	}
	
	
	
	
}

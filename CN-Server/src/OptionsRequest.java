import HTTP.Header;

public class OptionsRequest {

	private Header header;
	private StringBuilder body = null;

	public StringBuilder getBody() {
		return this.body;
	}

	public Header getHeader() {
		return header;
	}
	
	public OptionsRequest(Header head, int size, String filetype) {
	head.setHeader(201, filetype, size, null);
	header = head;
	StringBuilder options = new StringBuilder();
	options.append("GET: A client can use the GET request to get a web resource from the server.\r\n");
	options.append("HEAD: A client can use the HEAD request to get the header that a GET request would have obtained.\r\n");
	options.append("POST: Used to post data up to the web server.\r\n");
	options.append("PUT: Ask the server to store the data.\r\n");
	options.append("DELETE: Ask the server to delete the data.\r\n");
	options.append("OPTIONS: Ask the server to return the list of request methods it supports.\r\n");
	body = options;
	}
}

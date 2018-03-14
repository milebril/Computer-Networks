import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import http.requests.HTTPRequest;
import http.requests.Request;
import http.responses.HTTPResponse;


public class Client{
	
	private static Request request;
	
	/**
	 * TODO documentation
	 * @param inputted through command line {COMMAND} {URL} {PORT}(optional)
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception { 
		if (args.length == 3) { 
			request = new Request(args[0], args[1], Integer.parseInt(args[2]));
		} else {
			request = new Request(args[0], args[1]);
		}
		
		HTTPRequest httpRequest = new HTTPRequest(request);
		HTTPResponse response = httpRequest.getResponse();
		response.printHeader();
		
		if (request.getCommand().equals("GET")) {
			SearchEmbededObjects();
		}
    }
	
	/**
	 * @param url
	 * @param port
	 * @throws Exception
	 */
	 public static void SearchEmbededObjects() throws Exception{
			File input = new File("res/index.html");
			Document doc = Jsoup.parse(input, "UTF-8", " ");	
			
			//Search for img tags
			for (Element e : doc.select("img")) {
				System.out.println(request.getURL().toString() + "/" + e.attr("src"));
				Request imgRequest = new Request("GET", 
						request.getURL().toString() + "/" + e.attr("src")
						, request.getPort()); //Set up a new Request
				//System.out.println("Getting image... " + request.getURL().toString() + "/" + e.attr("src"));
				HTTPRequest request = new HTTPRequest(imgRequest); //Make the request
				HTTPResponse response = request.getResponse(); //Here is the response written
			}
			
			//search for style tags
			for (Element e : doc.select("link")) {
				if (e.attr("rel").equals("stylesheet")) {
					//String href = e.attr("href");
					System.out.println(request.getURL().toString() + "/" + e.attr("href"));
					Request imgRequest = new Request("GET", 
							request.getURL().toString() + "/" + e.attr("href")
							, request.getPort()); //Set up a new Request
					HTTPRequest request = new HTTPRequest(imgRequest); //Make the request
					HTTPResponse response = request.getResponse(); //Here is the response written
				}
				
			}
		}
}
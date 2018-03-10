import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import http.HTTPRequest;
import http.HTTPResponse;
import http.Request;


public class Client{
	
	private static Request request;
	
	/**
	 * TODO documentation
	 * @param args =  the command line
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
		
		SearchImages();
    }
	
	/**
	 * @param url
	 * @param port
	 * @throws Exception
	 */
	 public static void SearchImages() throws Exception{
			File input = new File("res/index.html");
			Document doc = Jsoup.parse(input, "UTF-8", " ");	
			for (Element e : doc.select("img")) {
				System.out.println(request.getURL().toString() + "/" + e.attr("src"));
				Request imgRequest = new Request("GET", 
						request.getURL().toString() + "/" + e.attr("src")
						, request.getPort()); //Set up a new Request
				HTTPRequest request = new HTTPRequest(imgRequest); //Make the request
				HTTPResponse response = request.getResponse(); //Here is the response written
			}
			
			System.out.println("all images written");
		}
}
package http.errors;

public class notEnoughArgumentsException extends Exception {
	
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Please use the correct format: {Commando} {URL} {PORT}(optional).";
	}
	
}

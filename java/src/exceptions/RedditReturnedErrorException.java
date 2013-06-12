package exceptions;

public class RedditReturnedErrorException extends RedditException {
	
	public RedditReturnedErrorException(String error) {
		super(error);
		NAME = "Reddit Returned an Error Exception";
	}
	
}

package exceptions;

/**
 * For whatever reason, a connection to the Reddit service couldn't be made.
 * It's possible that it's under heavy load, or maybe you don't have internet
 * service. Consider using the API in offline mode!
 * 
 * @author acbart
 * 
 */
public class RedditNotAvailableException extends RedditException {

	public RedditNotAvailableException(String error) {
		super(error);
		NAME = "Reddit Not Available Exception";
	}

}

package exceptions;

/**
 * Often returned when you navigate to a non-existent Subreddit. Instead of returning useful JSON, it returns an HTML page.
 * @author acbart
 *
 */
public class PostNotFoundException extends RedditException {

	public PostNotFoundException(String error) {
		super(error);
		NAME = "The Subreddit Could Not Be Found";
	}

}

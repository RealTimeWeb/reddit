package exceptions;

/**
 * Valid subreddit names must be:
 * <ul>
 * 	<li>At least one character</li>
 * 	<li>Fewer than 22 characters</li>
 *  <li>No non-alphanumerics</li>
 *  <li>
 * abcdefghijklmnopqrstuv
 * </ul>
 * @author acbart
 *
 */
public class RedditInvalidSubredditException extends RedditException{

	public RedditInvalidSubredditException(String error) {
		super(error);
		NAME = "Subreddit is not valid Exception";
	}
	
}

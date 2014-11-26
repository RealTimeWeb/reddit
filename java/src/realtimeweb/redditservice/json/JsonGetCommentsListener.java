package realtimeweb.redditservice.json;

/**
 * A listener for the getComments method. On success, passes the data into the getCommentsCompleted method. On failure, passes the exception to the getCommentsFailed method.
 */
public interface JsonGetCommentsListener {
	/**
	 * 
	 * @param data The method that should be overridden to handle the data if the method was successful.
	 */
	public abstract void getCommentsCompleted(String data);
	/**
	 * 
	 * @param error The method that should be overridden to handle an exception that occurred while getting the SearchResponse.
	 */
	public abstract void getCommentsFailed(Exception error);
}

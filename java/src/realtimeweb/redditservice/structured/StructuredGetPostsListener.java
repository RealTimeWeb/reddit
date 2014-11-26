package realtimeweb.redditservice.structured;

import java.util.ArrayList;
/**
 * A listener for the getPosts method. On success, passes the data into the getPostsCompleted method. On failure, passes the exception to the getPostsFailed method.
 */
public interface StructuredGetPostsListener {
	/**
	 * 
	 * @param data The method that should be overridden to handle the data if the method was successful.
	 */
	public abstract void getPostsCompleted(ArrayList<Object> data);
	/**
	 * 
	 * @param error The method that should be overridden to handle an exception that occurred while getting the SearchResponse.
	 */
	public abstract void getPostsFailed(Exception error);
}

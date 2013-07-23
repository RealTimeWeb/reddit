package realtimeweb.redditservice.structured;

import java.util.ArrayList;
import java.util.HashMap;
import realtimeweb.redditservice.domain.Post;
/**
 * 
 */
public interface StructuredGetPostsListener {
	/**
	 * 
	 * @param data
	 */
	public abstract void getPostsCompleted(ArrayList<Object> data);
	/**
	 * 
	 * @param error
	 */
	public abstract void getPostsFailed(Exception error);
}

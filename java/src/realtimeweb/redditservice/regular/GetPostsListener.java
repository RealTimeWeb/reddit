package realtimeweb.redditservice.regular;

import java.util.ArrayList;
import java.util.HashMap;
import realtimeweb.redditservice.domain.Post;
/**
 * 
 */
public interface GetPostsListener {
	/**
	 * 
	 * @param data
	 */
	public abstract void getPostsCompleted(ArrayList<Post> data);
	/**
	 * 
	 * @param error
	 */
	public abstract void getPostsFailed(Exception error);
}

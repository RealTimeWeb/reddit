package realtimeweb.redditservice.json;

import java.util.ArrayList;
import java.util.HashMap;
import realtimeweb.redditservice.domain.Post;
/**
 * 
 */
public interface JsonGetPostsListener {
	/**
	 * 
	 * @param data
	 */
	public abstract void getPostsCompleted(String data);
	/**
	 * 
	 * @param error
	 */
	public abstract void getPostsFailed(Exception error);
}

package realtimeweb.redditservice.structured;

import realtimeweb.redditservice.main.AbstractRedditService;
import realtimeweb.redditservice.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.google.gson.Gson;
import realtimeweb.redditservice.json.JsonRedditService;
import realtimeweb.redditservice.json.JsonGetPostsListener;
import realtimeweb.redditservice.json.JsonGetCommentsListener;

/**
 * Used to get data as built-in Java objects (HashMap, ArrayList, etc.).
 */
public class StructuredRedditService implements AbstractRedditService {
	private static StructuredRedditService instance;
	private JsonRedditService jsonInstance;
	private Gson gson;
	/**
	 * **For internal use only!** Protected Constructor guards against instantiation.
	
	 * @return 
	 */
	protected  StructuredRedditService() {
		this.jsonInstance = JsonRedditService.getInstance();
		this.gson = new Gson();
	}
	
	/**
	 * Retrieves the singleton instance.
	
	 * @return StructuredRedditService
	 */
	public static StructuredRedditService getInstance() {
		if (instance == null) {
			synchronized (StructuredRedditService.class) {
				if (instance == null) {
					instance = new StructuredRedditService();
				}
			}
			
		}
		return instance;
	}
	
	/**
	 * Establishes a connection to the online service. Requires an internet connection.
	
	 */
	@Override
	public void connect() {
		jsonInstance.connect();
	}
	
	/**
	 * Establishes that Business Search data should be retrieved locally. This does not require an internet connection.<br><br>If data is being retrieved locally, you must be sure that your parameters match locally stored data. Otherwise, you will get nothing in return.
	
	 */
	@Override
	public void disconnect() {
		jsonInstance.disconnect();
	}
	
	/**
	 * Retrieves all the top posts
	 * @param subreddit The subreddit that Posts will be returned from. Use "all" to return results from all subreddits.
	 * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
	 * @return HashMap<String, Object>
	 */
	public HashMap<String, Object> getPosts(String subreddit, String sortMode) throws Exception {
		return gson.fromJson(jsonInstance.getPosts(subreddit, sortMode), LinkedHashMap.class);
	}
	
	/**
	 * Retrieves all the top posts
	 * @param subreddit The subreddit that Posts will be returned from. Use "all" to return results from all subreddits.
	 * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
	 * @param callback The listener that will be given the data (or error)
	 */
	public void getPosts(String subreddit, String sortMode, final StructuredGetPostsListener callback) {
		
		jsonInstance.getPosts(subreddit, sortMode, new JsonGetPostsListener() {
		    @Override
		    public void getPostsFailed(Exception exception) {
		        callback.getPostsFailed(exception);
		    }
		    
		    @Override
		    public void getPostsCompleted(String data) {
		        callback.getPostsCompleted(gson.fromJson(data, ArrayList.class));
		    }
		});
		
	}
	
	/**
	 * Retrieves comments for a post
	 * @param id The unique id of a Post from which Comments will be returned.
	 * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
	 * @return HashMap<String, Object>
	 */
	public HashMap<String, Object> getComments(String subreddit, String id, String sortMode) throws Exception {
		return gson.fromJson(jsonInstance.getComments(subreddit, id, sortMode), LinkedHashMap.class);
	}
	
	/**
	 * Retrieves comments for a post
	 * @param id The unique id of a Post from which Comments will be returned.
	 * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
	 * @param callback The listener that will be given the data (or error)
	 */
	public void getComments(String subreddit, String id, String sortMode, final StructuredGetCommentsListener callback) {
		
		jsonInstance.getComments(subreddit, id, sortMode, new JsonGetCommentsListener() {
		    @Override
		    public void getCommentsFailed(Exception exception) {
		        callback.getCommentsFailed(exception);
		    }
		    
		    @Override
		    public void getCommentsCompleted(String data) {
		        callback.getCommentsCompleted(gson.fromJson(data, ArrayList.class));
		    }
		});
		
	}
	
}

package realtimeweb.redditservice.json;

import realtimeweb.redditservice.main.AbstractRedditService;
import java.util.HashMap;
import realtimeweb.redditservice.util.Util;

/**
 * Used to get data as a raw string.
 */
public class JsonRedditService implements AbstractRedditService {
	private static JsonRedditService instance;
	protected boolean local;
	private ClientStore clientStore;
	/**
	 * **For internal use only!** Protected Constructor guards against instantiation.
	
	 * @return 
	 */
	protected  JsonRedditService() {
		disconnect();
		this.clientStore = new ClientStore();
	}
	
	/**
	 * Retrieves the singleton instance.
	
	 * @return JsonRedditService
	 */
	public static JsonRedditService getInstance() {
		if (instance == null) {
			synchronized (JsonRedditService.class) {
				if (instance == null) {
					instance = new JsonRedditService();
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
		this.local = false;
	}
	
	/**
	 * Establishes that Business Search data should be retrieved locally. This does not require an internet connection.<br><br>If data is being retrieved locally, you must be sure that your parameters match locally stored data. Otherwise, you will get nothing in return.
	
	 */
	@Override
	public void disconnect() {
		this.local = true;
	}
	
	/**
	 * **For internal use only!** The ClientStore is the internal cache where offline data is stored.
	
	 * @return ClientStore
	 */
	public ClientStore getClientStore() {
		return this.clientStore;
	}
	
	/**
	 * Retrieves all the top posts
	 * @param subreddit The subreddit that Posts will be returned from. Use "all" to return results from all subreddits.
	 * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
	 * @return String
	 */
	public String getPosts(String subreddit, String sortMode) throws Exception {
		String url = String.format("http://www.reddit.com/r/%s/%s.json", subreddit, sortMode);
		HashMap<String, String> parameters = new HashMap<String, String>();
		if (this.local) {
			return clientStore.getData(Util.hashRequest(url, parameters));
		}
		String jsonResponse = "";
		try {
		    jsonResponse = Util.get(url, parameters);
		    if (jsonResponse.startsWith("<")) {
		        throw new Exception(jsonResponse);
		    }
		    return jsonResponse;
		} catch (Exception e) {
		    throw new Exception(e.toString());
		}
	}
	
	/**
	 * Retrieves all the top posts
	 * @param subreddit The subreddit that Posts will be returned from. Use "all" to return results from all subreddits.
	 * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
	 * @param callback The listener that will be given the data (or error).
	 */
	public void getPosts(final String subreddit, final String sortMode, final JsonGetPostsListener callback) {
		
		Thread thread = new Thread() {
		    @Override
		    public void run() {
		        try {
		            callback.getPostsCompleted(JsonRedditService.getInstance().getPosts(subreddit, sortMode));
		        } catch (Exception e) {
		            callback.getPostsFailed(e);
		        }
		    }
		};
		thread.start();
		
	}
	
	/**
	 * Retrieves comments for a post
	 * @param id The unique id of a Post from which Comments will be returned.
	 * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
	 * @return String
	 */
	public String getComments(String subreddit, String id, String sortMode) throws Exception {
		String url = String.format("http://www.reddit.com/r/%s/comments/%s/%s.json", subreddit, id, sortMode);
		HashMap<String, String> parameters = new HashMap<String, String>();
		if (this.local) {
			return clientStore.getData(Util.hashRequest(url, parameters));
		}
		String jsonResponse = "";
		try {
		    jsonResponse = Util.get(url, parameters);
		    if (jsonResponse.startsWith("<")) {
		        throw new Exception(jsonResponse);
		    }
		    return jsonResponse;
		} catch (Exception e) {
		    throw new Exception(e.toString());
		}
	}
	
	/**
	 * Retrieves comments for a post
	 * @param id The unique id of a Post from which Comments will be returned.
	 * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
	 * @param callback The listener that will be given the data (or error).
	 */
	public void getComments(final String subreddit, final String id, final String sortMode, final JsonGetCommentsListener callback) {
		
		Thread thread = new Thread() {
		    @Override
		    public void run() {
		        try {
		            callback.getCommentsCompleted(JsonRedditService.getInstance().getComments(subreddit, id, sortMode));
		        } catch (Exception e) {
		            callback.getCommentsFailed(e);
		        }
		    }
		};
		thread.start();
		
	}
	
}

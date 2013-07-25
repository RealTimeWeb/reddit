package realtimeweb.redditservice.regular;

import realtimeweb.redditservice.main.AbstractRedditService;
import realtimeweb.redditservice.json.JsonRedditService;
import realtimeweb.redditservice.util.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import realtimeweb.redditservice.domain.Comment;
import realtimeweb.redditservice.domain.Post;
import realtimeweb.redditservice.json.JsonGetPostsListener;
import realtimeweb.redditservice.json.JsonGetCommentsListener;

/**
 * Used to get data as classes.
 */
public class RedditService implements AbstractRedditService {
	private static RedditService instance;
	private JsonRedditService jsonInstance;
	private Gson gson;
	/**
	 * **For internal use only!** Protected Constructor guards against instantiation.
	
	 * @return 
	 */
	protected  RedditService() {
		this.jsonInstance = JsonRedditService.getInstance();
		this.gson = new Gson();
	}
	
	/**
	 * Retrieves the singleton instance.
	
	 * @return RedditService
	 */
	public static RedditService getInstance() {
		if (instance == null) {
			synchronized (RedditService.class) {
				if (instance == null) {
					instance = new RedditService();
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
	 * @return ArrayList<Post>
	 */
	public ArrayList<Post> getPosts(String subreddit, String sortMode) throws Exception {
		String data = jsonInstance.getPosts(subreddit,sortMode);
		JsonParser parser = new JsonParser();
		JsonArray allChildren = parser.parse(data).getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();
		ArrayList<Post> result = new ArrayList<Post>();
		for (int i = 0; i < allChildren.size()-1; i += 1) {
			result.add(new Post(allChildren.get(i).getAsJsonObject(), gson));
		}
		return result;
	}
	
	/**
	 * Retrieves all the top posts
	 * @param subreddit The subreddit that Posts will be returned from. Use "all" to return results from all subreddits.
	 * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
	 * @param callback The listener that will receive the data (or error).
	 */
	public void getPosts(String subreddit, String sortMode, final GetPostsListener callback) {
		
		jsonInstance.getPosts(subreddit, sortMode, new JsonGetPostsListener() {
		    @Override
		    public void getPostsFailed(Exception exception) {
		        callback.getPostsFailed(exception);
		    }
		    
		    @Override
		    public void getPostsCompleted(String data) {
		        JsonParser parser = new JsonParser();
		        JsonArray allChildren = parser.parse(data).getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();
		        ArrayList<Post> result = new ArrayList<Post>();
		        for (int i = 0; i < allChildren.size()-1; i += 1) {
		            result.add(new Post(allChildren.get(i).getAsJsonObject(), gson));
		        }
		        callback.getPostsCompleted(result);
		    }
		});
		
	}
	
	/**
	 * Retrieves comments for a post
	 * @param id The unique id of a Post from which Comments will be returned.
	 * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
	 * @return ArrayList<Comment>
	 */
	public ArrayList<Comment> getComments(String subreddit, String id, String sortMode) throws Exception {
		String data = jsonInstance.getComments(subreddit, id,sortMode);
		JsonParser parser = new JsonParser();
		JsonArray allChildren = parser.parse(data).getAsJsonArray().get(1).getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();
		ArrayList<Comment> result = new ArrayList<Comment>();
		for (int i = 0; i < allChildren.size()-1; i += 1) {
			result.add(new Comment(allChildren.get(i).getAsJsonObject(), gson));
		}
		return result;
	}
	
	/**
	 * Retrieves comments for a post
	 * @param id The unique id of a Post from which Comments will be returned.
	 * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
	 * @param callback The listener that will receive the data (or error).
	 */
	public void getComments(String subreddit, String id, String sortMode, final GetCommentsListener callback) {
		
		jsonInstance.getComments(subreddit, id, sortMode, new JsonGetCommentsListener() {
		    @Override
		    public void getCommentsFailed(Exception exception) {
		        callback.getCommentsFailed(exception);
		    }
		    
		    @Override
		    public void getCommentsCompleted(String data) {
		        JsonParser parser = new JsonParser();
		        JsonArray allChildren = parser.parse(data).getAsJsonArray().get(0).getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();
		        ArrayList<Comment> result = new ArrayList<Comment>();
		        for (int i = 0; i < allChildren.size()-1; i += 1) {
		            result.add(new Comment(allChildren.get(i).getAsJsonObject(), gson));
		        }
		        callback.getCommentsCompleted(result);
		    }
		});
		
	}
	
}

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
 * 
 */
public class RedditService implements AbstractRedditService {
	private static RedditService instance;
	private JsonRedditService jsonInstance;
	private Gson gson;
	/**
	 * 
	
	 * @return 
	 */
	protected  RedditService() {
		this.jsonInstance = JsonRedditService.getInstance();
		this.gson = new Gson();
	}
	
	/**
	 * 
	
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
	 * 
	
	 */
	@Override
	public void connect() {
		jsonInstance.connect();
	}
	
	/**
	 * 
	
	 */
	@Override
	public void disconnect() {
		jsonInstance.disconnect();
	}
	
	/**
	 * Retrieves all the top posts
	 * @param subreddit
	 * @param sortMode
	 * @return ArrayList<Post>
	 */
	public ArrayList<Post> getPosts(String subreddit, String sortMode) throws Exception {
		String data= jsonInstance.getPosts(subreddit,sortMode);
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
	 * @param subreddit
	 * @param sortMode
	 * @param callback
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
		            result.set(i, new Post(allChildren.get(i).getAsJsonObject(), gson));
		        }
		        callback.getPostsCompleted(result);
		    }
		});
		
	}
	
	/**
	 * Retrieves comments for a post
	 * @param subreddit
	 * @param id
	 * @param sortMode
	 * @return ArrayList<Comment>
	 */
	public ArrayList<Comment> getComments(String subreddit, String id, String sortMode) throws Exception {
		String data = jsonInstance.getComments(subreddit,id,sortMode);
		JsonParser parser = new JsonParser();
		JsonArray allChildren = parser.parse(data).getAsJsonArray().get(1).getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();
		ArrayList<Comment> result = new ArrayList<Comment>();
		for (int i = 0; i < allChildren.size()-1; i += 1) {
			result.add(i, new Comment(allChildren.get(i).getAsJsonObject(), gson));
		}
		return result;
	}
	
	/**
	 * Retrieves comments for a post
	 * @param subreddit
	 * @param id
	 * @param sortMode
	 * @param callback
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
		            result.set(i, new Comment(allChildren.get(i).getAsJsonObject(), gson));
		        }
		        callback.getCommentsCompleted(result);
		    }
		});
		
	}
	
}

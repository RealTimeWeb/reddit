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
 * 
 */
public class StructuredRedditService implements AbstractRedditService {
	private static StructuredRedditService instance;
	private JsonRedditService jsonInstance;
	private Gson gson;
	/**
	 * 
	
	 * @return 
	 */
	protected  StructuredRedditService() {
		this.jsonInstance = JsonRedditService.getInstance();
		this.gson = new Gson();
	}
	
	/**
	 * 
	
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
	 * @return HashMap<String, Object>
	 */
	public HashMap<String, Object> getPosts(String subreddit, String sortMode) throws Exception {
		return gson.fromJson(jsonInstance.getPosts(subreddit, sortMode), LinkedHashMap.class);
	}
	
	/**
	 * Retrieves all the top posts
	 * @param subreddit
	 * @param sortMode
	 * @param callback
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
	 * @param subreddit
	 * @param id
	 * @param sortMode
	 * @return HashMap<String, Object>
	 */
	public HashMap<String, Object> getComments(String subreddit, String id, String sortMode) throws Exception {
		return gson.fromJson(jsonInstance.getComments(subreddit, id, sortMode), LinkedHashMap.class);
	}
	
	/**
	 * Retrieves comments for a post
	 * @param subreddit
	 * @param id
	 * @param sortMode
	 * @param callback
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

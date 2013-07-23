package realtimeweb.redditservice.json;

import realtimeweb.redditservice.main.AbstractRedditService;
import java.util.HashMap;
import realtimeweb.redditservice.util.Util;

/**
 * 
 */
public class JsonRedditService implements AbstractRedditService {
	private static JsonRedditService instance;
	protected boolean local;
	private ClientStore clientStore;
	/**
	 * 
	
	 * @return 
	 */
	protected  JsonRedditService() {
		disconnect();
		this.clientStore = new ClientStore();
	}
	
	/**
	 * 
	
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
	 * 
	
	 */
	@Override
	public void connect() {
		this.local = false;
	}
	
	/**
	 * 
	
	 */
	@Override
	public void disconnect() {
		this.local = true;
	}
	
	/**
	 * 
	
	 * @return ClientStore
	 */
	public ClientStore getClientStore() {
		return this.clientStore;
	}
	
	/**
	 * Retrieves all the top posts
	 * @param subreddit
	 * @param sortMode
	 * @return String
	 */
	public String getPosts(String subreddit, String sortMode) throws Exception {
		String url = String.format("http://www.reddit.com/r/%s/%s.json", String.valueOf(subreddit), String.valueOf(sortMode));
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
	 * @param subreddit
	 * @param sortMode
	 * @param callback
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
	 * @param subreddit
	 * @param id
	 * @param sortMode
	 * @return String
	 */
	public String getComments(String subreddit, String id, String sortMode) throws Exception {
		String url = String.format("http://www.reddit.com/r/%s/comments/%s/%s.json", String.valueOf(subreddit), String.valueOf(id), String.valueOf(sortMode));
		HashMap<String, String> parameters = new HashMap<String, String>();
		if (this.local) {
			return clientStore.getData(Util.hashRequest(url, parameters));
		}
		String jsonResponse = "";
		    jsonResponse = Util.get(url, parameters);
		    if (jsonResponse.startsWith("<")) {
		        throw new Exception(jsonResponse);
		    }
		    return jsonResponse;
	}
	
	/**
	 * Retrieves comments for a post
	 * @param subreddit
	 * @param id
	 * @param sortMode
	 * @param callback
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

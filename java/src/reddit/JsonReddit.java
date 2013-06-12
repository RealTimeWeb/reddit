package reddit;

import java.util.ArrayList;

import org.json.simple.JSONValue;

import exceptions.RedditException;

public class JsonReddit extends AbstractReddit {
	private static JsonReddit instance;

	/*
	 * Protected Constructor to defeat instantiation Common Singleton pattern
	 * (google for more info!)
	 */
	protected JsonReddit() {
	}

	/**
	 * Get the global instance of the Reddit connection.
	 * 
	 * @return
	 */
	public static JsonReddit getInstance() {
		if (instance == null) {
			synchronized (JsonReddit.class) {
				if (instance == null) {
					instance = new JsonReddit();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Get the posts for a specific subreddit. If you want to get the posts from
	 * all the subreddits, you can use "all". If you want to search multiple subreddits
	 * at the same time, you can concatenate their names with "+".
	 * 
	 * 
	 * Examples:
	 * <ul>
	 * <li>getPosts("all", SortMode.NEW);</li>
	 * <li>getPosts("virginiatech", SortMode.TOP);</li>
	 * <li>getPosts("pics+video", SortMode.CONTROVERSIAL);</li>
	 * </ul>
	 * 
	 * @param subreddit A string representing the subreddit. Do <i>not</i> include the "/r/" that usually precedes a subreddit name. 
	 * @param sort The way you want results to be sorted.
	 * @param callback A callback function that will process the data collected by the method.
	 * @throws RedditException
	 */
	public void getPosts(final String subreddit, final SortMode sort,
			final JsonPostListener callback) {
		Reddit.getInstance().getPosts(subreddit, sort, new PostListener() {

			@Override
			public void onSuccess(ArrayList<Post> posts) {
				ArrayList<Object> postMap = new ArrayList<Object>();
				for (Post post: posts) {
					postMap.add(post.getAsMap());
				}
				String json = JSONValue.toJSONString(postMap);
				callback.onSuccess(json);
			}
			
			public void onFailure(Exception e) {
				callback.onFailure(e);
			}
			
		});
	}
	
	/**
	 * Gets the comments for a specific Post's ID in a subreddit.
	 * 
	 * @param subreddit A String representing a subreddit (e.g. "virginiatech" or "funny"). Do not include the "/r/".
	 * @param id A string representing a specific comment on a post in a subreddit. Can be obtained through getId on a Post.
	 * @param sort The order in which you want the results to be sorted.
	 * @param callback A function that will process the results or errors.
	 * @throws RedditException
	 */
	public void getComments(final String subreddit, final String id,
			final SortMode sort, final JsonCommentListener callback) {
		Reddit.getInstance().getComments(subreddit, id, sort, new CommentListener() {

			@Override
			public void onSuccess(ArrayList<Comment> comments) {
				ArrayList<Object> commentMap = new ArrayList<Object>();
				for (Comment comment: comments) {
					commentMap.add(comment.getAsMap());
				}
				String json = JSONValue.toJSONString(commentMap);
				callback.onSuccess(json);
			}
			
			public void onFailure(Exception e) {
				callback.onFailure(e);
			}
			
		});
	}
}

package reddit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.parser.ParseException;

import exceptions.CommentNotFoundException;
import exceptions.RedditException;
import exceptions.RedditInvalidSubredditException;
import exceptions.RedditNotAvailableException;
import exceptions.RedditReturnedErrorException;
import exceptions.PostNotFoundException;

public class Reddit extends AbstractReddit {

	private static Reddit instance;

	/*
	 * Protected Constructor to defeat instantiation Common Singleton pattern
	 * (google for more info!)
	 */
	protected Reddit() {
	}

	/**
	 * Get the global instance of the Reddit connection.
	 * 
	 * @return
	 */
	public static Reddit getInstance() {
		if (instance == null) {
			synchronized (Reddit.class) {
				if (instance == null) {
					instance = new Reddit();
				}
			}
		}
		return instance;
	}

	/**
	 * Used to check if a given subreddit is valid (length is between 1 and 21
	 * chars, and has only alphanumerics)
	 * 
	 * @param subreddit
	 * @return
	 */
	private static boolean isValidSubreddit(String subreddit) {
		return subreddit.length() <= 22 && subreddit.length() >= 0
				&& !subreddit.matches("^.*[^a-zA-Z0-9\\+ ].*$");
	}

	/**
	 * Get the posts for a specific subreddit. If you want to get the posts from
	 * all the subreddits, you can use "all". You can also search multiple
	 * subreddits at the same time by concatenating subreddits with "+".
	 * 
	 * Examples:
	 * <ul>
	 * <li>getPosts("all", SortMode.NEW);</li>
	 * <li>getPosts("virginiatech", SortMode.TOP);</li>
	 * <li>getPosts("pics+video", SortMode.CONTROVERSIAL);</li>
	 * </ul>
	 * 
	 * @param subreddit
	 *            A string representing the subreddit. Do <i>not</i> include the
	 *            "/r/" that usually precedes a subreddit name.
	 * @param sort
	 *            The way you want results to be sorted.
	 * @param callback
	 *            A callback function that will process the data collected by
	 *            the method.
	 * @throws RedditException
	 */
	public void getPosts(final String subreddit, final SortMode sort,
			final PostListener callback) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				ArrayList<Post> posts = new ArrayList<Post>();
				
				// Check that it's a valid subreddit
				if (!isValidSubreddit(subreddit)) {
					callback.onFailure(new RedditInvalidSubredditException('"'
							+ subreddit + '"' + " is not valid."));
					return;
				}
				
				// We can't sort by BEST, replace it with TOP
				SortMode _sort = sort;
				if (sort == SortMode.BEST) {
					_sort = SortMode.TOP;
				}
				
				String url = "http://www.reddit.com/r/" + subreddit + "/"
						+ _sort.toString() + ".json";
				String json = "";
				
				if (local) {
					HashMap<String, Object> cache = (HashMap<String, Object>) LocalCache.getInstance();
					if (cache.containsKey(url)) {
						json = (String) cache.get(url);
					} else {
						callback.onSuccess(posts);
						return;
					}
				} else {
					// Build our query;
					try {
						// Make the actual request
						json = Requests.get(url, _sort.getUrlArguments());
					} catch (IOException e) {
						// We couldn't connect to Reddit!
						callback.onFailure(new RedditNotAvailableException(e
								.toString()));
					}
				}
				
				try {
					// If we got back HTML, then give up
					if (json.startsWith("<")) {// Uh oh, we got back some random HTML page!
						callback.onFailure(new PostNotFoundException(json));
						return;
					}

					// Extract the list of posts from the JSON
					HashMap<String, Object> rawData = (HashMap<String, Object>) (JsonConverter
							.convertToHashMap(json).get("data"));
					ArrayList<HashMap<String, Object>> children = (ArrayList<HashMap<String, Object>>) rawData
							.get("children");
					for (HashMap<String, Object> child : children) {
						posts.add(new Post(child));
					}
	
					// Successfully hand off the data to the callback
					callback.onSuccess(posts);
					}  catch (ParseException e) {
						// Reddit gave us back some gibberish
						callback.onFailure(new RedditReturnedErrorException(e
								.toString()));
					}
			}
		};
		thread.start();
	}

	/**
	 * Gets the comments for a specific Post.
	 * 
	 * @param post
	 *            The Post you want Comments from.
	 * @param sort
	 *            The order in which you want the results to be sorted.
	 * @param callback
	 *            A function that will process the results or errors.
	 * @throws RedditException
	 */
	public void getComments(final Post post, final SortMode sort,
			final CommentListener callback) {
		getComments(post.getSubreddit(), post.getId(), sort, callback);
	}

	/**
	 * Gets the comments for a specific Post's ID in a subreddit.
	 * 
	 * @param subreddit
	 *            A String representing a subreddit (e.g. "virginiatech" or
	 *            "funny"). Do not include the "/r/".
	 * @param id
	 *            A string representing a specific comment on a post in a
	 *            subreddit. Can be obtained through getId on a Post.
	 * @param sort
	 *            The order in which you want the results to be sorted.
	 * @param callback
	 *            A function that will process the results or errors.
	 * @throws RedditException
	 */
	public void getComments(final String subreddit, final String id,
			final SortMode sort, final CommentListener callback) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				ArrayList<Comment> comments = new ArrayList<Comment>();
				String url = "http://www.reddit.com/r/" + subreddit
						+ "/comments/" + id + "/" + sort.toString() + ".json";

				// Check that it's a valid subreddit
				if (!isValidSubreddit(subreddit)) {
					callback.onFailure(new RedditInvalidSubredditException('"'
							+ subreddit + '"' + " is not valid."));
					return;
				}
				
				String json = "";
				if (local) {
					HashMap<String, Object> cache = (HashMap<String, Object>) LocalCache.getInstance();
					if (cache.containsKey(url)) {
						json = (String) cache.get(url);
					} else {
						callback.onSuccess(comments);
						return;
					}
				} else {
					// Make the actual request
					try {
						json = Requests.get(url, sort.getUrlArguments());
					} catch (IOException e) {
						callback.onFailure(new RedditNotAvailableException(e
								.toString()));
						return;
					}
				}
				
				if (json.equals("{\"error\": 404}")) {
					callback.onFailure(new CommentNotFoundException(json));
					return;
				}

				try {
					// Parse the resulting replies
					HashMap<String, Object> rawData = (HashMap<String, Object>) JsonConverter
							.convertToArrayList(json).get(1);
					rawData = (HashMap<String, Object>) rawData.get("data");
					ArrayList<HashMap<String, Object>> children = (ArrayList<HashMap<String, Object>>) rawData
							.get("children");
					for (HashMap<String, Object> child : children) {
						comments.add(new Comment(child));
					}

					// Pass along the result
					callback.onSuccess(comments);
				} catch (ParseException e) {
					callback.onFailure(new RedditReturnedErrorException(e
							.toString()));
					return;
				}
			}
		};
		thread.start();
	}

}

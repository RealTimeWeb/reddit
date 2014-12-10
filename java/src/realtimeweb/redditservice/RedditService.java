package realtimeweb.redditservice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import realtimeweb.redditservice.domain.*;
import realtimeweb.stickyweb.EditableCache;
import realtimeweb.stickyweb.StickyWeb;
import realtimeweb.stickyweb.StickyWebRequest;
import realtimeweb.stickyweb.StickyWebResponse;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceNotFoundException;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceParseException;
import realtimeweb.stickyweb.exceptions.StickyWebInternetException;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidPostArguments;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidQueryString;
import realtimeweb.stickyweb.exceptions.StickyWebJsonResponseParseException;
import realtimeweb.stickyweb.exceptions.StickyWebLoadDataSourceException;
import realtimeweb.stickyweb.exceptions.StickyWebNotInCacheException;

/**
 * Get the Front Page of the internet.
 */
public class RedditService {
    private StickyWeb connection;
	private boolean online;
    
    public static void main(String[] args) {
        RedditService redditService = new RedditService();
        
        // The following pre-generated code demonstrates how you can
		// use StickyWeb's EditableCache to create data files.
		try {
            // First, you create a new EditableCache, possibly passing in an FileInputStream to an existing cache
			EditableCache recording = new EditableCache();
            // You can add a Request object directly to the cache.
			// recording.addData(redditService.getPostsRequest(...));
            // Then you can save the expanded cache, possibly over the original
			recording.saveToStream(new FileOutputStream("cache.json"));
		} catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("The given FileStream was not able to be found.");
		} catch (StickyWebDataSourceParseException e) {
			System.err.println("The given FileStream could not be parsed; possibly the structure is incorrect.");
		} catch (StickyWebLoadDataSourceException e) {
			System.err.println("The given data source could not be loaded.");
		} catch (FileNotFoundException e) {
			System.err.println("The given cache.json file was not found, or could not be opened.");
		}
        // ** End of how to use the EditableCache
    }
	
    /**
     * Create a new, online connection to the service
     */
	public RedditService() {
        this.online = true;
		try {
			this.connection = new StickyWeb(null);
		} catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("The given datastream could not be loaded.");
		} catch (StickyWebDataSourceParseException e) {
			System.err.println("The given datastream could not be parsed");
		} catch (StickyWebLoadDataSourceException e) {
			System.err.println("The given data source could not be loaded");
		}
	}
	
    /**
     * Create a new, offline connection to the service.
     * @param cache The filename of the cache to be used.
     */
	public RedditService(String cache) {
        // TODO: You might consider putting the cache directly into the jar file,
        // and not even exposing filenames!
        try {
            this.online = false;
            this.connection = new StickyWeb(new FileInputStream(cache));
        } catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("The given data source could not be found.");
            System.exit(1);
		} catch (StickyWebDataSourceParseException e) {
			System.err.println("Could not read the data source. Perhaps its format is incorrect?");
            System.exit(1);
		} catch (StickyWebLoadDataSourceException e) {
			System.err.println("The given data source could not be read.");
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.err.println("The given cache file could not be found. Make sure it is in the right folder.");
			System.exit(1);
		}
	}
    
    
    /**
     * Retrieves all the top posts
     *
     * This version of the function meant for instructors to capture a
     * StickyWebRequest object which can be put into an EditableCache and then
     * stored to a "cache.json" file.
     * 
     * @param sort_Mode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
     * @return a StickyWebRequest
     * @param subreddit The subreddit that Posts will be returned from (without the "r/" preceeding it). Use "all" to return results from all subreddits.
     * @return a StickyWebRequest
     */
    public StickyWebRequest getPostsRequest(String subreddit, String sortMode) {
        try {
            /*
            * Perform any user parameter validation here. E.g.,
            * if the first argument can't be zero, or they give an empty string.
            */
            
            // Build up query string
            final String url = String.format("http://www.reddit.com/r/%s/%s.json", String.valueOf(subreddit), String.valueOf(sortMode));
            
            // Build up the query arguments that will be sent to the server
            HashMap<String, String> parameters = new HashMap<String, String>();
            
            // Build up the list of actual arguments that should be used to
            // create the local cache hash key
            ArrayList<String> indexList = new ArrayList<String>();
            
            
            // Build and return the connection object.
            return connection.get(url, parameters)
                            .setOnline(online)
                            .setIndexes(indexList);
        
        } catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("Could not find the data source.");
		}
        return null;
    }
    
    /**
     * Retrieves all the top posts
    
     * @param sort_Mode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
     * @param subreddit The subreddit that Posts will be returned from (without the "r/" preceeding it). Use "all" to return results from all subreddits.
     * @return a Post[]
     */
	public ArrayList<Post> getPosts(String sort_Mode, String subreddit) {
        
        // Need to filter by data->children
        try {
			StickyWebRequest request =  getPostsRequest(sort_Mode, subreddit);
            
            ArrayList<Post> result = new ArrayList<Post>();
            StickyWebResponse response = request.execute();
            // TODO: Validate the output here using response.isNull, response.asText, etc.
            if (response.isNull())
                return result;
            Iterator<Object> resultIter = ((ArrayList<Object>) ((Map<String, Object>) ((Map<String, Object>) response.asJSON()).get("data")).get("children")).iterator();
            while (resultIter.hasNext()) {
                result.add(new Post((Map<String, Object>)resultIter.next()));	//modified: cast
            }
            return result;
		} catch (StickyWebNotInCacheException e) {
			System.err.println("There is no query in the cache for the given inputs. Perhaps something was mispelled?");
		} catch (StickyWebInternetException e) {
			System.err.println("Could not connect to the web service. It might be your internet connection, or a problem with the web service.");
		} catch (StickyWebInvalidQueryString e) {
			System.err.println("The given arguments were invalid, and could not be turned into a query.");
		} catch (StickyWebInvalidPostArguments e) {
			System.err.println("The given arguments were invalid, and could not be turned into a query.");
        
        } catch (StickyWebJsonResponseParseException e) {
            System.err.println("The response from the server couldn't be understood.");
        
		}
		return null;
	}
    
    
     /**
     * Retrieves comments for a post
     *
     * This version of the function meant for instructors to capture a
     * StickyWebRequest object which can be put into an EditableCache and then
     * stored to a "cache.json" file.
     * 
     * @param subreddit 
     * @return a StickyWebRequest
     * @param id The unique id of a Post from which Comments will be returned.
     * @return a StickyWebRequest
     * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
     * @return a StickyWebRequest
     */
    public StickyWebRequest getCommentsRequest(String subreddit, String id, String sortMode) {
        try {
            /*
            * Perform any user parameter validation here. E.g.,
            * if the first argument can't be zero, or they give an empty string.
            */
            
            // Build up query string
            final String url = String.format("http://www.reddit.com/r/%s/comments/%s/%s.json", String.valueOf(subreddit), String.valueOf(id), String.valueOf(sortMode));
            
            // Build up the query arguments that will be sent to the server
            HashMap<String, String> parameters = new HashMap<String, String>();
            
            // Build up the list of actual arguments that should be used to
            // create the local cache hash key
            ArrayList<String> indexList = new ArrayList<String>();
            
            
            // Build and return the connection object.
            return connection.get(url, parameters)
                            .setOnline(online)
                            .setIndexes(indexList);
        
        } catch (StickyWebDataSourceNotFoundException e) {
			System.err.println("Could not find the data source.");
		}
        return null;
    }
    
    /**
     * Retrieves comments for a post
    
     * @param subreddit 
     * @param id The unique id of a Post from which Comments will be returned.
     * @param sortMode The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
     * @return a Comment[]
     */
	public ArrayList<Comment> getComments(String subreddit, String id, String sortMode) {
        
        // Need to filter by data->children. Also, skip the first element, and the last element.
        try {
			StickyWebRequest request =  getCommentsRequest(subreddit, id, sortMode);
            
            ArrayList<Comment> result = new ArrayList<Comment>();
            StickyWebResponse response = request.execute();
            // TODO: Validate the output here using response.isNull, response.asText, etc.
            if (response.isNull())
                return result;
            Iterator<Object> resultIter = ((ArrayList<Object>) ((Map<String, Object>) ((Map<String, Object>) ((ArrayList<Object>) response.asJSONArray()).get(1)).get("data")).get("children")).iterator();
            while (resultIter.hasNext()) {
                result.add(new Comment((Map<String, Object>)resultIter.next()));
            }
            return result;
		} catch (StickyWebNotInCacheException e) {
			System.err.println("There is no query in the cache for the given inputs. Perhaps something was mispelled?");
		} catch (StickyWebInternetException e) {
			System.err.println("Could not connect to the web service. It might be your internet connection, or a problem with the web service.");
		} catch (StickyWebInvalidQueryString e) {
			System.err.println("The given arguments were invalid, and could not be turned into a query.");
		} catch (StickyWebInvalidPostArguments e) {
			System.err.println("The given arguments were invalid, and could not be turned into a query.");
        
        } catch (StickyWebJsonResponseParseException e) {
            System.err.println("The response from the server couldn't be understood.");
        
		}
		return null;
	}
}
package realtimeweb.redditservice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
 * An example service that shows generally how the spec file is structured.
 */
public class Redditservice {
    private StickyWeb connection;
	private boolean online;
    
    public static void main(String[] args) {
        Redditservice redditservice = new Redditservice();
        //later might need to add another parameter for the time range (today,week,month, alltime)
        //http://www.reddit.com/r/science/top
        //getting list of post
        Postlisting l = redditservice.getpostlisting("science","top");
        System.out.println(l.getPosts().get(0));

        //getting list of comments for specific post id
        //http://www.reddit.com/r/science/comments/2hf891/scientists_discover_a_new_function_attributed_to/
        Listing cl = redditservice.getcommentlisting("science", "2hf891", "top");
        System.out.println(cl.getCommentlisting());
        

        
        // The following pre-generated code demonstrates how you can
		// use StickyWeb's EditableCache to create data files.
		try {
            // First, you create a new EditableCache, possibly passing in an FileInputStream to an existing cache
			EditableCache recording = new EditableCache();
            // You can add a Request object directly to the cache.
			// recording.addData(redditservice.getcommentlistingRequest(...));
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
	public Redditservice() {
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
	public Redditservice(String cache) {
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
     * get listing
     *
     * This version of the function meant for instructors to capture a
     * StickyWebRequest object which can be put into an EditableCache and then
     * stored to a "cache.json" file.
     * 
     * @param subreddit 
     * @return a StickyWebRequest
     * @param id 
     * @return a StickyWebRequest
     * @param sortmode 
     * @return a StickyWebRequest
     */
    private StickyWebRequest getcommentlistingRequest(String subreddit, String id, String sortmode) {
        try {
            /*
            * Perform any user parameter validation here. E.g.,
            * if the first argument can't be zero, or they give an empty string.
            */
            
            // Build up query string
            final String url = String.format("http://www.reddit.com/r/%s/comments/%s/%s.json", String.valueOf(subreddit), String.valueOf(id), String.valueOf(sortmode));
            
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
     * get listing
    
     * @param subreddit 
     * @param id 
     * @param sortmode 
     * @return a Listing
     */
	public Listing getcommentlisting(String subreddit, String id, String sortmode) {
        try {
			StickyWebRequest request =  getcommentlistingRequest(subreddit, id, sortmode);
            return new Listing((List<Object>)request.execute().asJSONArray());
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
     * Retrieves all the top posts
     *
     * This version of the function meant for instructors to capture a
     * StickyWebRequest object which can be put into an EditableCache and then
     * stored to a "cache.json" file.
     * 
     * @param subreddit The subreddit that Posts will be returned from (without the "r/" preceeding it). Use "all" to return results from all subreddits.
     * @return a StickyWebRequest
     * @param sortmode  
     * @return a StickyWebRequest
     */
    private StickyWebRequest getpostlistingRequest(String subreddit, String sortmode) {
        try {
            /*
            * Perform any user parameter validation here. E.g.,
            * if the first argument can't be zero, or they give an empty string.
            */
            
            // Build up query string
            final String url = String.format("http://www.reddit.com/r/%s/%s.json", String.valueOf(subreddit), String.valueOf(sortmode));
            
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
    
     * @param subreddit The subreddit that Posts will be returned from (without the "r/" preceeding it). Use "all" to return results from all subreddits.
     * @param sortmode  
     * @return a PostListing
     */
	public Postlisting getpostlisting(String subreddit, String sortmode) {
        
        // Need to filter by data->children
        try {
			StickyWebRequest request =  getpostlistingRequest(subreddit, sortmode);
            return new Postlisting((Map<String, Object>)request.execute().asJSON());
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
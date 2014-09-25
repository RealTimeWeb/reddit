package realtimeweb.redditservice.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




import realtimeweb.redditservice.domain.Post;

/**
 * 
 */
public class Postlisting {
	
    // They can have an optional comment.
    
    private ArrayList<Post> posts;
    
    
    /*
     * @return 
     */
    public ArrayList<Post> getPosts() {
        return this.posts;
    }
    
    /*
     * @param 
     * @return ArrayList<Post>
     */
    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }
    
	
	/**
	 * Creates a string based representation of this Postlisting.
	
	 * @return String
	 */
	public String toString() {
		return "Postlisting[" +posts+"]";
	}
	
	/**
	 * Internal constructor to create a Postlisting from a json representation.
	 * @param map The raw json data that will be parsed.
	 * @return 
	 */
    public Postlisting(Map<String, Object> raw) {
        // TODO: Check that the data has the correct schema.
        // NOTE: It's much safer to check the Map for fields than to catch a runtime exception.
        try {
            this.posts = new ArrayList<Post>();
            Iterator<Object> postsIter = ((List<Object>)((Map<String, Object>) raw.get("data")).get("children")).iterator();
            while (postsIter.hasNext()) {
                this.posts.add(new Post((Map<String, Object>)postsIter.next()));
            }
        } catch (NullPointerException e) {
    		System.err.println("Could not convert the response to a Postlisting; a field was missing.");
    		e.printStackTrace();
    	} catch (ClassCastException e) {
    		System.err.println("Could not convert the response to a Postlisting; a field had the wrong structure.");
    		e.printStackTrace();
        }
    
	}	
}
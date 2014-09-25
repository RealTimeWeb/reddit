package realtimeweb.redditservice.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import realtimeweb.redditservice.domain.Commentlisting;
import realtimeweb.redditservice.domain.Postlisting;

/**
 * 
 */
public class Listing {
	
    private Commentlisting commentlisting;
    private Postlisting postlisting;
    
    
    /*
     * @return 
     */
    public Commentlisting getCommentlisting() {
        return this.commentlisting;
    }
    
    /*
     * @param 
     * @return Commentlisting
     */
    public void setCommentlisting(Commentlisting commentlisting) {
        this.commentlisting = commentlisting;
    }
    
    /*
     * @return 
     */
    public Postlisting getPostlisting() {
        return this.postlisting;
    }
    
    /*
     * @param 
     * @return Postlisting
     */
    public void setPostlisting(Postlisting postlisting) {
        this.postlisting = postlisting;
    }
    
	
	/**
	 * Creates a string based representation of this Listing.
	
	 * @return String
	 */
	public String toString() {
		return "Listing[" +commentlisting+", "+postlisting+"]";
	}
	
	/**
	 * Internal constructor to create a Listing from a json representation.
	 * @param map The raw json data that will be parsed.
	 * @return 
	 */
    public Listing(List<Object> raw) {
        // TODO: Check that the data has the correct schema.
        // NOTE: It's much safer to check the Map for fields than to catch a runtime exception.
        try {
            this.commentlisting = new Commentlisting((Map<String, Object>)raw.get(1));
            this.postlisting = new Postlisting((Map<String, Object>)raw.get(0));
        } catch (NullPointerException e) {
    		System.err.println("Could not convert the response to a Listing; a field was missing.");
    		e.printStackTrace();
    	} catch (ClassCastException e) {
    		System.err.println("Could not convert the response to a Listing; a field had the wrong structure.");
    		e.printStackTrace();
        }
    
	}	
}
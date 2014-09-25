package realtimeweb.redditservice.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




import realtimeweb.redditservice.domain.Comment;

/**
 * 
 */
public class Commentlisting {
	
    // They can have an optional comment.
    
    // A comment is stored in the generated source code, but not in the docstrings.
    private ArrayList<Comment> comments;
    
    
    /*
     * @return This will be in the fields docstring.
     */
    public ArrayList<Comment> getComments() {
        return this.comments;
    }
    
    /*
     * @param This will be in the fields docstring.
     * @return ArrayList<Comment>
     */
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
    
	
	/**
	 * Creates a string based representation of this Commentlisting.
	
	 * @return String
	 */
	public String toString() {
		return "Commentlisting[" +comments+"]";
	}
	
	/**
	 * Internal constructor to create a Commentlisting from a json representation.
	 * @param map The raw json data that will be parsed.
	 * @return 
	 */
    public Commentlisting(Map<String, Object> raw) {
        // TODO: Check that the data has the correct schema.
        // NOTE: It's much safer to check the Map for fields than to catch a runtime exception.
        try {
            this.comments = new ArrayList<Comment>();
            Iterator<Object> commentsIter = ((List<Object>)((Map<String, Object>) raw.get("data")).get("children")).iterator();
            while (commentsIter.hasNext()) {
                this.comments.add(new Comment((Map<String, Object>)commentsIter.next()));
            }
        } catch (NullPointerException e) {
    		System.err.println("Could not convert the response to a Commentlisting; a field was missing.");
    		e.printStackTrace();
    	} catch (ClassCastException e) {
    		System.err.println("Could not convert the response to a Commentlisting; a field had the wrong structure.");
    		e.printStackTrace();
        }
    
	}	
}
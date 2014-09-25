package realtimeweb.redditservice.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




import realtimeweb.redditservice.domain.Commentlisting;

/**
 * 
 */
public class Comment {
	
    private Integer created;
    private Integer downs;
    private String author;
    private String subreddit;
    private String content;
    private Commentlisting replies;
    private String id;
    private String link_Id;
    private Integer ups;
    
    
    /*
     * @return 
     */
    public Integer getCreated() {
        return this.created;
    }
    
    /*
     * @param 
     * @return Integer
     */
    public void setCreated(Integer created) {
        this.created = created;
    }
    
    /*
     * @return 
     */
    public Integer getDowns() {
        return this.downs;
    }
    
    /*
     * @param 
     * @return Integer
     */
    public void setDowns(Integer downs) {
        this.downs = downs;
    }
    
    /*
     * @return 
     */
    public String getAuthor() {
        return this.author;
    }
    
    /*
     * @param 
     * @return String
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    
    /*
     * @return name for sub reddit
     */
    public String getSubreddit() {
        return this.subreddit;
    }
    
    /*
     * @param name for sub reddit
     * @return String
     */
    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }
    
    /*
     * @return 
     */
    public String getContent() {
        return this.content;
    }
    
    /*
     * @param 
     * @return String
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /*
     * @return 
     */
    public Commentlisting getReplies() {
        return this.replies;
    }
    
    /*
     * @param 
     * @return Commentlisting
     */
    public void setReplies(Commentlisting replies) {
        this.replies = replies;
    }
    
    /*
     * @return id of the post
     */
    public String getId() {
        return this.id;
    }
    
    /*
     * @param id of the post
     * @return String
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /*
     * @return 
     */
    public String getLink_Id() {
        return this.link_Id;
    }
    
    /*
     * @param 
     * @return String
     */
    public void setLink_Id(String link_Id) {
        this.link_Id = link_Id;
    }
    
    /*
     * @return 
     */
    public Integer getUps() {
        return this.ups;
    }
    
    /*
     * @param 
     * @return Integer
     */
    public void setUps(Integer ups) {
        this.ups = ups;
    }
    
	
	/**
	 * Creates a string based representation of this Comment.
	
	 * @return String
	 */
	public String toString() {
		return "Comment[" +created+", "+downs+", "+author+", "+subreddit+", "+content+", "+replies+", "+id+", "+link_Id+", "+ups+"]";
	}
	
	/**
	 * Internal constructor to create a Comment from a json representation.
	 * @param map The raw json data that will be parsed.
	 * @return 
	 */
    public Comment(Map<String, Object> raw) {
        // TODO: Check that the data has the correct schema.
        // NOTE: It's much safer to check the Map for fields than to catch a runtime exception.
        try {
        	//note:handle bigdecimal
            this.created = new BigDecimal(((Map<String, Object>) raw.get("data")).get("created").toString()).intValue();
            this.downs = Integer.parseInt(((Map<String, Object>) raw.get("data")).get("downs").toString());
            this.author = ((Map<String, Object>) raw.get("data")).get("author").toString();
            this.subreddit = ((Map<String, Object>) raw.get("data")).get("subreddit").toString();
            this.content = ((Map<String, Object>) raw.get("data")).get("body").toString();
          //note:handle the tree structure where no further replies occured
            try{	
            	Map<String, Object> temp  = (Map<String, Object>) ((Map<String, Object>) raw.get("data")).get("replies");
            	this.replies = new Commentlisting(temp);
            }catch(Exception e){
            	this.replies = null;
            }
            this.id = ((Map<String, Object>) raw.get("data")).get("id").toString();
            this.link_Id = ((Map<String, Object>) raw.get("data")).get("link_id").toString();
            this.ups = Integer.parseInt(((Map<String, Object>) raw.get("data")).get("ups").toString());
        } catch (NullPointerException e) {
    		System.err.println("Could not convert the response to a Comment; a field was missing.");
    		e.printStackTrace();
    	} catch (ClassCastException e) {
    		System.err.println("Could not convert the response to a Comment; a field had the wrong structure.");
    		e.printStackTrace();
        }
    
	}	
}
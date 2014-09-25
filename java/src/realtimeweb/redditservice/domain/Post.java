package realtimeweb.redditservice.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




/**
 * 
 */
public class Post {
	
    private String content;
    // prepend reddit.com to this
    private String permalink;
    private String title;
    private Integer downs;
    private String author;
    private String subreddit;
    private Boolean is_Nsfw;
    private Integer created;
    private String id;
    private Boolean is_Self;
    private Integer ups;
    
    
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
    public String getPermalink() {
        return this.permalink;
    }
    
    /*
     * @param 
     * @return String
     */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }
    
    /*
     * @return 
     */
    public String getTitle() {
        return this.title;
    }
    
    /*
     * @param 
     * @return String
     */
    public void setTitle(String title) {
        this.title = title;
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
    public Boolean getIs_Nsfw() {
        return this.is_Nsfw;
    }
    
    /*
     * @param 
     * @return Boolean
     */
    public void setIs_Nsfw(Boolean is_Nsfw) {
        this.is_Nsfw = is_Nsfw;
    }
    
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
    public Boolean getIs_Self() {
        return this.is_Self;
    }
    
    /*
     * @param 
     * @return Boolean
     */
    public void setIs_Self(Boolean is_Self) {
        this.is_Self = is_Self;
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
	 * Creates a string based representation of this Post.
	
	 * @return String
	 */
	public String toString() {
		return "Post[" +content+", "+permalink+", "+title+", "+downs+", "+author+", "+subreddit+", "+is_Nsfw+", "+created+", "+id+", "+is_Self+", "+ups+"]";
	}
	
	/**
	 * Internal constructor to create a Post from a json representation.
	 * @param map The raw json data that will be parsed.
	 * @return 
	 */
    public Post(Map<String, Object> raw) {
        // TODO: Check that the data has the correct schema.
        // NOTE: It's much safer to check the Map for fields than to catch a runtime exception.
        try {
            this.content = ((Map<String, Object>) raw.get("data")).get("selftext").toString();
            this.permalink = ((Map<String, Object>) raw.get("data")).get("permalink").toString();
            this.title = ((Map<String, Object>) raw.get("data")).get("title").toString();
            this.downs = Integer.parseInt(((Map<String, Object>) raw.get("data")).get("downs").toString());
            this.author = ((Map<String, Object>) raw.get("data")).get("author").toString();
            this.subreddit = ((Map<String, Object>) raw.get("data")).get("subreddit").toString();
            this.is_Nsfw = Boolean.parseBoolean(((Map<String, Object>) raw.get("data")).get("over_18").toString());
            //note:handle big decimal
            this.created = new BigDecimal(((Map<String, Object>) raw.get("data")).get("created").toString()).intValue();
            this.id = ((Map<String, Object>) raw.get("data")).get("id").toString();
            this.is_Self = Boolean.parseBoolean(((Map<String, Object>) raw.get("data")).get("is_self").toString());
            this.ups = Integer.parseInt(((Map<String, Object>) raw.get("data")).get("ups").toString());
        } catch (NullPointerException e) {
    		System.err.println("Could not convert the response to a Post; a field was missing.");
    		e.printStackTrace();
    	} catch (ClassCastException e) {
    		System.err.println("Could not convert the response to a Post; a field had the wrong structure.");
    		e.printStackTrace();
        }
    
	}	
}
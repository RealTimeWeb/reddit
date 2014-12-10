package realtimeweb.redditservice.domain;

import java.math.BigDecimal;
import java.util.Map;




/**
 * A link (or self-text) that has been submitted to Reddit.
 */
public class Post {
	
    // prepend reddit.com to this
    private String permalink;
    private String author;
    private String title;
    private Integer downs;
    // epoch time, save as long, convert to date
    private Long created;
    private String subreddit;
    // if is self then use data->url
    private String content;
    private Boolean isSelf;
    private String id;
    private Integer ups;
    private Boolean isNsfw;
    
    
    /*
     * @return A permanent url that directs to this Post.
     */
    public String getPermalink() {
        return this.permalink;
    }
    
    /*
     * @param A permanent url that directs to this Post.
     * @return String
     */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }
    
    /*
     * @return The username of the author of this Post.
     */
    public String getAuthor() {
        return this.author;
    }
    
    /*
     * @param The username of the author of this Post.
     * @return String
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    
    /*
     * @return The title of this Post.
     */
    public String getTitle() {
        return this.title;
    }
    
    /*
     * @param The title of this Post.
     * @return String
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /*
     * @return The number of downvotes associated with this Post.
     */
    public Integer getDowns() {
        return this.downs;
    }
    
    /*
     * @param The number of downvotes associated with this Post.
     * @return Integer
     */
    public void setDowns(Integer downs) {
        this.downs = downs;
    }
    
    /*
     * @return The date that this Post was created.
     */
    public Long getCreated() {
        return this.created;
    }
    
    /*
     * @param The date that this Post was created.
     * @return Integer
     */
    public void setCreated(Long created) {
        this.created = created;
    }
    
    /*
     * @return The subreddit that this Post was made in.
     */
    public String getSubreddit() {
        return this.subreddit;
    }
    
    /*
     * @param The subreddit that this Post was made in.
     * @return String
     */
    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }
    
    /*
     * @return The text of the post, or a url if it is not a self Post.
     */
    public String getContent() {
        return this.content;
    }
    
    /*
     * @param The text of the post, or a url if it is not a self Post.
     * @return String
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /*
     * @return Whether or not this Post was text (True), or a URL (False).
     */
    public Boolean getIsSelf() {
        return this.isSelf;
    }
    
    /*
     * @param Whether or not this Post was text (True), or a URL (False).
     * @return Boolean
     */
    public void setIsSelf(Boolean isSelf) {
        this.isSelf = isSelf;
    }
    
    /*
     * @return A unique ID for this Post. A combination of letters, numbers, and dashes.
     */
    public String getId() {
        return this.id;
    }
    
    /*
     * @param A unique ID for this Post. A combination of letters, numbers, and dashes.
     * @return String
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /*
     * @return The number of upvotes associated with this Post.
     */
    public Integer getUps() {
        return this.ups;
    }
    
    /*
     * @param The number of upvotes associated with this Post.
     * @return Integer
     */
    public void setUps(Integer ups) {
        this.ups = ups;
    }
    
    /*
     * @return Whether or not this Post is Not Safe for Work (NSFW).
     */
    public Boolean getIsNsfw() {
        return this.isNsfw;
    }
    
    /*
     * @param Whether or not this Post is Not Safe for Work (NSFW).
     * @return Boolean
     */
    public void setIsNsfw(Boolean isNsfw) {
        this.isNsfw = isNsfw;
    }
    
	
	/**
	 * Creates a string based representation of this Post.
	
	 * @return String
	 */
	public String toString() {
		return "Post[" +permalink+", "+author+", "+title+", "+downs+", "+created+", "+subreddit+", "+content+", "+isSelf+", "+id+", "+ups+", "+isNsfw+"]";
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
            this.permalink = ((Map<String, Object>) raw.get("data")).get("permalink").toString();
            this.author = ((Map<String, Object>) raw.get("data")).get("author").toString();
            this.title = ((Map<String, Object>) raw.get("data")).get("title").toString();
            this.downs = Integer.parseInt(((Map<String, Object>) raw.get("data")).get("downs").toString());
            this.created = new BigDecimal(((Map<String, Object>) raw.get("data")).get("created").toString()).longValue();
            this.subreddit = ((Map<String, Object>) raw.get("data")).get("subreddit").toString();
            this.content = ((Map<String, Object>) raw.get("data")).get("selftext").toString();
            this.isSelf = Boolean.parseBoolean(((Map<String, Object>) raw.get("data")).get("is_self").toString());
            this.id = ((Map<String, Object>) raw.get("data")).get("id").toString();
            this.ups = Integer.parseInt(((Map<String, Object>) raw.get("data")).get("ups").toString());
            this.isNsfw = Boolean.parseBoolean(((Map<String, Object>) raw.get("data")).get("over_18").toString());
        } catch (NullPointerException e) {
    		System.err.println("Could not convert the response to a Post; a field was missing.");
    		e.printStackTrace();
    	} catch (ClassCastException e) {
    		System.err.println("Could not convert the response to a Post; a field had the wrong structure.");
    		e.printStackTrace();
        }
    
	}	
}
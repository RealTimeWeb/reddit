package realtimeweb.redditservice.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;










import realtimeweb.redditservice.domain.Comment;

/**
 * A Comment on either a Post or another Comment.
 */
public class Comment {
	
    private String body;
    // epoch time, save as long, convert to date
    private Long created;
    private Integer downs;
    private String author;
    private String subreddit;
    private String bodyHtml;
    // filter by replies.data.children
    private ArrayList<Comment> replies;
    private String id;
    private Integer ups;
    
    
    /*
     * @return The text of this post, without any markup.
     */
    public String getBody() {
        return this.body;
    }
    
    /*
     * @param The text of this post, without any markup.
     * @return String
     */
    public void setBody(String body) {
        this.body = body;
    }
    
    /*
     * @return The date that this Comment was created.
     */
    public Long getCreated() {
        return this.created;
    }
    
    /*
     * @param The date that this Comment was created.
     * @return Integer
     */
    public void setCreated(Long created) {
        this.created = created;
    }
    
    /*
     * @return The number of downvotes associated with this Comment.
     */
    public Integer getDowns() {
        return this.downs;
    }
    
    /*
     * @param The number of downvotes associated with this Comment.
     * @return Integer
     */
    public void setDowns(Integer downs) {
        this.downs = downs;
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
     * @return The subreddit that this Comment was made in.
     */
    public String getSubreddit() {
        return this.subreddit;
    }
    
    /*
     * @param The subreddit that this Comment was made in.
     * @return String
     */
    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }
    
    /*
     * @return The HTML text of this post.
     */
    public String getBodyHtml() {
        return this.bodyHtml;
    }
    
    /*
     * @param The HTML text of this post.
     * @return String
     */
    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }
    
    /*
     * @return A list of comments that are in reply to this one.
     */
    public ArrayList<Comment> getReplies() {
        return this.replies;
    }
    
    /*
     * @param A list of comments that are in reply to this one.
     * @return ArrayList<Comment>
     */
    public void setReplies(ArrayList<Comment> replies) {
        this.replies = replies;
    }
    
    /*
     * @return A unique ID for this Comment. A combination of letters, numbers, and dashes.
     */
    public String getId() {
        return this.id;
    }
    
    /*
     * @param A unique ID for this Comment. A combination of letters, numbers, and dashes.
     * @return String
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /*
     * @return The number of upvotes associated with this Comment.
     */
    public Integer getUps() {
        return this.ups;
    }
    
    /*
     * @param The number of upvotes associated with this Comment.
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
		return "Comment[" +body+", "+created+", "+downs+", "+author+", "+subreddit+", "+bodyHtml+", "+replies+", "+id+", "+ups+"]";
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
            this.body = ((Map<String, Object>) raw.get("data")).get("body").toString();
            this.created = new BigDecimal(((Map<String, Object>) raw.get("data")).get("created").toString()).longValue();
            this.downs = Integer.parseInt(((Map<String, Object>) raw.get("data")).get("downs").toString());
            this.author = ((Map<String, Object>) raw.get("data")).get("author").toString();
            this.subreddit = ((Map<String, Object>) raw.get("data")).get("subreddit").toString();
            this.bodyHtml = ((Map<String, Object>) raw.get("data")).get("body_html").toString();
            this.replies = new ArrayList<Comment>();

            
            String temp = (String) ((Map<String, Object>) raw.get("data")).get("replies").toString();
//
//            if(!temp.isEmpty()){
//            	System.out.println("------------->"+temp);
//            }
            
            if(!temp.isEmpty()){
        		Iterator<Object> repliesIter = ((List<Object>)((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) raw.get("data")).get("replies")).get("data")).get("children")).iterator();
                while (repliesIter.hasNext()) {
                    this.replies.add(new Comment((Map<String, Object>)repliesIter.next()));
                }
        
            }
            
            
            this.id = ((Map<String, Object>) raw.get("data")).get("id").toString();
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
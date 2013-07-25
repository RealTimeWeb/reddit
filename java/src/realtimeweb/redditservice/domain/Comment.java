package realtimeweb.redditservice.domain;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * 
 */
public class Comment {
	
	
	private int ups;
	private int downs;
	private int created;    //epoch time, save as long, convert to date
	private String subreddit;
	private String id;
	private String author;
	private String body;
	private String bodyHtml;
	private ArrayList<Comment> replies;    //filter by replies->data->children
	
	
	/**
	 * The number of upvotes associated with this Comment.
	
	 * @return int
	 */
	public int getUps() {
		return this.ups;
	}
	
	/**
	 * 
	 * @param ups The number of upvotes associated with this Comment.
	 */
	public void setUps(int ups) {
		this.ups = ups;
	}
	
	/**
	 * The number of downvotes associated with this Comment.
	
	 * @return int
	 */
	public int getDowns() {
		return this.downs;
	}
	
	/**
	 * 
	 * @param downs The number of downvotes associated with this Comment.
	 */
	public void setDowns(int downs) {
		this.downs = downs;
	}
	
	/**
	 * The date that this Comment was created.
	
	 * @return int
	 */
	public int getCreated() {
		return this.created;
	}
	
	/**
	 * 
	 * @param created The date that this Comment was created.
	 */
	public void setCreated(int created) {
		this.created = created;
	}
	
	/**
	 * The subreddit that this Comment was made in.
	
	 * @return String
	 */
	public String getSubreddit() {
		return this.subreddit;
	}
	
	/**
	 * 
	 * @param subreddit The subreddit that this Comment was made in.
	 */
	public void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
	}
	
	/**
	 * A unique ID for this Comment. A combination of letters, numbers, and dashes.
	
	 * @return String
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 
	 * @param id A unique ID for this Comment. A combination of letters, numbers, and dashes.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * The username of the author of this Post.
	
	 * @return String
	 */
	public String getAuthor() {
		return this.author;
	}
	
	/**
	 * 
	 * @param author The username of the author of this Post.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * The text of this post, without any markup.
	
	 * @return String
	 */
	public String getBody() {
		return this.body;
	}
	
	/**
	 * 
	 * @param body The text of this post, without any markup.
	 */
	public void setBody(String body) {
		this.body = body;
	}
	
	/**
	 * The HTML text of this post.
	
	 * @return String
	 */
	public String getBodyHtml() {
		return this.bodyHtml;
	}
	
	/**
	 * 
	 * @param bodyHtml The HTML text of this post.
	 */
	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}
	
	/**
	 * A list of comments that are in reply to this one.
	
	 * @return ArrayList<Comment>
	 */
	public ArrayList<Comment> getReplies() {
		return this.replies;
	}
	
	/**
	 * 
	 * @param replies A list of comments that are in reply to this one.
	 */
	public void setReplies(ArrayList<Comment> replies) {
		this.replies = replies;
	}
	
	
	
	/**
	 * 
	
	 * @return String
	 */
	public String toString() {
		return "Comment[" + ups + ", " + downs + ", " + created + ", " + subreddit + ", " + id + ", " + author + ", " + body + ", " + bodyHtml + ", " + replies + "]";
	}
	
	/**
	 * Internal constructor to create a Comment from a Json representation.
	 * @param json The raw json data that will be parsed.
	 * @param gson The Gson parser. See <a href='https://code.google.com/p/google-gson/'>https://code.google.com/p/google-gson/</a> for more information.
	 * @return 
	 */
	public  Comment(JsonObject json, Gson gson) {
		JsonObject data = json.get("data").getAsJsonObject();
		this.ups = data.get("ups").getAsInt();
		this.downs = data.get("downs").getAsInt();
		this.created = data.get("created").getAsInt();
		this.subreddit = data.get("subreddit").getAsString();
		this.id = data.get("id").getAsString();
		this.author = data.get("author").getAsString();
		this.body = data.get("body").getAsString();
		this.bodyHtml = data.get("body_html").getAsString();
		if (!data.get("replies").isJsonPrimitive()) {
			if (data.get("replies").getAsJsonObject().get("kind").getAsString().equals("t1")) {
				JsonArray allChildren = data.get("replies").getAsJsonObject().get("data").getAsJsonObject().get("children").getAsJsonArray();
				this.replies = new ArrayList<Comment>();
		        for (int i = 0; i < allChildren.size()-1; i += 1) {
		            this.replies.set(i, new Comment(allChildren.get(i).getAsJsonObject(), gson));
		        }
			}
		}
	}
	
	/**
	 * Regular constructor to create a Comment.
	 * @param ups The number of upvotes associated with this Comment.
	 * @param downs The number of downvotes associated with this Comment.
	 * @param created The date that this Comment was created.
	 * @param subreddit The subreddit that this Comment was made in.
	 * @param id A unique ID for this Comment. A combination of letters, numbers, and dashes.
	 * @param author The username of the author of this Post.
	 * @param body The text of this post, without any markup.
	 * @param bodyHtml The HTML text of this post.
	 * @param replies A list of comments that are in reply to this one.
	 * @return 
	 */
	public  Comment(int ups, int downs, int created, String subreddit, String id, String author, String body, String bodyHtml, ArrayList<Comment> replies) {
		this.ups = ups;
		this.downs = downs;
		this.created = created;
		this.subreddit = subreddit;
		this.id = id;
		this.author = author;
		this.body = body;
		this.bodyHtml = bodyHtml;
		this.replies = replies;
	}
	
}

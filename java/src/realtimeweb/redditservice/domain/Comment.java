package realtimeweb.redditservice.domain;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 
 */
public class Comment {
	private int ups;
	private int downs;
	private int created;
	private String subreddit;
	private String id;
	private String author;
	private String body;
	private String bodyHtml;
	private ArrayList<Comment> replies;
	/**
	 * 
	
	 * @return int
	 */
	public int getUps() {
		return this.ups;
	}
	
	/**
	 * 
	 * @param ups
	 */
	public void setUps(int ups) {
		this.ups = ups;
	}
	
	/**
	 * 
	
	 * @return int
	 */
	public int getDowns() {
		return this.downs;
	}
	
	/**
	 * 
	 * @param downs
	 */
	public void setDowns(int downs) {
		this.downs = downs;
	}
	
	/**
	 * 
	
	 * @return int
	 */
	public int getCreated() {
		return this.created;
	}
	
	/**
	 * 
	 * @param created
	 */
	public void setCreated(int created) {
		this.created = created;
	}
	
	/**
	 * 
	
	 * @return String
	 */
	public String getSubreddit() {
		return this.subreddit;
	}
	
	/**
	 * 
	 * @param subreddit
	 */
	public void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
	}
	
	/**
	 * 
	
	 * @return String
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 
	
	 * @return String
	 */
	public String getAuthor() {
		return this.author;
	}
	
	/**
	 * 
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * 
	
	 * @return String
	 */
	public String getBody() {
		return this.body;
	}
	
	/**
	 * 
	 * @param body
	 */
	public void setBody(String body) {
		this.body = body;
	}
	
	/**
	 * 
	
	 * @return String
	 */
	public String getBodyHtml() {
		return this.bodyHtml;
	}
	
	/**
	 * 
	 * @param bodyHtml
	 */
	public void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}
	
	/**
	 * 
	
	 * @return ArrayList<Comment>
	 */
	public ArrayList<Comment> getReplies() {
		return this.replies;
	}
	
	/**
	 * 
	 * @param replies
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
	 * 
	 * @param json
	 * @param gson
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
}

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
public class Post {
	private int ups;
	private int downs;
	private int created;
	private String subreddit;
	private String id;
	private String title;
	private String author;
	private boolean isSelf;
	private boolean isNsfw;
	private String content;
	private String permalink;
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
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
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
	
	 * @return boolean
	 */
	public boolean getIsSelf() {
		return this.isSelf;
	}
	
	/**
	 * 
	 * @param isSelf
	 */
	public void setIsSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
	
	/**
	 * 
	
	 * @return boolean
	 */
	public boolean getIsNsfw() {
		return this.isNsfw;
	}
	
	/**
	 * 
	 * @param isNsfw
	 */
	public void setIsNsfw(boolean isNsfw) {
		this.isNsfw = isNsfw;
	}
	
	/**
	 * 
	
	 * @return String
	 */
	public String getContent() {
		return this.content;
	}
	
	/**
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 
	
	 * @return String
	 */
	public String getPermalink() {
		return this.permalink;
	}
	
	/**
	 * 
	 * @param permalink
	 */
	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}
	
	/**
	 * 
	
	 * @return String
	 */
	public String toString() {
		return "Post[" + ups + ", " + downs + ", " + created + ", " + subreddit + ", " + id + ", " + title + ", " + author + ", " + isSelf + ", " + isNsfw + ", " + content + ", " + permalink + "]";
	}
	
	/**
	 * 
	 * @param json
	 * @param gson
	 * @return 
	 */
	public  Post(JsonObject json, Gson gson) {
		this.ups = json.get("data").getAsJsonObject().get("ups").getAsInt();
		this.downs = json.get("data").getAsJsonObject().get("downs").getAsInt();
		this.created = json.get("data").getAsJsonObject().get("created").getAsInt();
		this.subreddit = json.get("data").getAsJsonObject().get("subreddit").getAsString();
		this.id = json.get("data").getAsJsonObject().get("id").getAsString();
		this.title = json.get("data").getAsJsonObject().get("title").getAsString();
		this.author = json.get("data").getAsJsonObject().get("author").getAsString();
		this.isSelf = json.get("data").getAsJsonObject().get("is_self").getAsBoolean();
		this.isNsfw = json.get("data").getAsJsonObject().get("over_18").getAsBoolean();
		this.content = json.get("data").getAsJsonObject().get("selftext").getAsString();
		this.permalink = json.get("data").getAsJsonObject().get("permalink").getAsString();
	}
	
}
